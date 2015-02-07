$.ajaxSetup({ cache: false });

$(function(){
	$("form.logout a").click(function(){
		$("form.logout").submit();
	})
});