package com.nt.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nt.service.IPurchaseOrderService;

@Component("emailRunner")
public class EmailSenderRunner implements CommandLineRunner {

	@Autowired
	private IPurchaseOrderService service;
	
	@Override
	public void run(String... args) throws Exception {
		String result=service.purchaseOrder(new String[] {"Shirt", "Trouser","Cap"},new double[] {1200.0,1499.0,499.0},new String[] {"jonyrajawat@gmail.com"});
		System.out.println(result);
	}
}
