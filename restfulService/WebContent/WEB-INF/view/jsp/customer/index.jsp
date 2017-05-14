<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../base.jsp"%>
<div class="row">
	<div class="col-md-1">
		
	</div>
	<div class="col-md-10">
		<div class="row">
			<div class="col-md-4">
			<div class="input-group input-group-lg">
				<input type="text" class="form-control" placeholder="Search for..." id="keyword" value="${keyword}"> <span class="input-group-btn">
					<button class="btn btn-default" type="button" onclick="queryProductByKeyword()">Go!</button>
				</span>
			</div>
			</div>
			<div class="col-md-4">
			
			</div>
			<div class="col-md-4" id="loginPart">
				
					<div class="row">
						<c:if test="${empty customer}">
							<div class="col-md-4">
								<input type="text" class="form-control" placeholder="用户名" id="userName"> 
							</div>
							<div class="col-md-4">
								<input type="password" class="form-control" placeholder="密码" id="password"> 
							</div>
							<div class="col-md-4">
								<button class="btn btn-default" type="button" onclick="login()">登录</button>		
							</div>	
						</c:if>
						<c:if test="${!empty customer}">
							<span>欢迎您，${customer.userName} </span>
						</c:if>
					</div>
			</div>
		</div>
		<br>
		<div class="row" id="products">
			
			
			
		</div>
	</div>
	<div class="col-md-1" id="buycarPart">
		<div class="row">
			购物车
		</div>
		
	</div>
</div>

<div id="buyDetail" class="modal fade" style="display: none;">
	    <div class="modal-dialog">
      		  <div class="modal-content" id="userEditContent">
      		  		<div class="modal-header">
    						<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
    						<h4 class="modal-title" id="myModalLabel">加入购物车</h4>
					</div>
					<div class="modal-body">
							<input type="hidden" id="buyProductId" >
							<div class="form-group row">
							    <label for="buyProductAccount" class="col-sm-4 control-label" id="buyProductName" style="text-align: center;padding-top: 5px;"></label>
							    <div class="col-sm-2">
							      <input type="text" class="form-control" id="buyProductAccount" placeholder="数量" >
							    </div>
 							 </div>
					</div>
					<div class="modal-footer">
					    <button type="button" class="btn btn-primary" onclick="addToBuycar()">确定</button>
					    <button type="button" class="btn btn-primary" data-dismiss="modal" id="closeBtn">关闭</button>
					</div>
      		  </div>
   		 </div>
</div>
<script type="text/javascript">
	
	
	var currentPageIndex=1;
	var totalPage=-1;
	var keyword="";
	function queryProductByKeyword(){
		var  queryKeyword=$("#keyword").val();
		$("#products").children().remove();
		queryProduct(queryKeyword,1);
	}
	
	
	function queryProduct(queryKeyword,pageIndex){
		keyword=queryKeyword;
		$.ajax({
		    type: "POST",
		    url: "customer/queryProducts",
		    data:{keyword:queryKeyword,pageIndex:pageIndex},
		    dataType: "json",
		    success: 
		    	function(data,status)
		    	{   
		    		if(data!=null){
		    			totalPage=data.totalPage;
		    			currentPageIndex=data.pageIndex
			    		var products=data.pageData;
			    		for(var i=0;i<products.length;i++){
			    			var pro=products[i];
			    			$("#products").append('<div class="col-md-3"><div class="thumbnail"><a href="customer/productDetail?id='+pro.id+'" target="_blank"><img src="'+pro.imgsPath[0]+'" alt="..."></a><div class="caption"><h3></h3><p>'+pro.name+'</p><p>￥'+pro.sellPrice+'</p><p>'+
									'<a onclick=buyProduct('+pro.id+',"'+pro.name+'") class="btn btn-primary" role="button">购买</a> '+
									'</p></div></div></div>');
			    		}	
		    		}
		    	},
		    error: 
		    	function(jqXHR, textStatus, errorThrown ){
		    	    
		    	}
			}
		);
	}
	queryProductByKeyword();
	

	function buyProduct(productId,productName){
		$("#buyProductId").val(productId);
		$("#buyProductName").text(productName);
		$("#buyProductAccount").val(1);
	    $("#buyDetail").modal();
	}
	function addToBuycar(){
		var id=$("#buyProductId").val();
		var count=$("#buyProductAccount").val();
		$.ajax({
		    type: "POST",
		    url: "customer/auth/addProductToBuycar",
		    data:{id:id,count:count},
		    dataType: "json",
		    success: 
		    	function(data,status)
		    	{   
		    		if(data.result=="ok"){
		    			reloadBuycarPart();
		    			$("#buyDetail").modal("hide");
		    		}else{
		    			modal({
		    				type: 'alert',
		    				title: '提示',
		    				text: data.mes
		    			});
		    		}
		    	},
		    error: 
		    	function(jqXHR, textStatus, errorThrown ){
		    	    
		    	}
			}
		);
	}
	function reloadBuycarPart(){
		$.ajax({
		    type: "POST",
		    url: "customer/queryBuycarProductInfo",
		    dataType: "json",
		    success: 
		    	function(data,status)
		    	{   
		    		$("#buycarPart").children().remove();
		    	 	if(data!=null&&data.length>0){
		    	 		for(var i=0;i<data.length;i++){
		    	 			var id=data[i].id;
		    	 			var proId=data[i].product.id;
		    	 			var proName=data[i].product.name;
		    	 			var count=data[i].count;
		    	 			var imgPath=data[i].product.imgsPath[0];
		    	 			var totalPrice=count*data[i].product.sellPrice;
		    	 			$("#buycarPart").append('<div class="row"><div class="thumbnail"><a href="productDetail?id='+proId+'" target="_blank">'+
		    	 					'<img src="'+imgPath+'" ></a><div class="caption"><p>'+proName+'  X '+count+' = ￥'+totalPrice+'</p><p><a  class="btn btn-primary" onclick="removeFromBuycar('+id+')" role="button">移出购物车</a> </p></div></div></div>');
		    	 		}
		    	 	}
		    	},
		    error: 
		    	function(jqXHR, textStatus, errorThrown ){
		    	    
		    	}
			}
		);
	}
	
	function login(){
		var userName=$("#userName").val();
		var password=$("#password").val();
		$.ajax({
		    type: "POST",
		    url: "customer/login",
		    dataType: "json",
		    data:{userName:userName,password:password},
		    success: 
		    	function(data,status)
		    	{   
			    	if(data.result=="ok"){
		    	 		$("#loginPart").children().remove();
		    	 		$("#loginPart").html("<span>欢迎您， "+userName+"</span>");
		    	 		reloadBuycarPart();
		    	 	}else{
		    	 		modal({
		    				type: 'alert',
		    				title: '提示',
		    				text: data.mes
		    			});	
		    	 	}
		    	},
		    error: 
		    	function(jqXHR, textStatus, errorThrown ){
		    	    
		    	}
			}
		);
	}
	function removeFromBuycar(id){
		$.ajax({
		    type: "POST",
		    url: "customer/auth/removeProductFromBuycar",
		    dataType: "json",
		    data:{id:id},
		    success: 
		    	function(data,status)
		    	{   
			    	if(data.result=="ok"){
		    	 		reloadBuycarPart();
		    	 	}else{
		    	 		modal({
		    				type: 'alert',
		    				title: '提示',
		    				text: data.mes
		    			});	
		    	 	}
		    	},
		    error: 
		    	function(jqXHR, textStatus, errorThrown ){
		    	    
		    	}
			}
		);
	}
	
	$(window).scroll(function(){
		var scrollTop = $(this).scrollTop();
		var scrollHeight = $(document).height();
		var windowHeight = $(this).height();
		if(scrollTop + windowHeight >= scrollHeight){
			if(totalPage>currentPageIndex){
				queryProduct(keyword,currentPageIndex+1);	
			}
		}
	});
	reloadBuycarPart();
</script>


<%@ include file="../bottom.jsp"%>
