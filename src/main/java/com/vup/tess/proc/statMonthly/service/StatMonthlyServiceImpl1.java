package com.vup.tess.proc.statMonthly.service;

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
import com.vup.tess.model.StatDaily;
import com.vup.tess.model.StatHourly;
import com.vup.tess.model.StatMonthly;
import com.vup.tess.model.key.PointMasterId;
import com.vup.tess.repository.EnergyGroupRepository;
import com.vup.tess.repository.FactoryMasterRepository;
import com.vup.tess.repository.PointMasterRepository;
import com.vup.tess.repository.StatDailyRepository;
import com.vup.tess.repository.StatHourlyRepository;
import com.vup.tess.repository.StatMonthlyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatMonthlyServiceImpl1 implements StatMonthlyService1{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PointMasterRepository pointMasterRepository;
	
	@Autowired
	private FactoryMasterRepository factoryMasterRepository;
	
	@Autowired
	private EnergyGroupRepository energyGroupRepository;
	
	@Autowired
	private StatDailyRepository statDailyRepository;
	
	@Autowired
	private StatMonthlyRepository statMonthlyRepository;
	
	
	@Override
	@Transactional
	public void doService() {
		statMonthly();
		
	}

	private void statMonthly() {
		//pointList get
		List<PointMaster> pointMaster = pointMasterRepository.findAll();;
		//System.out.println("pointMaster.size():" + pointMaster.size());
		for(int i = 0 ; i < pointMaster.size() ; i ++) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			Calendar time = Calendar.getInstance();
			//한시간전 날짜 가져오기
			time.add(Calendar.MONTH, -1);
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
			
			List<StatDaily> list = statDailyRepository.findByPointMasterAndStatDateDStartingWith(tempPointmaster,beforeHour);
			StatMonthly statMonthly = null;
			if (list.size() == 0) {
				statMonthly = StatMonthly.builder()
						.pointMaster(pointMaster.get(i))
						.statDateM(beforeHour)
						.build();
				logger.debug("statMonthly({})",statMonthly);
				statMonthlyRepository.save(statMonthly);
				logger.info("no data!!!!");
			}else {
				if(list.get(0).getPointValue() == null) {
					statMonthly = StatMonthly.builder()
							.pointMaster(pointMaster.get(i))
							.statDateM(beforeHour)
							.build();
					logger.debug("statMonthly({})",statMonthly);
					statMonthlyRepository.save(statMonthly);
					continue;
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
				BigDecimal pointAvr = BigDecimal.ZERO;
				 * for(BigDecimal value : tempPointAvr) { pointAvr = pointAvr.add(value); }
				 */
				
				int countSum = tempCount.stream().mapToInt(Integer::intValue).sum();
				BigDecimal pointAvr = BigDecimal.ZERO;
				if(pointSum  == BigDecimal.ZERO) {
					pointAvr = BigDecimal.ZERO;
				}else {
					pointAvr = pointSum.divide(BigDecimal.valueOf(countSum),3,BigDecimal.ROUND_HALF_UP);					
				}
				logger.debug("pointSum({}),pointAvr({}),countSum({})",pointSum, pointAvr, countSum);
				
				
				statMonthly = StatMonthly.builder()
						.pointMaster(pointMaster.get(i))
						.statDateM(beforeHour)
						.pointValue(pointSum)
						.pointAverage(pointAvr)
						.pointCount(countSum)
						.build();
				logger.debug("statMonthly({})",statMonthly);
				statMonthlyRepository.save(statMonthly);
			}
		}
		
	}

}
