package com.nt.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component("reader")
public class BookDetailReader implements ItemReader<String> {

	String[] books =new String [] {"CRJ","TIJ","HFJ","EJ","BBJ"};//Source 
	int count=0;
	public  BookDetailReader() {
		System.out.println("0-param constructor");
	}
@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		System.out.println("BookDetailReader.read()");
		if (count<books.length) {
			return books[count++];
		}
		else {
		return null;
		} 
	}	
}
