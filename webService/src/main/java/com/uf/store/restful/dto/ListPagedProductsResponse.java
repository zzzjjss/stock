package com.uf.store.restful.dto;

import java.util.ArrayList;
import java.util.List;

public class ListPagedProductsResponse extends RestfulResponse{
	private List<ProductSellInfo> productInfors=new ArrayList<ProductSellInfo>();
	private int pageIndex;
	private int totalPage;
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public List<ProductSellInfo> getProductInfors() {
		return productInfors;
	}

	public void setProductInfors(List<ProductSellInfo> productInfors) {
		this.productInfors = productInfors;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
