package com.vup.tess.proc.statYearly.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.transaction.annotation.Transactional;

import com.vup.tess.model.EnergyGroup;
import com.vup.tess.model.FactoryMaster;
import com.vup.tess.model.PointMaster;
import com.vup.tess.model.StatMonthly;
import com.vup.tess.model.StatYearly;
import com.vup.tess.model.key.PointMasterId;
import com.vup.tess.repository.EnergyGroupRepository;
import com.vup.tess.repository.FactoryMasterRepository;
import com.vup.tess.repository.PointMasterRepository;
import com.vup.tess.repository.StatMonthlyRepository;
import com.vup.tess.repository.StatYearlyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatYearlyServiceImpl1 implements StatYearlyService1{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PointMasterRepository pointMasterRepository;
	
	@Autowired
	private FactoryMasterRepository factoryMasterRepository;
	
	@Autowired
	private EnergyGroupRepository energyGroupRepository;
	
	@Autowired
	private StatMonthlyRepository statMonthlyRepository;
	
	@Autowired
	private StatYearlyRepository statYearlyRepository;
	
	@Override
	@Transactional
	public void doService() {
		statYearly();
		
	}

	private void statYearly() {
		//pointList get
		String useFlag ="Y";
		String chargeFlag ="Y";
		List<PointMaster> pointMaster = pointMasterRepository.findByUseFlagAndPointChargeFlag(useFlag,chargeFlag);
		//System.out.println("pointMaster.size():" + pointMaster.size());
		for(int i = 0 ; i < pointMaster.size() ; i ++) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			Calendar time = Calendar.getInstance();
			//한시간전 날짜 가져오기
			time.add(Calendar.YEAR, -1);
			String beforeHour = format.format(time.getTime());
			
			Optional<FactoryMaster> factoryId = factoryMasterRepository.findById(pointMaster.get(i).getSeq().getFactoryId().getFactoryId());
			
			Optional<EnergyGroup> energyGroupId = energyGroupRepository.findById(pointMaster.get(i).getSeq().getEnergyGroupId().getEnergyGroupId());
			
			String pointId = pointMaster.get(i).getSeq().getPointId();
			
			PointMasterId pointMasterId = new PointMasterId();
			pointMasterId.setFactoryId(factoryId.get());
			pointMasterId.setEnergyGroupId(energyGroupId.get());
			pointMasterId.setPointId(pointId);
			
			PointMaster tempPointmaster = new PointMaster();
			tempPointmaster.setSeq(pointMasterId);
			
			List<StatMonthly> list = statMonthlyRepository.findByPointMasterAndStatDateMStartingWith(tempPointmaster,beforeHour);
			logger.debug("Month list:({}), size({})",list, list.size());
			StatYearly statYearly = null;
			if (list.size() == 0) {
				statYearly = StatYearly.builder()
						.pointMaster(pointMaster.get(i))
						.statDateY(beforeHour)
						.build();
				
				statYearlyRepository.save(statYearly);
				logger.info("no data!!!!");
			}else {
				if(list.get(0).getPointValue() == null) {
					statYearly = StatYearly.builder()
							.pointMaster(pointMaster.get(i))
							.statDateY(beforeHour)
							.build();
					
					statYearlyRepository.save(statYearly);
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
				
				statYearlyRepository.save(statYearly);
			}
		}
		
	}

}
