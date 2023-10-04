package com.anish.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anish.entities.SubjectsDetailsByDate;
import com.anish.entities.SubjectsDetailsByDatePk;
import com.anish.entities.data.TestsGroupedData;

@Repository
public interface SubjectsDetailsByDateRepository extends JpaRepository<SubjectsDetailsByDate, SubjectsDetailsByDatePk> {
	
	@Query(value = "SELECT max(subjectsDetailsByDatePk.id) FROM SubjectsDetailsByDate")
	public Integer max();
	
	@Query(value = "select count(*) from SubjectsDetailsByDate e "
			+ "where e.subjectsDetailsByDatePk.test_date=:testDate and e.completedDate is null")
	public Long dataExistsForTradeDate(@Param(value = "testDate") Date testDate);
	
	@Query(value = "select b from SubjectsDetailsByDate b where b.subjectsDetailsByDatePk.id="
			+ "(SELECT min(a.subjectsDetailsByDatePk.id) FROM SubjectsDetailsByDate a "
			+ "where a.subjectsDetailsByDatePk.test_date=:testDate and a.completedDate is null)")
	public SubjectsDetailsByDate getNextRecord(@Param(value = "testDate") Date testDate);
	
	@Query("select new com.anish.entities.data.TestsGroupedData("
			+ "a.subjectsDetailsByDatePk.test_date as tradeDate, a.gradeId, count(*) as noOfRecords) "
			+ "from SubjectsDetailsByDate a where a.completedDate is null group by a.subjectsDetailsByDatePk.test_date, a.gradeId")
	public List<TestsGroupedData> listOfDates();
	
	
}