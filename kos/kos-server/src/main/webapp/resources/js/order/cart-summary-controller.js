(function(){
	angular
		.module("kos")
		.controller("CartSummaryCtrl", CartSummaryCtrl);
	
	function CartSummaryCtrl($scope, customerOrderService){
		var vm = this;
		
		$scope.$on("loadCartSummary", loadCartSummary);
		
		vm.loading = true;
		
		loadCartSummary();
		
		function loadCartSummary(){
			customerOrderService
				.loadCart()
				.then(loadCartSuccess);
			
			function loadCartSuccess(customerOrder){
				vm.loading = false;
				vm.customerOrder = customerOrder;
				vm.totalQuantity = customerOrderService.calculateTotalQuantity(customerOrder);
			}
		}
	}
})();