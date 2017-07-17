package com.uf.store.service.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Product {
  private Integer id;
  private String name;
  private String description;
  private String searchKeywords;
  private Float buyPrice;
  private Float sellPrice;
  private Date updateTime;
  private List<String> imgsPath=new ArrayList<String>();
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getSearchKeywords() {
    return searchKeywords;
  }

  public void setSearchKeywords(String searchKeywords) {
    this.searchKeywords = searchKeywords;
  }

  public Float getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(Float buyPrice) {
    this.buyPrice = buyPrice;
  }

  public Float getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(Float sellPrice) {
    this.sellPrice = sellPrice;
  }

  public List<String> getImgsPath() {
    return imgsPath;
  }

  public void setImgsPath(List<String> imgsPath) {
    this.imgsPath = imgsPath;
  }

}
