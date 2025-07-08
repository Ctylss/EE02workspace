package com.mes.bean;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "PurchaseOrder")

public class Order {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="order_id")
	private int orderId;

	@ManyToOne
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;

	@Column(name ="order_date")
	private String orderDate;

	@Column(name ="order_status")
	private String orderStatus;

	@Column(name ="sub_total")
	private double subTotal;

	@Transient 
	private String supplierName;
	
	@Transient 
	private List<OrderItem>itemList;

	
}