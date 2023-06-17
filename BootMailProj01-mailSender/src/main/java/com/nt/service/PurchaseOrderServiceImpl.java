package com.nt.service;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nt.model.EmailContent;

import jakarta.mail.internet.MimeMessage;

@Service
public class PurchaseOrderServiceImpl implements IPurchaseOrderService{
	@Autowired
  private  JavaMailSender sender;
	@Autowired
	private EmailContent content;
	@Value("${spring.mail.username}")
	private String fromEmail;
	@Override
	public String purchaseOrder(String[] items, double[] prices, String[] emailRecievers) throws Exception {
	//calculate the bill amt
		double billAmt=0.0;
		for (double price : prices) {
			billAmt = billAmt+price;
		}
		String msg=Arrays.toString(items)+" with prices "+Arrays.toString(prices)+" are purchased with BillAmount "+billAmt;
	//trigger email
		String status=sendMail(msg,emailRecievers);
		
		return msg+" ----->"+status;
	}
	
	private String sendMail(String msg,String[] emailRecievers)throws Exception {
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message,true);
		helper.setFrom(fromEmail);
		helper.setCc(emailRecievers);
		helper.setSubject(content.getSubject());
		helper.setSentDate(new Date());
		helper.setText(msg);
		helper.addAttachment(content.getAttachment(), new ClassPathResource(content.getAttachment()));
		sender.send(message);
		return "mail sent successfully";
	}
}
