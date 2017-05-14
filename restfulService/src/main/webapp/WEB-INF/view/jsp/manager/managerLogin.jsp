<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../base.jsp"%>
<div class="col-md-4"></div>
<div class="col-md-4">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">店主登录</h3>
		</div>
		<div class="panel-body">
			<div class="input-group">
			<span class="input-group-addon" id="basic-addon2">用户名</span>
			<input type="text"  class="form-control" placeholder="用户名" aria-describedby="basic-addon2" id="userName" />
		</div>
		<br>
		<div class="input-group">
			<span class="input-group-addon" id="basic-addon3">密&nbsp;&nbsp;&nbsp;码</span>
			<input type="password"  class="form-control" placeholder="密码" aria-describedby="basic-addon3" id="password"/>
		</div>
		</div>
		<div class="panel-footer">
			<button class="btn btn-lg" onclick="login()">登录</button>
		</div>
	</div>
</div>
<div class="col-md-4"></div>

<script type="text/javascript">
function login(){
	var userName=$("#userName").val();
	var password=$("#password").val();
	$.ajax({
	    type: "POST",
	    url: "login",
	    dataType: "json",
	    data:{userName:userName,password:password},
	    success: 
	    	function(data,status)
	    	{   
		    	if(data.result=="ok"){
	    	 		window.location="auth/productQuery"
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
$(document).keydown(function (event) {
	switch (event.keyCode) {
    	case 13:
    		login();
    		break;
	}
});
</script>
<%@ include file="../bottom.jsp"%>
