package com.vup.tess.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import com.vup.tess.model.CommonCode;

@Repository
public interface CommonCodeRepository  extends JpaRepository<CommonCode, Integer> {

	@Query(value="SELECT * FROM tb_common_code WHERE code_id = ?1", nativeQuery=true)
	Optional<CommonCode> findByCodeId(String codeId);
	
	//Optional<CommonCode> findByCodeId(String codeId);

}
