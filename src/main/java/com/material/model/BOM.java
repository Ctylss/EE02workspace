package com.material.model;

import java.io.Serializable;

import java.math.BigDecimal;

public class BOM implements Serializable {

	private int bomId;
	private int productId;
	private int materialId;
	private String materialName;
	private BigDecimal quantityPerUnit; // 單純紀錄物料比例，這裡double即可
	private String productName;
	private String category;
	
	public BOM() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
	public BOM(int bomId, int productId, int materialId, BigDecimal quantityPerUnit) {
		super();
		this.bomId = bomId;
		this.productId = productId;
		this.materialId = materialId;
		this.quantityPerUnit = quantityPerUnit;
	}



	public BOM(int productId, int materialId, BigDecimal quantityPerUnit) {
		super();
		this.productId = productId;
		this.materialId = materialId;
		this.quantityPerUnit = quantityPerUnit;
	}


	public BOM(Integer productId, String productName, String category) {
	    this.productId = productId;
	    this.productName = productName;
	    this.category = category;
	}



	public BOM(int materialId, String materialName, BigDecimal quantityPerUnit) {
		super();
		this.materialId = materialId;
		this.materialName = materialName;
		this.quantityPerUnit = quantityPerUnit;
	}


	public int getBomId() {
		return bomId;
	}


	public void setBomId(int bomId) {
		this.bomId = bomId;
	}


	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}


	public int getMaterialId() {
		return materialId;
	}


	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}


	public BigDecimal getQuantityPerUnit() {
		return quantityPerUnit;
	}


	public void setQuantityPerUnit(BigDecimal quantityPerUnit) {
		this.quantityPerUnit = quantityPerUnit;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getMaterialName() {
		return materialName;
	}


	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	
}
