package com.anish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anish.entities.Subjects;

@Repository
public interface SubjectsRepository extends JpaRepository<Subjects, Integer> {
	Subjects findByCode(String code);
}