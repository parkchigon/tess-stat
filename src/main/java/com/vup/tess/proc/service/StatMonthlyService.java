package com.vup.tess.proc.service;

import java.util.List;

import com.vup.tess.model.PointMaster;
import com.vup.tess.model.StatMonthly;

public interface StatMonthlyService {

	void statMonthlySave(StatMonthly statMonthly);

	List<StatMonthly> findByPointMasterAndStatDateMStartingWith(PointMaster tempPointmaster, String beforeHour);

}
