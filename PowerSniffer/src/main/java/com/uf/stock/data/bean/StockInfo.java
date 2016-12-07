package com.uf.stock.data.bean;

public class StockInfo {
	public static final String STOCK_TYPE_SHANG_HAI = "sh";
	public static final String STOCK_TYPE_SHEN_ZHEN = "sz";
	public static final String STOCK_TYPE_CHUANG_YE = "cy";

	private Integer code;
	private String name;
	private String symbol;
	private Float totalAAmount;
	private Float peRatio;
	private boolean isInAlarmMonitor;
	private Float downPercentToLowest;

	public Float getPeRatio() {
		return peRatio;
	}

	public void setPeRatio(Float peRatio) {
		this.peRatio = peRatio;
	}

	public Float getTotalAAmount() {
		return totalAAmount;
	}

	public void setTotalAAmount(Float totalAAmount) {
		this.totalAAmount = totalAAmount;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Float getDownPercentToLowest() {
		return downPercentToLowest;
	}

	public void setDownPercentToLowest(Float downPercentToLowest) {
		this.downPercentToLowest = downPercentToLowest;
	}

	public boolean getIsInAlarmMonitor() {
		return isInAlarmMonitor;
	}

	public void setIsInAlarmMonitor(boolean isInAlarmMonitor) {
		this.isInAlarmMonitor = isInAlarmMonitor;
	}
	
}
