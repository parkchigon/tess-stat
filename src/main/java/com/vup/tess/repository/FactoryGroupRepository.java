package com.vup.tess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vup.tess.model.FactoryGroup;

@Repository
public interface FactoryGroupRepository extends JpaRepository<FactoryGroup, String> {

}
