package com.mes.bean;

import java.math.BigDecimal;

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

@Entity @Table(name = "Material")
public class Material {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="material_id")
	private int materialId;

	@Column(name ="material_name")
	private String materialName;

	@Column(name ="unit")
	private String unit;

	@Column(name="material_description")
	private String materialDescription;

	@Column(name="location")
	private String location;

	@Column(name="stock_current")
	private BigDecimal stockCurrent;

	@Column(name="stock_reserved")
	private BigDecimal stockReserved;

	@Column(name="stock_in_shipping")
	private BigDecimal stockInShipping;

	@Column(name="safety_stock")
	private int safetyStock;

	@Column(name="reorder_level")
	private int reorderLevel;

	@Column(name="active")
	private boolean active;

}