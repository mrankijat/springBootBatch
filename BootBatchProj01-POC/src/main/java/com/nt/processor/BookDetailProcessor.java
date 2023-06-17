package com.nt.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("iTProcessor")
public class BookDetailProcessor implements ItemProcessor<String , String> {
	
	public BookDetailProcessor() {
		System.out.println("0-param constructor");
	}
@Override
public String process(String item) throws Exception {
 System.out.println("BookDetailProcessor.process()");
 String bookWithTitle=null;
 if (item.equalsIgnoreCase("CRJ")) {
	bookWithTitle=item+" by HS and PS";
}else if(item.equalsIgnoreCase("TIJ")) {
	bookWithTitle=item+" by KS";
}else if(item.equalsIgnoreCase("HFJ")) {
	bookWithTitle=item+" by KS";
}else if (item.equalsIgnoreCase("EJ")) {
	bookWithTitle=item+" by JB";
}else if(item.equalsIgnoreCase("BBJ")) {
	bookWithTitle=item+" by RNR";
}
 
	return bookWithTitle;
}//method
}//class
