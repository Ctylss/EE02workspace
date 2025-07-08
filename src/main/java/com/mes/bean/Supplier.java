package com.mes.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "Supplier")
public class Supplier {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "supplier_id")
	private int supplierId;

	@Column(name = "supplier_name")
	private String supplierName;

	@Column(name = "pm")
	private String pm;

	@Column(name = "supplier_phone")
	private String supplierPhone;

	@Column(name = "supplier_email")
	private String supplierEmail;

	@Column(name = "supplier_address")
	private String supplierAddress;

	@Column(name = "active")
	private boolean active;
	
}
