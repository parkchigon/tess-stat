package com.vup.tess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vup.tess.model.PointMaster;
import com.vup.tess.model.StatDaily;

@Repository
public interface StatDailyRepository extends JpaRepository<StatDaily, String>{

	List<StatDaily> findByPointMasterAndStatDateDStartingWith(PointMaster tempPointmaster, String beforeHour);

	List<StatDaily> findByStatDateD(String beforeHour);
	
	

}
