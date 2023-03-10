package com.vup.tess.proc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vup.tess.model.PointMaster;
import com.vup.tess.model.StatMonthly;
import com.vup.tess.repository.StatMonthlyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatMonthlyServiceImpl implements StatMonthlyService{
	@Autowired
	private StatMonthlyRepository statMonthlyRepository;

	@Transactional(readOnly = false)
	public void statMonthlySave(StatMonthly statMonthly) {
		statMonthlyRepository.save(statMonthly);
	}

	@Transactional(readOnly = true)
	public List<StatMonthly> findByPointMasterAndStatDateMStartingWith(PointMaster tempPointmaster, String beforeHour) {
		// TODO Auto-generated method stub
		return statMonthlyRepository.findByPointMasterAndStatDateMStartingWith(tempPointmaster, beforeHour);
	}
	
}
