package com.nt.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component("writer")
public class BookDetailWriter implements ItemWriter<String> {

	public BookDetailWriter() {
		System.out.println("0-param constructor");
	}
	
 @Override
public void write(List<? extends String> items) throws Exception {

	 System.out.println("BookDetailWriter.write()");
	 items.forEach(System.out::println);
	
}
}
