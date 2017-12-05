package com.uf.store.dao.mysql.po;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class ShopCar {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@OneToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	@OneToMany(mappedBy="shopCar")
	private List<ShopCarItem>  items;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public List<ShopCarItem> getItems() {
		return items;
	}
	public void setItems(List<ShopCarItem> items) {
		this.items = items;
	}
}
