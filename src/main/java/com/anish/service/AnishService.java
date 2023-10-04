package com.anish.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anish.view.Pair;
import com.anish.entities.Subjects;
import com.anish.entities.SubjectsDetails;
import com.anish.entities.SubjectsDetailsByDate;
import com.anish.entities.SubjectsDetailsByDatePk;
import com.anish.entities.data.TestsGroupedData;
import com.anish.repository.SubjectsDetailsByDateRepository;
import com.anish.repository.SubjectsDetailsRepository;
import com.anish.repository.SubjectsRepository;


@Service
public class AnishService {
	
	@Autowired SubjectsRepository subjectsRepo;
	@Autowired SubjectsDetailsRepository subjectsRepoDetail;
	@Autowired SubjectsDetailsByDateRepository subjectsByDate;
	
	private static String url = "https://www.ixl.com/math/grade-";
	
	public String createTest(LocalDate testDate, Integer grade) {
		try {
			Long records = getTotalRows(testDate);
			if(records<=0) {
				List<Pair> data = this.getUrls(grade);
				if(data != null) {
					Integer maxValue = subjectsByDate.max();
					AtomicInteger maxLongValue = new AtomicInteger((maxValue==null) ?0:maxValue);
					List<SubjectsDetailsByDate> dataToUpdate = data.stream().map(x -> {
						SubjectsDetailsByDate obj = new SubjectsDetailsByDate();
						String[] arr = x.getFirst().split("-");
						SubjectsDetailsByDatePk pk = new SubjectsDetailsByDatePk();
						pk.setId(maxLongValue.incrementAndGet());
						pk.setTest_date(Date.valueOf(testDate));
						obj.setSubjectsDetailsByDatePk(pk);
						obj.setCode(arr[0]);
						obj.setDetailId(Integer.valueOf(arr[1]));
						obj.setUrl(x.getSecond());
						obj.setGradeId(grade);
						return obj;
					}).collect(Collectors.toList());
					subjectsByDate.saveAllAndFlush(dataToUpdate);
					return "Success";
				}
			} else {
				return "Test already there for this date="+testDate;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error creating test for date="+testDate;
	}
	
	private List<Pair> getUrls(Integer grade) throws Exception {
		List<Pair> urls = new ArrayList<>();
        try {
        	Map<Integer, Subjects> subjectsMapData = subjectsRepo.findAll().stream().filter(x -> {
        		if(x.getIsActive()!=0 && x.getGradeId()==grade) {
        			return true;
        		}
        		return false;
        	}).collect(Collectors.toMap(k -> Integer.valueOf(k.getId()), v -> v));
        	
        	List<SubjectsDetails> data = subjectsRepoDetail.findAll().stream().filter(x -> {
        		if(x.getIsActive()==0) {
        			return false;
        		}
        		return true;
        	}).collect(Collectors.toList());
        	/*
        	Map<Integer, Map<Integer, List<SubjectsDetails>>> map = data.collect(
        			Collectors.groupingBy(SubjectsDetails::getSubjectId,Collectors.groupingBy(SubjectsDetails::getDetailId)));
        	*/
        	Map<Integer, AtomicInteger> maxCountMap = new HashMap<>();
        	Map<Integer, SubjectsDetails> mapData = new HashMap<>();
        	Map<Integer, Boolean> allDone = new HashMap<>();        	
        	if(data.size()>0) {
        		data.forEach(x -> {
            		maxCountMap.put(x.getId(), new AtomicInteger(0));
            		mapData.put(x.getId(), x);
            	});
            	do {
            		maxCountMap.entrySet().stream().forEach(entry -> {
            			SubjectsDetails subjects = mapData.get(entry.getKey());
            			if(subjects.getNoOfQuestions() > maxCountMap.get(entry.getKey()).get()) {
            				Subjects subj = subjectsMapData.get(subjects.getSubjectId());
            				urls.add(new Pair(subj.getCode()+"-"+subjects.getDetailId(),url+subj.getGradeId()+"/"+subjects.getUrl()));
            				maxCountMap.get(entry.getKey()).incrementAndGet();
            				allDone.put(entry.getKey(), Boolean.FALSE);
            			} else {
            				if(allDone.get(entry.getKey()) != null && !allDone.get(entry.getKey())) {
                				allDone.put(entry.getKey(), Boolean.TRUE);
            				}
            			}
            		});
            	} while(maxCountMap.size()==allDone.size() && !allDone.values().stream().allMatch(x -> x==Boolean.TRUE));

            	System.out.println(" ------------------------");
            	urls.stream().forEach(x ->{
            		//System.out.println(x.getFirst()+"::"+x.getSecond());
            		System.out.println(x.getSecond());
            	});
            	System.out.println(" ----------Done--------------");
        	}
        } catch (Exception ex) {
            throw new RuntimeException("ClientsDao->getClients"+ex);
        }
        return urls;
    }
	
	/*
	public String createTest(LocalDate testDate) {
		try {
			Long records = getTotalRows(testDate);
			if(records<=0) {
				List<Pair> data = this.getUrls();
				if(data != null) {
					Integer maxValue = subjectsByDate.max();
					AtomicInteger maxLongValue = new AtomicInteger((maxValue==null) ?0:maxValue);
					List<SubjectsDetailsByDate> dataToUpdate = data.stream().map(x -> {
						SubjectsDetailsByDate obj = new SubjectsDetailsByDate();
						String[] arr = x.getFirst().split("-");
						SubjectsDetailsByDatePk pk = new SubjectsDetailsByDatePk();
						pk.setId(maxLongValue.incrementAndGet());
						pk.setTest_date(Date.valueOf(testDate));
						obj.setSubjectsDetailsByDatePk(pk);
						obj.setCode(arr[0]);
						obj.setDetailId(Integer.valueOf(arr[1]));
						obj.setUrl(x.getSecond());
						return obj;
					}).collect(Collectors.toList());
					subjectsByDate.saveAllAndFlush(dataToUpdate);
					return "Success";
				}
			} else {
				return "Test already there for this date="+testDate;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error creating test for date="+testDate;
	}
	
	private List<Pair> getUrls() throws Exception {
		List<Pair> urls = new ArrayList<>();
        try {
        	Map<Integer, Subjects> subjectsMapData = subjectsRepo.findAll().stream().filter(x -> {
        		if(x.getIsActive()==0) {
        			return false;
        		}
        		return true;
        	}).collect(Collectors.toMap(k -> Integer.valueOf(k.getId()), v -> v));
        	
        	
        	Map<Integer, AtomicInteger> maxCountMap = new HashMap<>();
        	Map<Integer, SubjectsDetails> mapData = new HashMap<>();
        	Map<Integer, Boolean> allDone = new HashMap<>();
        	List<SubjectsDetails> data = subjectsRepoDetail.findAll().stream().filter(x -> {
        		if(x.getIsActive()==0) {
        			return false;
        		}
        		return true;
        	}).collect(Collectors.toList());
        	if(data.size()>0) {
        		data.forEach(x -> {
            		maxCountMap.put(x.getId(), new AtomicInteger(0));
            		mapData.put(x.getId(), x);
            	});
            	do {
            		maxCountMap.entrySet().stream().forEach(entry -> {
            			SubjectsDetails subjects = mapData.get(entry.getKey());
            			if(subjects.getNoOfQuestions() > maxCountMap.get(entry.getKey()).get()) {
            				Subjects subj = subjectsMapData.get(subjects.getSubjectId());
            				urls.add(new Pair(subj.getCode()+"-"+subjects.getDetailId(),url+subj.getGradeId()+"/"+subjects.getUrl()));
            				maxCountMap.get(entry.getKey()).incrementAndGet();
            				allDone.put(entry.getKey(), Boolean.FALSE);
            			} else {
            				if(allDone.get(entry.getKey()) != null && !allDone.get(entry.getKey())) {
                				allDone.put(entry.getKey(), Boolean.TRUE);
            				}
            			}
            		});
            	} while(maxCountMap.size()==allDone.size() && !allDone.values().stream().allMatch(x -> x==Boolean.TRUE));

            	System.out.println(" ------------------------");
            	urls.stream().forEach(x ->{
            		//System.out.println(x.getFirst()+"::"+x.getSecond());
            		System.out.println(x.getSecond());
            	});
            	System.out.println(" ----------Done--------------");
        	}
        } catch (Exception ex) {
            throw new RuntimeException("ClientsDao->getClients"+ex);
        }
        return urls;
    }
	*/
	/*
	public String createTest(LocalDate testDate) {
		try {
			Long records = getTotalRows(testDate);
			if(records<=0) {
				List<Pair> data = this.getUrls();
				if(data != null) {
					Integer maxValue = subjectsByDate.max();
					AtomicInteger maxLongValue = new AtomicInteger((maxValue==null) ?0:maxValue);
					List<SubjectsDetailsByDate> dataToUpdate = data.stream().map(x -> {
						SubjectsDetailsByDate obj = new SubjectsDetailsByDate();
						String[] arr = x.getFirst().split("-");
						obj.setId(maxLongValue.incrementAndGet());
						obj.setCode(arr[0]);
						obj.setDetailId(Integer.valueOf(arr[1]));
						obj.setUrl(x.getSecond());
						obj.setTestDate(Date.valueOf(testDate));
						return obj;
					}).collect(Collectors.toList());
					subjectsByDate.saveAllAndFlush(dataToUpdate);
					return "Success";
				}
			} else {
				return "Test already there for this date="+testDate;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error creating test for date="+testDate;
	}
	
	private List<Pair> getUrls() throws Exception {
		List<Pair> urls = new ArrayList<>();
        try {
        	Map<Integer, Subjects> subjectsMapData = subjectsRepo.findAll().stream().filter(x -> {
        		if(x.getIsActive()==0) {
        			return false;
        		}
        		return true;
        	}).collect(Collectors.toMap(k -> Integer.valueOf(k.getId()), v -> v));
        	
        	
        	Map<Integer, AtomicInteger> maxCountMap = new HashMap<>();
        	Map<Integer, SubjectsDetails> mapData = new HashMap<>();
        	Map<Integer, Boolean> allDone = new HashMap<>();
        	List<SubjectsDetails> data = subjectsRepoDetail.findAll().stream().filter(x -> {
        		if(x.getIsActive()==0) {
        			return false;
        		}
        		return true;
        	}).collect(Collectors.toList());
        	if(data.size()>0) {
        		data.forEach(x -> {
            		maxCountMap.put(x.getId(), new AtomicInteger(0));
            		mapData.put(x.getId(), x);
            	});
            	do {
            		maxCountMap.entrySet().stream().forEach(entry -> {
            			SubjectsDetails subjects = mapData.get(entry.getKey());
            			if(subjects.getNoOfQuestions() > maxCountMap.get(entry.getKey()).get()) {
            				Subjects subj = subjectsMapData.get(subjects.getSubjectId());
            				urls.add(new Pair(subj.getCode()+"-"+subjects.getDetailId(),url+subj.getGradeId()+"/"+subjects.getUrl()));
            				maxCountMap.get(entry.getKey()).incrementAndGet();
            				allDone.put(entry.getKey(), Boolean.FALSE);
            			} else {
            				if(allDone.get(entry.getKey()) != null && !allDone.get(entry.getKey())) {
                				allDone.put(entry.getKey(), Boolean.TRUE);
            				}
            			}
            		});
            	} while(maxCountMap.size()==allDone.size() && !allDone.values().stream().allMatch(x -> x==Boolean.TRUE));

            	System.out.println(" ------------------------");
            	urls.stream().forEach(x ->{
            		//System.out.println(x.getFirst()+"::"+x.getSecond());
            		System.out.println(x.getSecond());
            	});
            	System.out.println(" ----------Done--------------");
        	}
        } catch (Exception ex) {
            throw new RuntimeException("ClientsDao->getClients"+ex);
        }
        return urls;
    }
	*/
	
	public SubjectsDetailsByDate getTestLink(LocalDate testDate) {
		try {
			return subjectsByDate.getNextRecord(Date.valueOf(testDate));
		} catch (Exception e) {
			throw new RuntimeException("Error in Exception:"+e);
		}
	}
	
	public Subjects getSubject(String code) {
		return subjectsRepo.findByCode(code);
	}
	
	public int getDetailId(int id) {
		return subjectsRepoDetail.findById(id).get().getDetailId();
	}
	
	public void updateCurrentQuestion(int id, LocalDate testDate) {
		SubjectsDetailsByDatePk pk = new SubjectsDetailsByDatePk();
		pk.setId(id);
		pk.setTest_date(Date.valueOf(testDate));
		SubjectsDetailsByDate x = subjectsByDate.findById(pk).get();
		x.setCompletedDate(Timestamp.valueOf(LocalDateTime.now()));
		subjectsByDate.save(x);
	}
	
	public long getTotalRows(LocalDate testDate) {
		return subjectsByDate.dataExistsForTradeDate(Date.valueOf(testDate));
	}
	
	public List<TestsGroupedData> listOfDates() {
		return subjectsByDate.listOfDates();
	}
}