package com.vup.tess.proc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vup.tess.model.StatYearly;
import com.vup.tess.repository.StatYearlyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatYearlyServiceImpl implements StatYearlyService{
	@Autowired
	private StatYearlyRepository statYearlyRepository;

	@Override
	public void statYearlySave(StatYearly statYearly) {
		statYearlyRepository.save(statYearly);
	}
	
}
