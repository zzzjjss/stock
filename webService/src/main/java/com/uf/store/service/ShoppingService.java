package com.uf.store.service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uf.store.dao.mysql.AddressRepository;
import com.uf.store.dao.mysql.OrderAddressRepository;
import com.uf.store.dao.mysql.OrderItemRepository;
import com.uf.store.dao.mysql.OrderRepository;
import com.uf.store.dao.mysql.ProductRepository;
import com.uf.store.dao.mysql.ShopCarItemRepository;
import com.uf.store.dao.mysql.po.Address;
import com.uf.store.dao.mysql.po.Customer;
import com.uf.store.dao.mysql.po.Order;
import com.uf.store.dao.mysql.po.OrderAddress;
import com.uf.store.dao.mysql.po.OrderItem;
import com.uf.store.dao.mysql.po.OrderStatus;
import com.uf.store.dao.mysql.po.PaymentStatus;
import com.uf.store.dao.mysql.po.Product;
import com.uf.store.dao.mysql.po.ShopCarItem;
import com.uf.store.restful.action.customer.dto.GenerateOrderRequestItem;

@Transactional
@Service
public class ShoppingService {
	@Autowired
	private ShopCarItemRepository shopCarItemRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private OrderRepository  orderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private OrderAddressRepository orderAddressRepository;
	public ShopCarItem  findShopCarItemById(Long id) {
		return shopCarItemRepository.findOne(id);
	}
	public ShopCarItem saveProductToShopCar(Long productId, int amount, Customer customer) {
		ShopCarItem scit = shopCarItemRepository.findTopByProductAndCustomer(productId, customer.getId());
		if (scit != null) {
			scit.setAmount(scit.getAmount() + amount);
		} else {
			scit = new ShopCarItem();
			scit.setAmount(amount);
			scit.setCustomer(customer);
			Product product = new Product();
			product.setId(productId);
			scit.setProduct(product);
		}
		shopCarItemRepository.save(scit);
		return scit;
	}
	public void changeOrderStatus(OrderStatus status,Long orderId,Long customerId) {
		orderRepository.updateOrderStatus(status, orderId, customerId);
	}
	public void removeShopcarItem(Long itemId, Customer customer) {
		shopCarItemRepository.deleteCustomerShopcarItem(itemId, customer.getId());
	}
	public void removeShopcarItemByProduct(Long productId,Customer customer) {
		shopCarItemRepository.deleteCustomerShopcarItemByProduct(productId, customer.getId());
	}
	public List<ShopCarItem> findCustomerShopcarItems(Customer custoer){
		return shopCarItemRepository.findByCustomer(custoer.getId());
	}
	public Long countCustomerShopcartItem(Customer customer) {
		return shopCarItemRepository.countByCustomer(customer.getId());
	}
	
	public Product findProductById(Long productId) {
		return productRepository.findOne(productId);
	}
	
	public Address findCustomerDefaultAddress(Customer customer) {
		return addressRepository.findCustomerDefaultAddress(customer.getId());
	}
	public Order generateOrder(Customer customer ,List<GenerateOrderRequestItem> orderItems ,Long addressId)throws InvalidParameterException {
		if (orderItems!=null&&orderItems.size()>0) {
			Address address=addressRepository.findOne(addressId);
			OrderAddress orderAddress=new OrderAddress();
			orderAddress.setAddressDetail(address.getAddressDetail());
			orderAddress.setArea(address.getArea());
			orderAddress.setCity(address.getCity());
			orderAddress.setName(address.getName());
			orderAddress.setPhone(address.getPhone());
			orderAddress.setProvince(address.getProvince());
			orderAddressRepository.save(orderAddress);
			Order order=new Order();
			order.setOrderAddress(orderAddress);
			order.setGenerateTime(new Date());
			order.setPaymentStaus(PaymentStatus.NOPAY);
			order.setStatus(OrderStatus.NOPAY);
			order.setOrderNumber(UUID.randomUUID().toString());
			orderRepository.save(order);
			List<Float> itemPrices=new ArrayList<Float>();
			orderItems.forEach(item->{
				OrderItem oItem=new OrderItem();
				oItem.setOrder(order);
				oItem.setAmount(item.getAmount());
				Product product=productRepository.findOne(item.getProductId());
				if (product==null) {
					throw new InvalidParameterException("can't find product");
				}
				removeShopcarItemByProduct(product.getId(), customer);
				oItem.setProduct(product);
				Float itMoney=item.getAmount()*product.getSellPrice();
				itemPrices.add(itMoney);
				oItem.setTotalMoney(itMoney);
				orderItemRepository.save(oItem);
			});
			Double sum=itemPrices.stream().mapToDouble(p->p.doubleValue()).sum();
			order.setTotalMoney(sum.floatValue());
			order.setCustomer(customer);
			orderRepository.save(order);
			return order;
		}
		return null;
	}
	public List<Order> listCustomerOrdersByStatus(Customer customer,OrderStatus status){
		return orderRepository.findByCustomerAndStauts(customer.getId(), status);
	}
	public Order getOrderById(Long id) {
		return orderRepository.getOne(id);
	}
}
