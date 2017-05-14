<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../base.jsp"%>
<div class="row">
	<div class="col-md-1">
	</div>
	<div class="col-md-10">
		<br>
		<div class="input-group">
			<span class="input-group-addon" id="basic-addon1">商品名称</span> 
			<input type="text" class="form-control" placeholder="商品名称" aria-describedby="basic-addon1" id="name" value="${product.name}" readonly="readonly">
			<span class="input-group-addon" id="basic-addon4">进货价格</span> 
			<input type="text" class="form-control" placeholder="进货价格" aria-describedby="basic-addon4" id="buyPrice" value="${product.buyPrice}">
			<span class="input-group-addon" id="basic-addon5">卖出价格</span> 
			<input type="text" class="form-control" placeholder="卖价" aria-describedby="basic-addon5" id="sellPrice" value="${product.sellPrice}">
		</div>
		<br>
		<div class="row" id="imgRow">
			<c:forEach var="path" items="${product.imgsPath}">
				<div class="col-xs-6 col-md-3" id="imgContain"><a href="#"  class="thumbnail"><img src="${path}"></a></div>
			</c:forEach>
		</div>
		<br>
		<div class="input-group">
			<span class="input-group-addon" id="basic-addon2">商品简介</span>
			<textarea rows="5"  class="form-control" placeholder="商品简介" aria-describedby="basic-addon2" id="description" >${product.description}</textarea>
		</div>
		<br>
	</div>
	<div class="col-md-1"></div>
</div>

<script type="text/javascript">


	
	

	
</script>


<%@ include file="../bottom.jsp"%>