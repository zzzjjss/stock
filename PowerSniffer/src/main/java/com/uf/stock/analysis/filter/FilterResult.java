package com.uf.stock.analysis.filter;

public class FilterResult<T> {
  private Boolean isPass;
  private T filterValue;
  public Boolean getIsPass() {
    return isPass;
  }
  public void setIsPass(Boolean isPass) {
    this.isPass = isPass;
  }
  public T getFilterValue() {
    return filterValue;
  }
  public void setFilterValue(T filterValue) {
    this.filterValue = filterValue;
  }
  
}
