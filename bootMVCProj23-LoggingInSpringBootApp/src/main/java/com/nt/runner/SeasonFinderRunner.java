package com.nt.runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nt.sbeans.SeasonFinder;

import lombok.extern.slf4j.Slf4j;
@Component 
public class SeasonFinderRunner implements CommandLineRunner {
	@Autowired
	private SeasonFinder sFinder;
	
	private static Logger logger=LoggerFactory.getLogger(SeasonFinderRunner.class);
	
	@Override
	public void run(String... args) throws Exception {
	logger.info("Runner Started");
		String seasonFind = sFinder.seasonFind();
		logger.info("Program Successfully executed");
		System.out.println(seasonFind);
		logger.info("Program is over");
		
	}

}
