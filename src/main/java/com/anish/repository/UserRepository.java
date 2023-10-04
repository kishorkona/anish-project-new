package com.anish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anish.entities.UserEntity;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	public UserEntity findByUsername(String username);
}
