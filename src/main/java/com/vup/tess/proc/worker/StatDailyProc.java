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
import com.vup.tess.model.StatDaily;
import com.vup.tess.model.StatHourly;
import com.vup.tess.model.key.PointMasterId;
import com.vup.tess.proc.service.EnergyGroupMasterService;
import com.vup.tess.proc.service.FactoryMasterService;
import com.vup.tess.proc.service.PointMasterService;
import com.vup.tess.proc.service.StatDailyService;
import com.vup.tess.proc.service.StatHourService;

@Service
public class StatDailyProc {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PointMasterService pointMasterService;
	
	@Autowired 
	private StatHourService statHourService;
	
	@Autowired 
	private FactoryMasterService factoryMasterService;
	
	@Autowired 
	private EnergyGroupMasterService energyGroupMasterService;
	
	@Autowired 
	private StatDailyService statDailyService;
	
	public void doService() {
		try {
			logger.info("########### Start daily statistics ########### ");
			//pointList get
			List<PointMaster> pointMaster = pointMasterService.findAll();
			//System.out.println("pointMaster.size():" + pointMaster.size());
			for(int i = 0 ; i < pointMaster.size() ; i ++) {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				Calendar time = Calendar.getInstance();
				//한시간전 날짜 가져오기
				time.add(Calendar.DATE, -1);
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
				StatDaily statDaily = null;
				List<StatHourly> list = statHourService.findByPointMasterAndStatDateHStartingWith(tempPointmaster,beforeHour);
				if (list.size() != 0 ) {
					if(list.get(0).getPointValue() == null) {
						statDaily = StatDaily.builder()
								.statDateD(beforeHour)
								.pointMaster(pointMaster.get(i))
								.build();
						statDailyService.statDailySave(statDaily);
						continue;
					}
					List<BigDecimal> tempPointValue = new LinkedList<>();
					//List<BigDecimal> tempPointAvr = new LinkedList<>();
					List<Integer> tempCount = new ArrayList<Integer> ();
					for(int j = 0 ; j < list.size() ; j++) {
						tempPointValue.add(list.get(j).getPointValue());
						//tempPointAvr.add(list.get(j).getPointAverage());
						tempCount.add(list.get(j).getPointCount());
					}
					BigDecimal pointSum = BigDecimal.ZERO;
					for(BigDecimal value : tempPointValue) {
						pointSum = pointSum.add(value);
				    }
					//BigDecimal pointAvr = BigDecimal.ZERO;
					/*
					 * for(BigDecimal value : tempPointAvr) { pointAvr = pointAvr.add(value); }
					 */

					int countSum = tempCount.stream().mapToInt(Integer::intValue).sum();
					System.out.println("pointSum::"+pointSum);
					BigDecimal pointAvr = pointSum.divide(BigDecimal.valueOf(countSum),3,BigDecimal.ROUND_HALF_UP);
					
					
					statDaily = StatDaily.builder()
								.statDateD(beforeHour)
								.pointMaster(pointMaster.get(i))
								.pointValue(pointSum)
								.pointAverage(pointAvr.abs())
								.pointCount(countSum)
								.build();
					statDailyService.statDailySave(statDaily);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
