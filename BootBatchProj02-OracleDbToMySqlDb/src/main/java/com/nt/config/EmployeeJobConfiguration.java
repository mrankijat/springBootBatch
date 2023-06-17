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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
	
	@Bean
	@ConfigurationProperties(prefix = "oracle.datasource")
	@Primary
	DataSource createOraDs() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.ds2")
	DataSource createMsDs() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "fReader")
	JdbcCursorItemReader<Employee> createJBIReader(DataSource ds){
		//create jdbc item reader 
	JdbcCursorItemReader<Employee> reader=new JdbcCursorItemReader<Employee>();
	reader.setDataSource(createOraDs());
	reader.setName("fReader");
	reader.setSql("SELECT EID,ENAME,EADDRS,SALARY,GROSSSALARY,NETSALARY FROM BATCH_EMPLOYEEINFO");
    reader.setRowMapper((rs,rowNow)->new Employee(rs.getInt(1)
    		                                                                                    ,rs.getString(2)
    		                                                                                    ,rs.getString(3)
    		                                                                                    ,rs.getFloat(4)
    		                                                                                    ,rs.getFloat(5)
    		                                                                                    ,rs.getFloat(6)));
    return reader;
			
	
	}

	@Bean(name = "jbiw")
	JdbcBatchItemWriter<Employee> createJBIWriter(DataSource ds) {
		// create JdbcBatchItemWriter
		JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<Employee>();
		
		// set datasource
		writer.setDataSource(createMsDs());
		writer.setSql("INSERT INTO BATCH_EMPLOYEEINFO VALUES(:eId,:eName,:eAddrs,:salary,:grossSalary,:netSalary)");
		// create BeanPropertyItemSqlParameterSourceProvider object
		BeanPropertyItemSqlParameterSourceProvider<Employee> sourceProvider = new BeanPropertyItemSqlParameterSourceProvider<Employee>();
		// set SourceProvider to writer object
		writer.setItemSqlParameterSourceProvider(sourceProvider);
		return writer;
	}

	@Bean(name = "step1")
	Step createStep1() {
		return stepFactory.get("step1").<Employee, Employee>chunk(5)
				.reader(createJBIReader(createOraDs()))
				.writer(createJBIWriter(createMsDs()))
				.processor(processor)
				.build();
	}

	@Bean(name = "job1")
	Job createJob1() {
		return jobFactory.get("job1").listener(listener).incrementer(new RunIdIncrementer()).start(createStep1())
				.build();
	}
}
