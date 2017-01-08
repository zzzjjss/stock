package com.uf.stock.k_analysis;

public class AnalysisResult {
	private int stockCode;
	private float avgUpSpeed;
	private float maxUpSpeed;
	private float avgDownSpeed;
	private float maxDownSpeed;
	private float maxUpRate;
	private float maxDownRate;
	private float upDownRate;
	private float downRateToLowest;

	public float getAvgUpSpeed() {
		return avgUpSpeed;
	}

	public void setAvgUpSpeed(float avgUpSpeed) {
		this.avgUpSpeed = avgUpSpeed;
	}

	public float getMaxUpSpeed() {
		return maxUpSpeed;
	}

	public void setMaxUpSpeed(float maxUpSpeed) {
		this.maxUpSpeed = maxUpSpeed;
	}

	public float getAvgDownSpeed() {
		return avgDownSpeed;
	}

	public void setAvgDownSpeed(float avgDownSpeed) {
		this.avgDownSpeed = avgDownSpeed;
	}

	public float getMaxDownSpeed() {
		return maxDownSpeed;
	}

	public void setMaxDownSpeed(float maxDownSpeed) {
		this.maxDownSpeed = maxDownSpeed;
	}

	public float getUpDownRate() {
		return upDownRate;
	}

	public void setUpDownRate(float upDownRate) {
		this.upDownRate = upDownRate;
	}

	public float calculateSidewayIndex() {
		float length = maxUpRate - maxDownRate;
		if (length == 0) {
			return 0;
		} else {
			return 1f / length;
		}
	}
	public float calculateSlowUpFastDownIndex(){
		if(avgDownSpeed==0f){
			return -1.0f;
		}
		return avgUpSpeed/avgDownSpeed;
	}
	
	public float getMaxUpRate() {
		return maxUpRate;
	}

	public void setMaxUpRate(float maxUpRate) {
		this.maxUpRate = maxUpRate;
	}

	public float getMaxDownRate() {
		return maxDownRate;
	}

	public void setMaxDownRate(float maxDownRate) {
		this.maxDownRate = maxDownRate;
	}

	public float getDownRateToLowest() {
		return downRateToLowest;
	}

	public void setDownRateToLowest(float downRateToLowest) {
		this.downRateToLowest = downRateToLowest;
	}
	
	public int getStockCode() {
		return stockCode;
	}

	public void setStockCode(int stockCode) {
		this.stockCode = stockCode;
	}

	@Override
	public String toString() {
		return "maxUpSpeed:" + maxUpSpeed + ";avgUpSpeed:" + avgUpSpeed
				+ ";maxDownSpeed:" + maxDownSpeed + ";avgDownSpeed:"
				+ avgDownSpeed + ";upDownRate:" + upDownRate + "%"
				+ ";maxUpRate:" + maxUpRate + "%;maxDownRate:" + maxDownRate
				+ "%;downRateToLowest:" + downRateToLowest+"%";
	}
}
