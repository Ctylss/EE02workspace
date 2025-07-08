package com.mes.bean;

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
@Entity @Table(name = "PurchaseOrderItem")
public class OrderItem {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="order_item_id")
	private int orderItemId;

	@Column(name ="order_id")
	private int orderId;

	@Column(name ="material_id")
	private int materialId;

	@Column(name ="quantity")
	private int quantity;

	@Column(name ="unit_price")
	private double unitPrice;

	@Column(name ="delivery_status")
	private String deliveryStatus;
	
	@Transient
	private String materialName;

	@ManyToOne
    @JoinColumn(name = "material_id", insertable = false, updatable = false)
    private Material material;

	@Transient
    public String getMaterialName() {
        return material != null ? material.getMaterialName() : "無資料";
    }

}