package com.anish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anish.entities.SubjectsDetails;

@Repository
public interface SubjectsDetailsRepository extends JpaRepository<SubjectsDetails, Integer> {
	

}
