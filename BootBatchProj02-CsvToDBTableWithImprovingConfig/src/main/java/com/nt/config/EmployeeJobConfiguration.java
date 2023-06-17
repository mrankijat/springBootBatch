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
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import com.nt.listener.JobMoniteringListener;
import com.nt.model.Employee;
import com.nt.processor.EmployeeInfoItemProcessor;

@Configuration
@EnableBatchProcessing
@Primary
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
		return new FlatFileItemReaderBuilder()
				.name("fReader")
				.resource(new ClassPathResource("Employee_csv.csv"))
				.delimited()
				.names("eId","eName","eAddrs","salary")
				.targetType(Employee.class)
				.build();
	}

	@Bean(name = "jbiw")
	JdbcBatchItemWriter<Employee> createJBIWriter() {
	return new JdbcBatchItemWriterBuilder()
			.dataSource(ds)
			.sql("INSERT INTO BATCH_EMPLOYEEINFO VALUES(:eId,:eName,:eAddrs,:salary,:grossSalary,:netSalary)")
			.beanMapped()
			.build();
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
