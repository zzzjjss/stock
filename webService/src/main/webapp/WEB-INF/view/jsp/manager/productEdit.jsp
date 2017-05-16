<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../base.jsp"%>
<div class="row">
	<div class="col-md-1">
		<jsp:include page="navLeft.jsp">
			<jsp:param value="1" name="index"/>
		</jsp:include>
	</div>
	<div class="col-md-10">
		<br>
		<div class="input-group">
			<span class="input-group-addon" id="basic-addon1">商品名称</span> 
			<input type="text" class="form-control" placeholder="商品名称" aria-describedby="basic-addon1" id="name" value="${product.name}">
			<span class="input-group-addon" id="basic-addon4">进货价格</span> 
			<input type="text" class="form-control" placeholder="进货价格" aria-describedby="basic-addon4" id="buyPrice" value="${product.buyPrice}">
			<span class="input-group-addon" id="basic-addon5">卖出价格</span> 
			<input type="text" class="form-control" placeholder="卖价" aria-describedby="basic-addon5" id="sellPrice" value="${product.sellPrice}">
		</div>
		<br>
		<div class="row" id="imgRow">
			<c:forEach var="path" items="${product.imgsPath}">
				<div class="col-xs-6 col-md-3" id="imgContain"><a href="#" onclick="deleteImg(this)" class="thumbnail"><img src="${path}"></a></div>
			</c:forEach>
			<div class="col-xs-6 col-md-3">
				<a href="#" class="thumbnail"> 
					
					<input type="file" id="imgUpload" name="imgFile" onchange="uploadProductImg()">	
				</a>
			</div>
		</div>
		<br>
		<div class="input-group">
			<span class="input-group-addon" id="basic-addon2">商品简介</span>
			<textarea rows="5"  class="form-control" placeholder="商品简介" aria-describedby="basic-addon2" id="description" >${product.description}</textarea>
		</div>
		<br>
		<div class="input-group">
			<span class="input-group-addon" id="basic-addon3">搜索关键字</span>
			<textarea rows="5"  class="form-control" placeholder="搜索关键字" aria-describedby="basic-addon3" id="searchKeywords">${product.searchKeywords}</textarea>
		</div>
		<br>
		
		<div class="input-group">
			<button class="btn btn-lg" onclick="saveProduct()">保存</button>
		</div>		
	</div>
	<div class="col-md-1"></div>
</div>

<script type="text/javascript">
	function saveProduct(){
		var id="${product.id}";		
		var name=$("#name").val();
		var desc=$("#description").val();
		var searchKeywords=$("#searchKeywords").val();
		var buyPrice=$("#buyPrice").val();
		var sellPrice=$("#sellPrice").val();
		var imgsPath=new Array();
		$("#imgRow  img").each(function(index){
			imgsPath.push($(this).attr("src"));
		});
		var imgs=imgsPath.join(",");
		
		if(name.trim()==""||desc.trim()==""||searchKeywords.trim()==""||buyPrice.trim()==""||sellPrice.trim()==""||imgsPath.length==0){
			modal({
				type: 'alert',
				title: '提示',
				text: '输入不能有空值'
			});
			return ;
		}
		
		$.ajax({
		    type: "POST",
		    url: "saveProduct",
		    data:{id:id,name:name,description:desc,searchKeywords:searchKeywords,buyPrice:buyPrice,sellPrice:sellPrice,imgs:imgs},
		    dataType: "json",
		    success: 
		    	function(data,status)
		    	{   
					if(data.result=="ok"){
						window.location="productQuery";
					}			    		
            	
		    	},
		    error: 
		    	function(jqXHR, textStatus, errorThrown ){
		    	    
		    	}
			}
		);
	
		
	}

	function uploadProductImg(){
		var img=$("#imgRow #imgUpload").val();
		if(img=="")
			return;
		
		$.ajaxFileUpload(
				{
					url:'uploadProductImg',
					secureuri:false,
					fileElementId:'imgUpload',
					dataType: 'json',					
					complete:function()
					{
					},				
					success: function (data, status)
					{
						if(data.result=="ok"){
							$("#imgRow").prepend('<div class="col-xs-6 col-md-3" id="imgContain"><a href="#" onclick="deleteImg(this)" class="thumbnail"><img src='+data.imgPath+'></a></div>');
						}						
					},
					error: function (data, status, e)
					{
						alert(e);
					}
				}
			);
	}
	
	function deleteImg(imga){
		modal({
			type: 'confirm',
			title: 'Confirm',
			text: '确定删除图片？',
			callback: function(result) {
				if(result==true){
					$(imga).parents("#imgContain").remove();
				}
			},
			buttonText: {
				yes: '确定',
				cancel: '取消'
			}
		});
	}
	
</script>


<%@ include file="../bottom.jsp"%>