package com.anish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anish.entities.UserEntity;
import com.anish.repository.UserRepository;


@Service
public class UserServiceInterfaceImpl implements UserServiceInterface {
	
@Autowired	
private UserRepository userRepository;

	@Override
	public void save(UserEntity user) {
		userRepository.save(user);
		
	}

	@Override
	public void update(UserEntity user) {
		userRepository.save(user);
		
	}

	@Override
	public void delete(UserEntity user) {
		userRepository.delete(user);
		
	}

	@Override
	public UserEntity findById(int id) {
		
		return userRepository.findById(id).get();
	}

	@Override
	public List<UserEntity> findAll() {
		return userRepository.findAll();
	}

	@Override
	public UserEntity findByUsername(String username) {
		UserEntity userEntity = userRepository.findByUsername(username);
		System.out.println(userEntity);
		return userEntity;
	}
	
	
	
	
	
	/*
	@Autowired
	private UserDaoInterface userDaoInterface;
	
	

	public UserDaoInterface getUserInterface() {
		return userDaoInterface;
	}

	public void setUserInterface(UserDaoInterface userInterface) {
		this.userDaoInterface = userInterface;
	}

	@Override
	public void save(UserEntity user) {
		userDaoInterface.save(user);

	}

	@Override
	public void update(UserEntity user) {
		userDaoInterface.update(user);

	}

	@Override
	public void delete(UserEntity user) {
		userDaoInterface.delete(user);

	}

	@Override
	public UserEntity findById(int id) {
		
		return userDaoInterface.findById(id);
	}

	@Override
	public List<UserEntity> findAll() {
		
		return userDaoInterface.findAll();

}

	@Override
	public UserEntity findByUsername(String username) {
		
		return userDaoInterface.findByUsername(username);
	}

	*/
}
