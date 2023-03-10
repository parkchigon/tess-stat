package com.vup.tess.proc.worker;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vup.tess.model.EnergyGroup;
import com.vup.tess.model.FactoryMaster;
import com.vup.tess.model.PointMaster;
import com.vup.tess.model.StatMonthly;
import com.vup.tess.model.StatYearly;
import com.vup.tess.model.key.PointMasterId;
import com.vup.tess.proc.service.EnergyGroupMasterService;
import com.vup.tess.proc.service.FactoryMasterService;
import com.vup.tess.proc.service.PointMasterService;
import com.vup.tess.proc.service.StatMonthlyService;
import com.vup.tess.proc.service.StatYearlyService;

@Service
public class StatYearlyProc {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PointMasterService pointMasterService;
	
	@Autowired 
	private FactoryMasterService factoryMasterService;
	
	@Autowired 
	private EnergyGroupMasterService energyGroupMasterService;
	
	@Autowired 
	private StatMonthlyService statMonthlyService;
	
	@Autowired 
	private StatYearlyService statYearlyService;
	
	public void doService() {
		try {
			logger.info("########### Start monthly statistics ########### ");
			//pointList get
			List<PointMaster> pointMaster = pointMasterService.findAll();
			for(int i = 0 ; i < pointMaster.size() ; i ++) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy");
				Calendar time = Calendar.getInstance();
				//한시간전 날짜 가져오기
				time.add(Calendar.YEAR, -1);
				String beforeHour = format.format(time.getTime());
				
				Optional<FactoryMaster> factoryId = factoryMasterService.findById(pointMaster.get(i).getSeq().getFactoryId().getFactoryId());
				
				Optional<EnergyGroup> energyGroupId = energyGroupMasterService.findById(pointMaster.get(i).getSeq().getEnergyGroupId().getEnergyGroupId());
				
				String pointId = pointMaster.get(i).getSeq().getPointId();
				
				PointMasterId pointMasterId = new PointMasterId();
				pointMasterId.setFactoryId(factoryId.get());
				pointMasterId.setEnergyGroupId(energyGroupId.get());
				pointMasterId.setPointId(pointId);
				
				PointMaster tempPointmaster = new PointMaster();
				tempPointmaster.setSeq(pointMasterId);
				
				List<StatMonthly> list = statMonthlyService.findByPointMasterAndStatDateMStartingWith(tempPointmaster,beforeHour);
				logger.debug("Month list:({}), size({})",list, list.size());
				StatYearly statYearly = null;
				if (list.size() == 0) {
					statYearly = StatYearly.builder()
							.pointMaster(pointMaster.get(i))
							.statDateY(beforeHour)
							.build();
					
					statYearlyService.statYearlySave(statYearly);
					logger.info("no data!!!!");
				}else {
					if(list.get(0).getPointValue() == null) {
						statYearly = StatYearly.builder()
								.pointMaster(pointMaster.get(i))
								.statDateY(beforeHour)
								.build();
						
						statYearlyService.statYearlySave(statYearly);
					}
					List<BigDecimal> tempPointValue = new LinkedList<>();
					List<BigDecimal> tempPointAvr = new LinkedList<>();
					List<Integer> tempCount = new ArrayList<Integer> ();
					for(int j = 0 ; j < list.size() ; j++) {
						tempPointValue.add(list.get(j).getPointValue());
						tempPointAvr.add(list.get(j).getPointAverage());
						tempCount.add(list.get(j).getPointCount());
					}
					BigDecimal pointSum = BigDecimal.ZERO;
					for(BigDecimal value : tempPointValue) {
						if (value == null) {
							value = BigDecimal.ZERO;
						}
						pointSum = pointSum.add(value);
				    }
					/*
					 * BigDecimal pointAvr = BigDecimal.ZERO; for(BigDecimal value : tempPointAvr) {
					 * pointAvr = pointAvr.add(value); }
					 */
					
					int countSum = tempCount.stream().mapToInt(Integer::intValue).sum();
					BigDecimal pointAvr = BigDecimal.ZERO;
					if(pointSum  == BigDecimal.ZERO) {
						pointAvr = BigDecimal.ZERO;
					}else {
						pointAvr = pointSum.divide(BigDecimal.valueOf(countSum),3,BigDecimal.ROUND_HALF_UP);					
					}
					logger.debug("pointSum({}),pointAvr({}),countSum({})",pointSum, pointAvr, countSum);
					
					
					statYearly = StatYearly.builder()
							.pointMaster(pointMaster.get(i))
							.pointValue(pointSum)
							.pointAverage(pointAvr)
							.pointCount(countSum)
							.statDateY(beforeHour)
							.build();
					
					statYearlyService.statYearlySave(statYearly);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
