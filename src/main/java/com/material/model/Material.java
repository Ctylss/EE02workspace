package com.material.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Material implements Serializable {
    private int materialId;
    private String materialName;
    private String unit;
    private String materialDescription;
    private BigDecimal stockAvailable;
    private BigDecimal stockCurrent;
    private BigDecimal stockInShipping;
    private BigDecimal stockReserved;
    private String location;
    
	public Material() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	// dao 需要建構子再回來加


	public Material(Integer materialId2, String materialName2, String location2, BigDecimal stockAvailable2,
			BigDecimal stockCurrent2, String unit2, String materialDescription2) {
		// TODO Auto-generated constructor stub
	}

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMaterialDescription() {
		return materialDescription;
	}

	public void setMaterialDescription(String materialDescription) {
		this.materialDescription = materialDescription;
	}

	public BigDecimal getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(BigDecimal stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public BigDecimal getStockCurrent() {
		return stockCurrent;
	}

	public void setStockCurrent(BigDecimal stockCurrent) {
		this.stockCurrent = stockCurrent;
	}

	public BigDecimal getStockInShipping() {
		return stockInShipping;
	}

	public void setStockInShipping(BigDecimal stockInShipping) {
		this.stockInShipping = stockInShipping;
	}

	public BigDecimal getStockReserved() {
		return stockReserved;
	}

	public void setStockReserved(BigDecimal stockReserved) {
		this.stockReserved = stockReserved;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
    
	
    
}
