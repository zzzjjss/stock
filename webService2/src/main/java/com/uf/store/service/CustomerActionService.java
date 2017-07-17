package com.uf.store.service;

import java.util.List;

import com.uf.store.service.entity.BuycarProductInfo;
import com.uf.store.service.entity.Customer;
import com.uf.store.service.entity.Order;

public interface CustomerActionService  {
  public Customer  findCustomerByUserName(String userName);
  public Customer  findCustomerByWechatId(String wechatId);
  public void saveOrUpdateCustomer(Customer customer);
  public List<BuycarProductInfo>  findCustomerBuycarProductInfo(Integer customerId);
  public BuycarProductInfo findCustomerBuycarProductInfoByProductId(Integer productId,Integer customerId);
  public void saveProductToBuyCar(BuycarProductInfo buyInfo);
  public void removeProductFromBuycar(Integer customerId,Integer buycarProductInfoId);

  public List<Order> listOrders(Integer customerId);
  public Order findOrderById(Integer orderId);
  public void updateOrderStatus(Integer orderId,String newStatus);
  public void deleteOrder(Integer orderId,Integer customerId);

  public void generateOrder(Order order);
}
