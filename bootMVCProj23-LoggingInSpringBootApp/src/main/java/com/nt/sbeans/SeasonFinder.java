package com.nt.sbeans;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component("sFinder")
public class SeasonFinder {

	public static String seasonFind() {
		LocalDateTime dt=LocalDateTime.now();
		if (dt.getHour()<12) {
			return "Good Morning";
		}else if (dt.getHour()>=12 || dt.getHour()<20) {
			return "Good Afternoon";
		}else {
			return "Good Night";
		}
	}
}
