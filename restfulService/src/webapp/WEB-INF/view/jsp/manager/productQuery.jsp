<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../base.jsp"%>
<%@ include file="navTop.jsp"%>
<div class="row" style="margin-top: 60px;">
	<div class="col-md-1">
		<jsp:include page="navLeft.jsp">
			<jsp:param value="1" name="index"/>
		</jsp:include>
	</div>
	<div class="col-md-10">
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4">
				<div class="input-group input-group-lg">
				<input type="text" class="form-control" placeholder="Search for..." id="keyword"> <span class="input-group-btn">
					<button class="btn btn-default" type="button" onclick="queryProductByKeyword()">搜索	</button>
				</span>
				</div>			
			</div>
			<div class="col-md-4"></div>
		</div>
		
		<br>
		<div class="row" id="products">
			
		</div>
	</div>
	<div class="col-md-1"></div>
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
		    url: "queryProducts",
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
			    			$("#products").append('<div class="col-md-3"><div class="thumbnail"><a href="productDetail?id='+pro.id+'" target="_blank"><img src="'+pro.imgsPath[0]+'" alt="..."></a><div class="caption"><h3></h3><p>'+pro.name+'</p><p>￥'+pro.sellPrice+'</p><p>'+
									'<a href="productEdit?id='+pro.id+'" class="btn btn-primary" role="button">修改</a> <a href="#" class="btn btn-primary" role="button" onclick="deleteProduct('+pro.id+')">删除</a>'+
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
	queryProduct("",1);
	
	function deleteProduct(id){
		modal({
			type: 'confirm',
			title: 'Confirm',
			text: '确定删除商品？',
			callback: function(result) {
				if(result==true){
					$.ajax({
					    type: "POST",
					    url: "deleteProduct",
					    data:{id:id},
					    dataType: "json",
					    success: 
					    	function(data,status)
					    	{   
					    		if(data.result=="ok"){
					    			window.location.reload(false);
					    		}
					    	},
					    error: 
					    	function(jqXHR, textStatus, errorThrown ){
					    	    
					    	}
						}
					);
				}
			},
			buttonText: {
				yes: '确定',
				cancel: '取消'
			}
		});
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
	$(document).keydown(function (event) {
		switch (event.keyCode) {
	    	case 13:
	    		queryProductByKeyword();
	    		break;
		}
	});
</script>


<%@ include file="../bottom.jsp"%>
