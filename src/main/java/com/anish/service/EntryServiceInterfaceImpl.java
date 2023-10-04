package com.anish.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anish.entities.EntryEntity;
import com.anish.entities.data.GroupedData;
import com.anish.repository.EntryRepository;
import com.anish.repository.MoneyRepository;

@Service
public class EntryServiceInterfaceImpl implements EntryServiceInterface {
	
	@Autowired
	private EntryRepository entryRepository;
	
	@Autowired
	private MoneyRepository moneyRepository;

	@Override
	public void save(EntryEntity entry) {
		entryRepository.save(entry);
	}

	@Override
	public void update(EntryEntity entry) {
		entryRepository.save(entry);
		
	}

	@Override
	public void delete(EntryEntity entry) {
		entryRepository.delete(entry);
		
	}

	@Override
	public EntryEntity findById(int id) {
		
		return entryRepository.findById(id).get();
	}

	@Override
	public List<EntryEntity> findAll() {
		
		return entryRepository.findAll();
	}

	@Override
	public List<EntryEntity> findByUserId(int userid) {
		List<EntryEntity> entryEntity=entryRepository.findByUserid(userid);
		System.out.println(entryEntity);
		return entryEntity;
	}

	@Override
	public Integer listOfEntriesForUser(Integer userid) {
		Integer entryEntity=entryRepository.listOfEntriesForUser(userid);
		return entryEntity;
	}

	@Override
	public Integer toalNumberOfEntriesForUser(Integer userid) {
		Integer entryEntity=entryRepository.toalNumberOfEntriesForUser(userid);
		return entryEntity;
	}

	@Override
	public List<GroupedData> getMoneyListByUserId(Long userid) {
		return moneyRepository.getMoneyListByUserId(userid);
	}
	
	public EntryEntity getEntryByIdAndDate(String entrydate, Integer entryId) {
		LocalDate localDate = LocalDate.parse(entrydate);
		return entryRepository.getEntryByIdAndDate(Date.valueOf(localDate), entryId);
	}

	@Override
	public List<EntryEntity> getEntryByDate(String entrydate) {
		LocalDate localDate = LocalDate.parse(entrydate);
		List<EntryEntity> entryEntity=entryRepository.getEntryByDate(Date.valueOf(localDate));
		return entryEntity;
	}

	@Override
	public List<EntryEntity> getEntryByUserIdAndDate(String entrydate, Integer userid) {
		LocalDate localDate = LocalDate.parse(entrydate);
		List<EntryEntity> entryEntity=entryRepository.getEntryByUserIdAndDate(Date.valueOf(localDate), userid);
		return entryEntity;
	}
	
	
	//public EntryEntity getEntryByIdAndDate(String entrydate, Integer entryId) {
	//	LocalDate localDate = LocalDate.parse(entrydate);
	//	return entryRepository.getEntryByIdAndDate(Date.valueOf(localDate), entryId);
	
	
	/*@Autowired
	private EntryDaoInterface entryDaoInterface;
	
	

	public EntryDaoInterface getEntryDaoInterface() {
		return entryDaoInterface;
	}

	public void setEntryDaoInterface(EntryDaoInterface entryDaoInterface) {
		this.entryDaoInterface = entryDaoInterface;
	}

	@Override
	public void save(EntryEntity entry) {
		entryDaoInterface.save(entry);
	}

	@Override
	public void update(EntryEntity entry) {
		entryDaoInterface.update(entry);
	}

	@Override
	public void delete(EntryEntity entry) {
		entryDaoInterface.delete(entry);
		
	}

	@Override
	public EntryEntity findById(int id) {
		
		return entryDaoInterface.findById(id);
	}

	@Override
	public List<EntryEntity> findAll() {
		
		return entryDaoInterface.findAll();
	}

	@Override
	public List<EntryEntity> findByUserId(int id) {
		
		return entryDaoInterface.findByUserId(id);
	}*/

	

}
