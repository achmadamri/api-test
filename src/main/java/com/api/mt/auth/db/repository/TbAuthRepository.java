package com.api.mt.auth.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.mt.auth.db.entity.TbAuth;

public interface TbAuthRepository extends JpaRepository<TbAuth, Integer> {
	
}