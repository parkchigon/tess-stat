package com.vup.tess.proc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vup.tess.model.PointMaster;
import com.vup.tess.model.StatDaily;
import com.vup.tess.repository.StatDailyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatDailyServiceImpl implements StatDailyService{
	@Autowired
	private StatDailyRepository statDailyRepository;

	@Transactional(readOnly = false)
	public void statDailySave(StatDaily statDaily) {
		statDailyRepository.save(statDaily);
	}

	@Transactional(readOnly = true)
	public List<StatDaily> findByPointMasterAndStatDateDStartingWith(PointMaster tempPointmaster, String beforeHour) {
		return statDailyRepository.findByPointMasterAndStatDateDStartingWith(tempPointmaster, beforeHour);
	}
	
}
