package com.nt.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nt.listener.JobMoniteringListener;
import com.nt.processor.BookDetailProcessor;
import com.nt.reader.BookDetailReader;
import com.nt.writer.BookDetailWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	private JobBuilderFactory jobFactory;
    @Autowired
    private StepBuilderFactory stepFactory;
    @Autowired
    private JobMoniteringListener listener;
	@Autowired
    private BookDetailReader reader;
	@Autowired
	private BookDetailProcessor processor;
	@Autowired
	private BookDetailWriter writer;
	
	@Bean(name = "step1")
	 Step createStep1() {
		System.out.println("BatchConfig.createStep()");
		return stepFactory.get("step1")
				.<String,String>chunk(10)
				.reader(reader)
	            .processor(processor)
	            .writer(writer)
	            .build();
	}
	@Bean(name="job1")
	Job createJob() {
		System.out.println("BatchConfig.createJob()");
		return jobFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.start(createStep1())
				.build();
	}
}
