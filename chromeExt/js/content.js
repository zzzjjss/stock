$(document).ready(function() {
	var index=0;
	var bookNames=books.books;
	var foundDownUrl=new Set();
	var domTime;
	$("body").on("DOMSubtreeModified",function(){
		var date=new Date();
		domTime=date.getTime();
		var downLoadUrl = $("body a.localbook_download");	
		if(downLoadUrl!=null&&downLoadUrl.length>0){
			downLoadUrl.each(function(index){
				var downUrl=$(this).attr("href");
				if(!foundDownUrl.has(downUrl)){
//					console.log(downUrl);
					window.open(downUrl);
					foundDownUrl.add(downUrl);
				}
			});
		}
		});


		$("#SearchWord").val(bookNames[index]);
		$("#SearchButton").click();
		setInterval(function(){
			var date=new Date();
			if(date.getTime()-domTime>10*1000){
				foundDownUrl.clear();
				index++;
				$("#SearchWord").val(bookNames[index]);
				$("#SearchButton").click();
				domTime=date.getTime();
			}
		},10000);	
	
});
