package com.uf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uf.dao.BuycarProductInfoDao;
import com.uf.dao.CustomerDao;
import com.uf.entity.BuycarProductInfo;
import com.uf.entity.Customer;
import com.uf.service.CustomerActionService;
@Service("customerActionService")
public class CustomerActionServiceImpl implements CustomerActionService{
  @Autowired
  private CustomerDao customerDao;
  @Autowired
  private BuycarProductInfoDao buycarDao;
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
  
}
