package com.uf.service;

import java.util.List;

import com.uf.entity.BuycarProductInfo;
import com.uf.entity.Customer;

public interface CustomerActionService {
  public Customer  findCustomerByUserName(String userName);
  public List<BuycarProductInfo>  findCustomerBuycarProductInfo(Integer customerId);
  public BuycarProductInfo findCustomerBuycarProductInfoByProductId(Integer productId,Integer customerId);
  public void saveProductToBuyCar(BuycarProductInfo buyInfo);
  public void removeProductFromBuycar(Integer customerId,Integer buycarProductInfoId);
}
