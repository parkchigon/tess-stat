package com.vup.tess.proc.service;

import java.util.List;

import com.vup.tess.model.PointMaster;
import com.vup.tess.model.RawData;
import com.vup.tess.model.StatHourly;

public interface StatHourService {

	List<RawData> findByPointMasterAndFullPointStatusBetween(String factoryId, String energyGroupId, String pointId,
			String beforePointStatusDate, String pointStatusDate);

	void statHourlySave(StatHourly statHour);

	List<StatHourly> findByPointMasterAndStatDateHStartingWith(PointMaster tempPointmaster, String beforeHour);

}
