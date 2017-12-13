package com.uf.store.restful.action.customer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uf.store.dao.mysql.po.Address;
import com.uf.store.dao.mysql.po.Customer;
import com.uf.store.dao.mysql.po.Order;
import com.uf.store.dao.mysql.po.OrderItem;
import com.uf.store.dao.mysql.po.OrderStatus;
import com.uf.store.dao.mysql.po.Product;
import com.uf.store.dao.mysql.po.ShopCarItem;
import com.uf.store.restful.action.customer.dto.AddProductToShopCarRequest;
import com.uf.store.restful.action.customer.dto.AddProductToShopCarResponse;
import com.uf.store.restful.action.customer.dto.AddressInfo;
import com.uf.store.restful.action.customer.dto.GenerateOrderRequest;
import com.uf.store.restful.action.customer.dto.GenerateOrderRequestItem;
import com.uf.store.restful.action.customer.dto.GenerateOrderResponse;
import com.uf.store.restful.action.customer.dto.GetShopcarItemInfoResponse;
import com.uf.store.restful.action.customer.dto.GotoGenerateOrderRequest;
import com.uf.store.restful.action.customer.dto.GotoGenerateOrderResponse;
import com.uf.store.restful.action.customer.dto.ListOrderResponse;
import com.uf.store.restful.action.customer.dto.ListShopcarItemsResponse;
import com.uf.store.restful.action.customer.dto.OrderInfo;
import com.uf.store.restful.action.customer.dto.ShopcarItemInfo;
import com.uf.store.restful.dto.RestfulResponse;
import com.uf.store.restful.dto.RestfulResponse.ResultCode;
import com.uf.store.service.ShoppingService;
import com.uf.store.service.cache.CacheService;

@RestController
@RequestMapping("customer")
public class ShoppingAction {
	private Logger logger=LoggerFactory.getLogger(ShoppingAction.class);
	@Autowired
	private CacheService cache;
	@Autowired
	private ShoppingService shoppingService;
	@Value("${webServer.imageBaseUrl}") 
	private String imageBaseUrl;

	@RequestMapping(value = "saveProductToShopCar", method = RequestMethod.POST)
	public AddProductToShopCarResponse saveProductToShopCar(@RequestBody AddProductToShopCarRequest request,@RequestHeader(value="Authorization") String token) {
		AddProductToShopCarResponse response=new AddProductToShopCarResponse();
		try {
			Object customer=cache.getCachedObject(token);
			ShopCarItem item=shoppingService.saveProductToShopCar(request.getProductId(),request.getAmount(), (Customer)customer);
			response.setShopcarItemId(item.getId());
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	@RequestMapping(value = "deleteItemFromShopcar", method = RequestMethod.GET)
	public RestfulResponse deleteItemFromShopcar(@RequestParam(value="itemId") Long shopCarItemId,@RequestHeader(value="Authorization") String token) {
		RestfulResponse response=new RestfulResponse();
		try {
			//must  verify the shopcar item is owned by  the  customer
			Object customer=cache.getCachedObject(token);
			shoppingService.removeShopcarItem(shopCarItemId, (Customer)customer);
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	@RequestMapping(value = "getShopcarItemInfo", method = RequestMethod.GET)
	public GetShopcarItemInfoResponse getShopcarItemInfo(@RequestParam(value="id")Long itemId,@RequestHeader(value="Authorization") String token) {
		GetShopcarItemInfoResponse  response=new GetShopcarItemInfoResponse();
		try {
			if (itemId!=null) {
				ShopCarItem item=shoppingService.findShopCarItemById(itemId);
				if (item!=null) {
					ShopcarItemInfo info=new ShopcarItemInfo();
					info.setAmount(item.getAmount());
					info.setDescription(item.getProduct().getDescription());
					info.setProductId(item.getProduct().getId());
					info.setId(item.getId());
					info.setProductSnapshotImgUrl(imageBaseUrl+"/"+item.getProduct().getId()+"/snapshot.jpg");
					info.setSellPrice(item.getProduct().getSellPrice());
					response.setItemInfo(info);
				}
			}
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "listShopcarItems", method = RequestMethod.GET)
	public ListShopcarItemsResponse listShopcarItems(@RequestHeader(value="Authorization") String token) {
		ListShopcarItemsResponse  response=new ListShopcarItemsResponse();
		try {
			//find  Customer from cache, and return the customer's shopcar
			Customer customer=(Customer)cache.getCachedObject(token);
			List<ShopCarItem> items=shoppingService.findCustomerShopcarItems(customer);
			if (items!=null) {
				items.forEach(i->{
					ShopcarItemInfo info=new ShopcarItemInfo();
					info.setAmount(i.getAmount());
					info.setDescription(i.getProduct().getDescription());
					info.setProductId(i.getProduct().getId());
					info.setProductSnapshotImgUrl(imageBaseUrl+"/"+i.getProduct().getId()+"/snapshot.jpg");
					info.setSellPrice(i.getProduct().getSellPrice());
					info.setId(i.getId());
					response.getItems().add(info);
				});
			}
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	@RequestMapping(value = "gotoGenerateOrder", method = RequestMethod.POST)
	public GotoGenerateOrderResponse gotoGenerateOrder(@RequestBody GotoGenerateOrderRequest request,@RequestHeader(value="Authorization") String token) {
		GotoGenerateOrderResponse   response=new GotoGenerateOrderResponse();
		try {
			//generate the order total infor   by  calculating  the  price  in some rule 
			List<GenerateOrderRequestItem> requestItems=request.getOrderItems();
			if (requestItems!=null&&requestItems.size()>0) {
				requestItems.forEach(item->{
					Product product=shoppingService.findProductById(item.getProductId());
					if (product!=null) {
						ShopcarItemInfo info=new ShopcarItemInfo();
						info.setAmount(item.getAmount());
						info.setDescription(product.getDescription());
						info.setProductId(item.getProductId());
						info.setProductSnapshotImgUrl(imageBaseUrl+"/"+product.getId()+"/snapshot.jpg");
						info.setSellPrice(product.getSellPrice());
						response.getOrderPreItem().add(info);
					}
				});
			}
			Double totalMoney=response.getOrderPreItem().stream().mapToDouble(item->item.getSellPrice().doubleValue()*item.getAmount()).sum();
			response.setTotalMoney(totalMoney.floatValue());
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	

	@RequestMapping(value = "generateOrder", method = RequestMethod.POST)
	public GenerateOrderResponse generateOrder(@RequestBody GenerateOrderRequest request,@RequestHeader(value="Authorization") String token) {
		GenerateOrderResponse response=new GenerateOrderResponse();
		try {
			//generate the order total infor   by  calculating  the  price  in some rule 
			Customer customer=(Customer)cache.getCachedObject(token);
			Order order=shoppingService.generateOrder(customer,request.getOrderItems(),request.getAddressId());
			response.setOrderId(order.getId());
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}	

	@RequestMapping(value = "listOrdersByStatus", method = RequestMethod.GET)
	public ListOrderResponse  listOrdersByStatus(@RequestParam(value="status") String status,@RequestHeader(value="Authorization") String token) {
		ListOrderResponse response=new ListOrderResponse();
		try {
			Customer customer=(Customer)cache.getCachedObject(token);
			List<Order> orders=shoppingService.listCustomerOrdersByStatus(customer,OrderStatus.valueOf(status));
			if (orders!=null) {
				orders.stream().forEach(o->{
					OrderInfo orderInfo=new OrderInfo();
					orderInfo.setId(o.getId());
					orderInfo.setAddress(swap(o.getAddress()));
					orderInfo.setStatus(status);
					orderInfo.setTotalMoney(o.getTotalMoney());
					List<OrderItem> items=o.getOrderItem();
					items.forEach(oi->{
						ShopcarItemInfo info=new ShopcarItemInfo();
						info.setAmount(oi.getAmount());
						Product product=oi.getProduct();
						info.setDescription(product.getDescription());
						info.setProductId(product.getId());
						info.setProductSnapshotImgUrl(imageBaseUrl+"/"+product.getId()+"/snapshot.jpg");
						info.setSellPrice(product.getSellPrice());
						orderInfo.getOrderItemsInfo().add(info);
					});
					response.getOrders().add(orderInfo);
				});
			}
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;	
	}
	@RequestMapping(value = "cancelOrder", method = RequestMethod.GET)
	public  RestfulResponse cancelOrder(@RequestParam(value="orderId")Long orderId,@RequestHeader(value="Authorization") String token) {
		RestfulResponse response=new RestfulResponse();
		try {
			Customer customer=(Customer)cache.getCachedObject(token);
			Order order=shoppingService.getOrderById(orderId);
			if (order!=null) {
				if(order.getStatus()!=OrderStatus.NOPAY) {
					response.setMes("order status is wrong");
					response.setResultCode(ResultCode.FAIL);
					return response;
				}
				shoppingService.changeOrderStatus(OrderStatus.CANCELED, orderId, customer.getId());
			}
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;	
	}	
	private AddressInfo  swap(Address address) {
		AddressInfo info=new AddressInfo();
		info.setAddressDetail(address.getAddressDetail());
		info.setArea(address.getArea());
		info.setCity(address.getCity());
		info.setDefault(address.isDefault());
		info.setId(address.getId());
		info.setName(address.getName());
		info.setPhone(address.getPhone());
		info.setProvince(address.getProvince());
		return info;
	}
	
}
