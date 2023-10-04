package com.anish.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.anish.entities.EntryEntity;
import com.anish.entities.data.GroupedData;

public interface EntryServiceInterface {
	public void save(EntryEntity entry);
	public void update(EntryEntity entry);
	public void delete(EntryEntity entry);
	public EntryEntity findById(int id);
	public List<EntryEntity> findAll();
	public List<EntryEntity> findByUserId(int userid);
	public Integer listOfEntriesForUser(Integer userid);
	public Integer toalNumberOfEntriesForUser(Integer userid);
	public List<GroupedData> getMoneyListByUserId(	Long userid);
	public EntryEntity getEntryByIdAndDate(String entrydate, Integer entryId);
	public List<EntryEntity> getEntryByDate(String entrydate);
	public List<EntryEntity> getEntryByUserIdAndDate(String entrydate,Integer userid);
}
