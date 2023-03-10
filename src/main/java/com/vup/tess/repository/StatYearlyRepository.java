package com.vup.tess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vup.tess.model.StatYearly;

@Repository
public interface StatYearlyRepository extends JpaRepository<StatYearly, String>{

}
