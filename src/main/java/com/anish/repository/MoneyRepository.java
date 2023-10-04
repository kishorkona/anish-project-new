package com.anish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.anish.entities.MoneyEntity;
import com.anish.entities.data.GroupedData;
@Repository
public interface MoneyRepository extends JpaRepository<MoneyEntity, Long> {
	
	@Query("select new com.anish.entities.data.GroupedData(a.receivedDate, a.amount, a.currency, b.name) "
			+ "from MoneyEntity a INNER JOIN RecievedFromEntity b on a.receivedFromId=b.id where a.userid=:userid")
	public List<GroupedData> getMoneyListByUserId(@Param(value = "userid") Long userid);

}