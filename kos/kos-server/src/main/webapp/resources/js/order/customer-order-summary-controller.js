(function(){
	angular
		.module("kos")
		.controller("CustomerOrderSummaryCtrl", CustomerOrderSummaryCtrl);
	
	function CustomerOrderSummaryCtrl($scope, customerOrderService){
		var vm = this;
		
		$scope.$on("loadCustomerOrderSummary", loadCustomerOrderSummary);
		
		vm.loading = true;
		
		loadCustomerOrderSummary();
		
		function loadCustomerOrderSummary(){
			customerOrderService
				.loadOpenCustomerOrder()
				.then(loadCustomerOrderSummarySuccess);
			
			function loadCustomerOrderSummarySuccess(customerOrder){
				vm.loading = false;
				vm.customerOrder = customerOrder;
				vm.totalQuantity = customerOrderService.calculateTotalQuantity(customerOrder);
			}
		}
	}
})();