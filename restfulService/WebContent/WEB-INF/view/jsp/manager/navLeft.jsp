<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="list-group" style="position: fixed;">
  <a href="productAdd" class="list-group-item">
    	添加商品
  </a>
  <a href="productQuery" class="list-group-item">商品查询</a>
  
</div>
<script>
	var navIndex=${param.index};
	$(".list-group a").eq(navIndex).addClass("active");

</script>