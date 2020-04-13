package com.api.jasa.auth.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.jasa.auth.db.entity.TbMaster;

public interface TbMasterRepository extends JpaRepository<TbMaster, Integer> {
	
}