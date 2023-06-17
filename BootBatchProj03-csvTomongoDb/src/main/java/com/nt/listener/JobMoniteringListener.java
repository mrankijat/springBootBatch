package com.nt.listener;

import java.util.Date;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobMoniteringListener implements JobExecutionListener {

	private long startTime,endTime;
	
	public JobMoniteringListener() {
		//0-param constructor
	}
	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Job is going to be started");
		long startTime = System.currentTimeMillis();
		System.out.println("Job is started");
	}
	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("Job is going to be closed");
		endTime=System.currentTimeMillis();
		System.out.println("Job is closed");
		System.out.println("Job completed at::"+new Date());
		System.out.println("Job execution time::"+(endTime-startTime));
		System.out.println("Job execution status::"+jobExecution.getStatus());
	}
}
