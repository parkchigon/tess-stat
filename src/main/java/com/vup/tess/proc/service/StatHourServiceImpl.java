package com.vup.tess.proc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vup.tess.model.PointMaster;
import com.vup.tess.model.RawData;
import com.vup.tess.model.StatHourly;
import com.vup.tess.repository.RawDataRepository;
import com.vup.tess.repository.StatHourlyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatHourServiceImpl implements StatHourService{
	@Autowired
	private RawDataRepository rawDataRepository;
	
	@Autowired
	private StatHourlyRepository statHourlyRepository;

	@Transactional(readOnly = true)
	public List<RawData> findByPointMasterAndFullPointStatusBetween(String factoryId, String energyGroupId,
			String pointId, String beforePointStatusDate, String pointStatusDate) {
		// TODO Auto-generated method stub
		return rawDataRepository.findByPointMasterAndFullPointStatusBetween(factoryId, energyGroupId, pointId, beforePointStatusDate, pointStatusDate);
	}

	@Transactional(readOnly = false)
	public void statHourlySave(StatHourly statHour) {
		statHourlyRepository.save(statHour);
	}

	@Transactional(readOnly = true)
	public List<StatHourly> findByPointMasterAndStatDateHStartingWith(PointMaster tempPointmaster, String beforeHour) {
		return statHourlyRepository.findByPointMasterAndStatDateHStartingWith(tempPointmaster, beforeHour);
	}
	
	

}
