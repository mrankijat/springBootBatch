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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

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

	@Bean(name = "fReader1")
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

	@Bean(name = "jbiw1")
	JdbcBatchItemWriter<Employee> createJBIWriter() {
		// create JdbcBatchItemWriter
		JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<Employee>() {
			{
				setDataSource(ds);
				setSql("INSERT INTO BATCH_EMPLOYEEINFO VALUES(:eId,:eName,:eAddrs,:salary,:grossSalary,:netSalary)");
			    setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
			}};
		return writer;
	}

	@Bean(name = "step11")
	Step createStep1() {
		return stepFactory.get("step11").<Employee, Employee>chunk(5).reader(createItemReader())
				.writer(createJBIWriter())
				.processor(processor)
				.build();
	}

	@Bean(name = "job11")
	Job createJob11() {
		return jobFactory.get("job11").listener(listener).incrementer(new RunIdIncrementer()).start(createStep1())
				.build();
	}
}
