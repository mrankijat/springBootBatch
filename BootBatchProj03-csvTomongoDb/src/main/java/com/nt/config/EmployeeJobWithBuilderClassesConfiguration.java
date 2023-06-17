package com.nt.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.nt.listener.JobMoniteringListener;
import com.nt.model.Employee;
import com.nt.processor.EmployeeInfoItemProcessor;

@Configuration
@EnableBatchProcessing
public class EmployeeJobWithBuilderClassesConfiguration {

	@Autowired
	private EmployeeInfoItemProcessor processor;
	@Autowired
	private JobBuilderFactory jobFactory;
	@Autowired
	private StepBuilderFactory stepFactory;
	@Autowired
	private JobMoniteringListener listener;
	@Autowired
	private DataSource ds;
	@Autowired
	private MongoTemplate template;

	@Bean(name = "reader")
	FlatFileItemReader<Employee> createItemReader() {
		// create Reader Object
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
		reader.setResource(new ClassPathResource("Employee_csv.csv"));
		// create LineMapper obj (To get each line from csv file)
		reader.setLineMapper(new DefaultLineMapper<Employee>() {
			{
				//set Delimeted tokenizer object
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
					setDelimiter(",");
					setNames("eId","eName","eAddrs","salary");
					}});
				//set field set mapper
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
					{
						setTargetType(Employee.class);
					}} );
			}});
		return reader;
	}

	@Bean(name = "writer")
	MongoItemWriter<Employee> createWriter(){
	     MongoItemWriter<Employee> writer =new MongoItemWriter<Employee>();
	    writer.setCollection("Employee_Info");
	    writer.setTemplate(template);
		return writer;
	}

	@Bean(name = "step1")
	Step createStep1() {
		return stepFactory.get("step1").<Employee, Employee>chunk(5).reader(createItemReader())
				.writer(createWriter())
				.processor(processor)
				.build();
	}

	@Bean(name = "job1")
	Job createJob11() {
		return jobFactory.get("job1")
				.listener(listener)
				.incrementer(new RunIdIncrementer())
				.start(createStep1())
				.build();
	}
}
