package com.api.jasa.auth.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.jasa.auth.db.entity.TbDetail;

public interface TbDetailRepository extends JpaRepository<TbDetail, Integer> {
	
}