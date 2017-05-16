package com.uf.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.uf.entity.Customer;
import com.uf.entity.Product;

public class GsonExcludeStrategy implements ExclusionStrategy {

  @Override
  public boolean shouldSkipField(FieldAttributes f) {
    return ((f.getDeclaringClass()==Customer.class && f.getName().equals("password"))||
            (f.getDeclaringClass()==Product.class && f.getName().equals("buyPrice"))||
            (f.getDeclaringClass()==Product.class && f.getName().equals("searchKeywords")));
  }

  @Override
  public boolean shouldSkipClass(Class<?> clazz) {
    // TODO Auto-generated method stub
    return false;
  }
}
