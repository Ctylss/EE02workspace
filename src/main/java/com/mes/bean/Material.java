package com.mes.bean;

import java.math.BigDecimal;
import java.util.List;

public class Material {
	private int materialId;
	private String materialName;
	private String unit;
	private String materialDescription;
	private String location;
	private BigDecimal stockCurrent;
	private BigDecimal stockReserved;
	private BigDecimal stockInShipping;
	private int safetyStock;
	private int reorderLevel;
	private boolean active;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public BigDecimal getStockCurrent() {
		return stockCurrent;
	}
	public void setStockCurrent(BigDecimal stockCurrent) {
		this.stockCurrent = stockCurrent;
	}
	public BigDecimal getStockReserved() {
		return stockReserved;
	}
	public void setStockReserved(BigDecimal stockReserved) {
		this.stockReserved = stockReserved;
	}
	public BigDecimal getStockInShipping() {
		return stockInShipping;
	}
	public void setStockInShipping(BigDecimal stockInShipping) {
		this.stockInShipping = stockInShipping;
	}
	public int getSafetyStock() {
		return safetyStock;
	}
	public void setSafetyStock(int safetyStock) {
		this.safetyStock = safetyStock;
	}
	public int getReorderLevel() {
		return reorderLevel;
	}
	public void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
}