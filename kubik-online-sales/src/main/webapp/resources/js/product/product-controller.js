(function(){
	var productId = window.location.pathname.split("/")[2];
	
	angular
		.module("kos")
		.controller("ProductCtrl", ProductCtrl);
	
	function ProductCtrl(productService, companyService){
		var vm = this;
		
		companyService.getEan13().then(function(ean13){
			vm.ean13 = ean13;	
		});
		productService.loadProduct(productId).then(function(product){
			vm.product = product;
		});
	}
})();