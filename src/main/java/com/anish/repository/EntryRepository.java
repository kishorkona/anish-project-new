package com.anish.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anish.entities.EntryEntity;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, Integer> {
	
	public List<EntryEntity> findByUserid(Integer userid);
	
	@Query(value = "select count(*) from EntryEntity e where e.userid=:userid")
	public Integer listOfEntriesForUser(@Param(value = "userid") Integer userid);
	
	@Query(value = "select sum(id) from EntryEntity e where e.userid=:userid")
	public Integer toalNumberOfEntriesForUser(@Param(value = "userid") Integer userid);
	
	@Query(value = "SELECT e FROM EntryEntity e WHERE e.id=:id and e.entrydate=:entrydate")
	public EntryEntity getEntryByIdAndDate(@Param(value = "entrydate") Date entrydate, @Param(value = "id") Integer id);
	
	@Query(value = "SELECT e FROM EntryEntity e WHERE e.entrydate=:entrydate")
	public List<EntryEntity> getEntryByDate(@Param(value = "entrydate") Date entrydate);
	
	@Query(value = "SELECT e FROM EntryEntity e WHERE e.userid=:userid and e.entrydate=:entrydate")
	public List<EntryEntity> getEntryByUserIdAndDate(@Param(value = "entrydate") Date entrydate, @Param(value = "userid") Integer userid);
}

