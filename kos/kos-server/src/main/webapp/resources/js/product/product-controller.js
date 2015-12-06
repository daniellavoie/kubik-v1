(function(){
	var productId = window.location.pathname.split("/")[2];
	
	angular
		.module("kos")
		.controller("ProductCtrl", ProductCtrl);
	
	function ProductCtrl($scope, customerOrderService, productService){
		var vm = this;

		vm.addingProductToCustomerOrder = false;
		vm.productAdded = false;
		
		productService
			.loadProduct(productId)
			.then(loadProductSuccess);		
		
		vm.addProductToCustomerOrder = addProductToCustomerOrder;
		
		function addProductToCustomerOrder(){
			vm.productAdded = false;
			vm.addingProductToCustomerOrder = true;
			
			customerOrderService
				.addProductToCustomerOrder(vm.product, 1)
				.then(addProductToCustomerOrderSuccess)
				.finally(addProductToCustomerOrderCompleted);
			
			function addProductToCustomerOrderCompleted(){
				vm.addingProductToCustomerOrder = false;
			}
			
			function addProductToCustomerOrderSuccess(){
				vm.productAdded = true;
				
				$scope.$broadcast("loadCustomerOrderSummary");
			}
		}
		
		function loadProductSuccess(product){
			vm.product = product;
			
			$scope.$emit("updateTitle", vm.product.title);
		}
	}
})();