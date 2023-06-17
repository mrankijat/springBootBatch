package com.nt.report;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportGenerator {

	//@Scheduled(initialDelay = 2000,fixedDelay = 3000)
//@Scheduled(fixedRate = 4000)
	//@Scheduled(fixedRate = 10000)
	@Scheduled(initialDelay = 1000,fixedRate = 5000)
	public void generateSalesReport() {
		System.out.println("Thread Name  :: "+Thread.currentThread().getName());
		System.out.println("Report Generation Start :: "+new Date());
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		  e.printStackTrace();
		}
		System.out.println("Report Generation End :"+new Date());
	}
	@Scheduled(fixedRate = 3000)
	public void generateSalesReport1() {
		System.out.println("Thread Name1  :: "+Thread.currentThread().getName());
		System.out.println("Report1 Generation Start :: "+new Date());
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		  e.printStackTrace();
		}
		System.out.println("Report1 Generation End :"+new Date());
	}

}