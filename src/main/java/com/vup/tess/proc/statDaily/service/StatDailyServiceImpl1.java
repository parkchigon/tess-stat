package com.vup.tess.proc.statDaily.service;

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
import com.vup.tess.model.key.PointMasterId;
import com.vup.tess.repository.EnergyGroupRepository;
import com.vup.tess.repository.FactoryMasterRepository;
import com.vup.tess.repository.PointMasterRepository;
import com.vup.tess.repository.StatDailyRepository;
import com.vup.tess.repository.StatHourlyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatDailyServiceImpl1 implements StatDailyService1{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PointMasterRepository pointMasterRepository;
	
	@Autowired
	private FactoryMasterRepository factoryMasterRepository;
	
	@Autowired
	private EnergyGroupRepository energyGroupRepository;
	
	@Autowired
	private StatHourlyRepository statHourlyRepository;
	
	@Autowired
	private StatDailyRepository statDailyRepository;
	
	@Override
	@Transactional
	public void doService() {
		statDailyRun();
	}

	private void statDailyRun() {
		//pointList get
		List<PointMaster> pointMaster = pointMasterRepository.findAll();
		//System.out.println("pointMaster.size():" + pointMaster.size());
		for(int i = 0 ; i < pointMaster.size() ; i ++) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Calendar time = Calendar.getInstance();
			//한시간전 날짜 가져오기
			time.add(Calendar.DATE, -1);
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
			StatDaily statDaily = null;
			List<StatHourly> list = statHourlyRepository.findByPointMasterAndStatDateHStartingWith(tempPointmaster,beforeHour);
			if (list.size() != 0 ) {
				if(list.get(0).getPointValue() == null) {
					statDaily = StatDaily.builder()
							.statDateD(beforeHour)
							.pointMaster(pointMaster.get(i))
							.build();
				statDailyRepository.save(statDaily);
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
				statDailyRepository.save(statDaily);
				
			}
		}
		
	}
	
	
}
