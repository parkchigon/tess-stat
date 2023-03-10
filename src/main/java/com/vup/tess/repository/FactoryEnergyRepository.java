package com.vup.tess.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vup.tess.model.FactoryEnergy;
import com.vup.tess.model.key.FactoryEnergyId;

@Repository
public interface FactoryEnergyRepository  extends JpaRepository<FactoryEnergy, FactoryEnergyId> {

}
