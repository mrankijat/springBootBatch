package com.nt.service;

public interface IPurchaseOrderService {
	public String purchaseOrder(String [] items,double [] prices,String [] emailRecievers )throws Exception;

}
