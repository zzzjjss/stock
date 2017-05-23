package com.uf.entity;

import java.util.List;

public class Order {
private Long id;
private String phoneNumber;
private String realName;
private String province;
private String city;
private String county;
private String arear;
private String address;
private float totalAmount;
private List<OrderItem> items;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getPhoneNumber() {
	return phoneNumber;
}
public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
}
public String getRealName() {
	return realName;
}
public void setRealName(String realName) {
	this.realName = realName;
}
public String getProvince() {
	return province;
}
public void setProvince(String province) {
	this.province = province;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getCounty() {
	return county;
}
public void setCounty(String county) {
	this.county = county;
}
public String getArear() {
	return arear;
}
public void setArear(String arear) {
	this.arear = arear;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public float getTotalAmount() {
	return totalAmount;
}
public void setTotalAmount(float totalAmount) {
	this.totalAmount = totalAmount;
}
public List<OrderItem> getItems() {
	return items;
}
public void setItems(List<OrderItem> items) {
	this.items = items;
}

}
