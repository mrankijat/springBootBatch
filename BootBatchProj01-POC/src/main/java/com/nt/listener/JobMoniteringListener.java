package com.nt.listener;

import java.util.Date;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component("jMListener")
public class JobMoniteringListener implements JobExecutionListener {

	private Long startTime;
	private Long endTime;

	public JobMoniteringListener() {
		System.out.println("0-param constructor");
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Job is going to be started::" + new Date());
		startTime = System.currentTimeMillis();
		System.out.println("Job Execution status::" + jobExecution.getStatus());

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("Job is going to be closed::" + new Date());
		endTime = System.currentTimeMillis();
		System.out.println("Job Execution status::" + jobExecution.getStatus());
		System.out.println("Job Execution time::" + (endTime - startTime));
		System.out.println("Job Exit status::" + jobExecution.getExitStatus());

	}
}
