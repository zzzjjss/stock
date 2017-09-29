$(document).ready(function() {
	var index=0;
	var bookNames=books.books;
	var foundDownUrl;
	$("body").on("DOMSubtreeModified",function(){
		var downLoadUrl = $("body .localbook_download").attr("href");
		if(downLoadUrl!=undefined&&downLoadUrl!=foundDownUrl){
			foundDownUrl=downLoadUrl;
			window.location=downLoadUrl;
			index++;
			setInterval(function(){
				$("#SearchWord").val(bookNames[index]);
				$("#SearchButton").click();
			},10000);
		}
		if(foundDownUrl==downLoadUrl){
			return;
		}
		
		});


		$("#SearchWord").val(bookNames[index]);
		$("#SearchButton").click();

		
//		setTimeout(function() {
//			var downLoadUrl = $("body .localbook_download").attr("href");
//			window.location = downLoadUrl;	
//						
//		}, 5000);
	
});