$.ajaxSetup({ cache: false });

$(function(){
	$("form.logout a").click(function(){
		$("form.logout").submit();
	})
});

(function(){
	angular.module("Kubik", []);
})();

$(document).on('hidden.bs.modal', '.modal', function () {
    $('.modal:visible').length && $(document.body).addClass('modal-open');
});