package com.anish.service;

import java.util.List;

import com.anish.entities.UserEntity;

public interface UserServiceInterface 
{
	public void save(UserEntity user);
	public void update(UserEntity user);
	public void delete(UserEntity user);
	public UserEntity findById(int id);
	public List<UserEntity> findAll();
	public UserEntity findByUsername(String username);
	
}
