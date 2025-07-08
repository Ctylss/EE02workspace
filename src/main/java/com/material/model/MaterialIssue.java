package com.material.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class MaterialIssue implements Serializable {
	
    private int issueId;
    private int workOrderId;
    private int materialId;
    private BigDecimal quantity;
    private Date issueDate;
    private String description;
    
	public MaterialIssue() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getIssueId() {
		return issueId;
	}

	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}

	public int getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(int workOrderId) {
		this.workOrderId = workOrderId;
	}

	public int getMaterialId() {
		return materialId;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
    
}
