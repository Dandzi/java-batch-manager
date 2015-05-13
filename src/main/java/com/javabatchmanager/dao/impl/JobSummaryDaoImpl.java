package com.javabatchmanager.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.javabatchmanager.dao.JobSummaryDao;
import com.javabatchmanager.dtos.JobSummary;

public class JobSummaryDaoImpl implements JobSummaryDao{
	
	//@PersistenceContext 
	//private EntityManager em;

	public JobSummary create(JobSummary jobSum) {
		//em.persist(jobSum);
		
		return jobSum;
	}

	public void delete(Long Id) {
		// TODO Auto-generated method stub
		
	}

	public JobSummary update(JobSummary jobSum) {
		// TODO Auto-generated method stub
		return null;
	}

	public JobSummary getById(Long Id) {
		// TODO Auto-generated method stub
		return null;
	}
}
