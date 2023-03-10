package com.vup.tess.proc.service;

import java.util.List;

import com.vup.tess.model.PointMaster;
import com.vup.tess.model.StatDaily;

public interface StatDailyService {

	void statDailySave(StatDaily statDaily);

	List<StatDaily> findByPointMasterAndStatDateDStartingWith(PointMaster tempPointmaster, String beforeHour);

}
