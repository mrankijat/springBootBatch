package com.nt.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.nt.listener.JobMoniteringListener;
import com.nt.model.Employee;
import com.nt.processor.EmployeeInfoItemProcessor;

@Configuration
@EnableBatchProcessing
public class EmployeeJobConfiguration {

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

	@Bean(name = "fReader")
	FlatFileItemReader<Employee> createItemReader() {
		// create Reader Object
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
		reader.setResource(new ClassPathResource("Employee_csv.csv"));
		// create LineMapper obj (To get each line from csv file)
		DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<Employee>();
		// create LineTokenizer (To get tokens from lines)
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(",");
		tokenizer.setNames("eId", "eName", "Addrs", "salary");
		// create FieldSetMapper (To set the tokens to Model class object properties)
		BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<Employee>();
		fieldSetMapper.setTargetType(Employee.class);

		// set Tokenizer , fieldSetMapper object to LineMapper
		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(tokenizer);

		// set LineMapper to Reader object
		reader.setLineMapper(lineMapper);

		return reader;
	}

	@Bean(name = "jbiw")
	JdbcBatchItemWriter<Employee> createJBIWriter() {
		// create JdbcBatchItemWriter
		JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<Employee>();
		// set datasource
		writer.setDataSource(ds);
		writer.setSql("INSERT INTO BATCH_EMPLOYEEINFO VALUES(:eId,:eName,:eAddrs,:salary,:grossSalary,:netSalary)");
		// create BeanPropertyItemSqlParameterSourceProvider object
		BeanPropertyItemSqlParameterSourceProvider<Employee> sourceProvider = new BeanPropertyItemSqlParameterSourceProvider<Employee>();
		// set SourceProvider to writer object
		writer.setItemSqlParameterSourceProvider(sourceProvider);
		return writer;
	}

	@Bean(name = "step1")
	Step createStep1() {
		return stepFactory.get("step1").<Employee, Employee>chunk(5).reader(createItemReader())
				.writer(createJBIWriter())
				.processor(processor)
				.build();
	}

	@Bean(name = "job1")
	Job createJob1() {
		return jobFactory.get("job1").listener(listener).incrementer(new RunIdIncrementer()).start(createStep1())
				.build();
	}
}
