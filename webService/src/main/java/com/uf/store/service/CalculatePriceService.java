package com.uf.store.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatePriceService {
	public float calculateSellPrice(float costPrice,float originalPrice,int buyAmount) {
		if(buyAmount==1)
			return originalPrice;
		float oneProductProfile=originalPrice-costPrice;
		float allProfile=oneProductProfile*buyAmount;
		float baseFree=0.05f;
		float totalFree=0.0f;
		float maxFreeRate=0.6f;
		for(int i=2;i<=buyAmount;i++) {
			float profieRate=baseFree*(i-1)>maxFreeRate?maxFreeRate:baseFree*(i-1);
			float free=profieRate*oneProductProfile;
			totalFree=totalFree+free;
		}
		float oneProductLastProfile=(allProfile-totalFree)/(float)buyAmount;
		return costPrice+oneProductLastProfile;
	}
	
	
public static void main(String[] args) {
	CalculatePriceService service=new CalculatePriceService();
	float costPrice=80.00f,originalPrice=100.00f;
	for(int i=1;i<100;i++) {
		float onePrice=service.calculateSellPrice(costPrice,originalPrice,i);
		System.out.println(i+"-->"+onePrice+"---->"+(onePrice*i-costPrice*i));
		
	}
}	
}
