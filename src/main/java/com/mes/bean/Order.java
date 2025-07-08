package com.mes.bean;

import java.util.List;

public class Order {
	private int orderId;
	private int supplierId;
	private String orderDate;
	private String orderStatus;
	private double subTotal;
	private String supplierName;
	private List<OrderItem>itemList;
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public String getSupplierName() {
	    return supplierName;
	}

	public void setSupplierName(String supplierName) {
	    this.supplierName = supplierName;
	}
	public List<OrderItem> getItemList() {
	    return itemList;
	}

	public void setItemList(List<OrderItem> itemList) {
	    this.itemList = itemList;
	}
}
