<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav role="navigation" id="header" class="navbar navbar-default navbar-fixed-top">
	<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-6">
			
			</div>
			<div class="col-md-2" >
					<div id="navigation" class="collapse navbar-collapse">
						 <ul class="nav navbar-nav navbar-right">
						 	<li class="dropdown">
				              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"> 
					              <c:if test="${!empty manager}">
					             	 ${manager.userName} 
					              </c:if>
				              <span class="caret"></span></a>
				              <ul class="dropdown-menu" role="menu">
				                  <li>
				                    <a href="#" onclick="logOut()">退出</a>
				                    <a href="#" onclick="changePassword()">修改密码</a>
				                  </li>
			              	   </ul>
							</li>
						 </ul>
					</div>
			</div>
		</div>
</nav>
<script>
function logOut(){
	$.ajax({
	    type: "POST",
	    url: "logOut",
	    dataType: "json",
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

function changePassword(){
	
}

</script>