package com.vup.tess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vup.tess.model.RawData;

@Repository
public interface RawDataRepository extends JpaRepository<RawData,String>{
	 
	
	@Query(value="SELECT * FROM tbl_raw_data WHERE factory_id = ?1 AND energy_group_id = ?2 AND point_id= ?3 AND concat(POINT_STATUS_DATE,POINT_STATUS_TIME)  between ?4 and ?5", nativeQuery=true)
	List<RawData> findByPointMasterAndFullPointStatusBetween(String factoryId,
			String energyGroupId, String pointId, String beforeHour, String now);

	@Query(value="SELECT * FROM tbl_raw_data WHERE factory_id = ?1 AND energy_group_id = ?2 AND point_id= ?3 AND concat(POINT_STATUS_DATE,POINT_STATUS_TIME) >= ?4 and concat(POINT_STATUS_DATE,POINT_STATUS_TIME) < ?5", nativeQuery=true)
	List<RawData> instantList(String factoryId, String energyGroupId, String pointId, String beforePointStatusDate,
			String pointStatusDate);

}
