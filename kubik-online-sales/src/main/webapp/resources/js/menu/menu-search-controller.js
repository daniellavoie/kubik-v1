(function(){
	var PRODUCT_URL = "/product";
	
	var productId = window.location.pathname.split("/")[2];
	
	angular
		.module("kos")
		.controller("MenuSearchCtrl", MenuSearchCtrl);
	
	function MenuSearchCtrl(){
		var vm = this;
		
		var params = $.getUrlVars();
		if(params.query != undefined){
			vm.searchParams = {query : params.query};
		}
		
		vm.search = search;
		vm.searchKeyUp = searchKeyUp;
		
		function search(){
			location.href = PRODUCT_URL + "?" + $.param(vm.searchParams);
		}
		
		function searchKeyUp($event){
			if($event.keyCode == 13){
				vm.search();
			}
		}
	}
})();