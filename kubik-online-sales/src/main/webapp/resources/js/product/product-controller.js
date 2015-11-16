(function(){
	var productId = window.location.pathname.split("/")[2];
	
	angular
		.module("kos")
		.controller("ProductCtrl", ProductCtrl);
	
	function ProductCtrl($scope, productService){
		var vm = this;
		
		productService.loadProduct(productId).then(loadProductSuccess);
		
		function loadProductSuccess(product){
			vm.product = product;
			
			$scope.$emit("updateTitle", vm.product.title);
		}
	}
})();