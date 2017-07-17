package com.uf.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uf.store.service.CustomerActionService;
import com.uf.store.service.dao.BuycarProductInfoDao;
import com.uf.store.service.dao.CustomerDao;
import com.uf.store.service.dao.OrderDao;
import com.uf.store.service.entity.BuycarProductInfo;
import com.uf.store.service.entity.Customer;
import com.uf.store.service.entity.Order;
@Service("customerActionService")
public class CustomerActionServiceImpl implements CustomerActionService{
  @Autowired
  private CustomerDao customerDao;
  @Autowired
  private BuycarProductInfoDao buycarDao;
  @Autowired
  private OrderDao orderDao;
  public Customer  findCustomerByUserName(String userName){
    List<Customer> cus=customerDao.findByHql("select c from  Customer c  where c.userName=?", userName);
    if(cus!=null&&cus.size()>0){
      return cus.get(0);
    }else{
      return null;
    }
  }
  public List<BuycarProductInfo>  findCustomerBuycarProductInfo(Integer customerId){
    return  buycarDao.findByHql("select info from BuycarProductInfo  info where info.customer.id=?", customerId);
  }
  public void saveProductToBuyCar(BuycarProductInfo buyInfo){
    buycarDao.saveOrUpdate(buyInfo);
  }
  public BuycarProductInfo findCustomerBuycarProductInfoByProductId(Integer productId,Integer customerId){
    List<BuycarProductInfo> results=buycarDao.findByHql("select info from BuycarProductInfo  info where info.customer.id=? and info.product.id=?", customerId,productId);
    if(results!=null&&results.size()>0){
      return results.get(0);
    }
    return null;
  }
  public void removeProductFromBuycar(Integer customerId,Integer buycarProductInfoId){
    buycarDao.executeUpdateHql("delete from BuycarProductInfo pi where pi.customer.id=? and pi.id=?", customerId,buycarProductInfoId);
  }
@Override
public Customer findCustomerByWechatId(String wechatId) {
	List<Customer> cus=customerDao.findByHql("select c from  Customer c  where c.wechatId=?", wechatId);
    if(cus!=null&&cus.size()>0){
      return cus.get(0);
    }else{
      return null;
    }
}
@Override
public void saveOrUpdateCustomer(Customer customer) {
	customerDao.saveOrUpdate(customer);
}
@Override
public List<Order> listOrders(Integer customerId) {
	return (List<Order>)orderDao.findByHql("select o from Order o where o.customer.id", customerId);
}
@Override
public Order findOrderById(Integer orderId) {
	return orderDao.findById(Order.class, orderId);
}
@Override
public void updateOrderStatus(Integer orderId, String newStatus) {
	orderDao.executeUpdateHql("update Order o set o.status=? where o.id=?", newStatus,orderId);
}
@Override
public void deleteOrder(Integer orderId, Integer customerId) {
	orderDao.executeUpdateHql("delete from Order o where o.customer.id=? and o.id=?", customerId,orderId);

}
@Override
public void generateOrder(Order order) {
	orderDao.insert(order);

}

}
