package com.personnel.model;

import java.io.Serializable;
import java.sql.Date;

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
@Entity
@Table(name = "Employees")
public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "employee_id", unique = true, nullable = false) // 對應到名為 employee_id 的欄位，必須唯一且不為空
	private String employeeId;

	@Column(name = "name", nullable = false) // 對應到名為 name 的欄位，不為空
	private String name;

	@Column(name = "department")
	private String department;

	@Column(name = "position")
	private String position;

	@Column(name = "hire_date")
	private Date hireDate;

//	public Employee() {
//	}

//	public Employee(int id, String employeeId, String name, String department, String position, Date hireDate) {
//		this.id = id;
//		this.employeeId = employeeId;
//		this.name = name;
//		this.department = department;
//		this.position = position;
//		this.hireDate = hireDate;
//	}
//
	public Employee(String employeeId, String name, String department, String position, Date hireDate) {
		this.employeeId = employeeId;
		this.name = name;
		this.department = department;
		this.position = position;
		this.hireDate = hireDate;
	}
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getEmployeeId() {
//		return employeeId;
//	}
//
//	public void setEmployeeId(String employeeId) {
//		this.employeeId = employeeId;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getDepartment() {
//		return department;
//	}
//
//	public void setDepartment(String department) {
//		this.department = department;
//	}
//
//	public String getPosition() {
//		return position;
//	}
//
//	public void setPosition(String position) {
//		this.position = position;
//	}
//
//	public Date getHireDate() {
//		return hireDate;
//	}
//
//	public void setHireDate(Date hireDate) {
//		this.hireDate = hireDate;
//	}

//	@Override
//	public String toString() {
//		return "Employee{" + "id=" + id + ", employeeId='" + employeeId + '\'' + ", name='" + name + '\''
//				+ ", department='" + department + '\'' + ", position='" + position + '\'' + ", hireDate=" + hireDate
//				+ '}';
//	}
//
//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (o == null || getClass() != o.getClass())
//			return false;
//		Employee employee = (Employee) o;
//		return employeeId.equals(employee.employeeId);
//	}
//
//	@Override
//	public int hashCode() {
//		return java.util.Objects.hash(employeeId);
//	}
}
