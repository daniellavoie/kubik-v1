(function(){
	var CUSTOMER_ORDER_URL = "/customer-order";
	var customerOrderId = window.location.pathname.split("/")[2];
	
	angular
		.module("kos")
		.controller("CustomerOrderConfirmationCtrl", CustomerOrderConfirmationCtrl);
	
	function CustomerOrderConfirmationCtrl($scope, customerOrderService){
		var vm = this;

		$scope.$emit("updateTitle", "Panier");
		
		loadCustomerOrder();
		
		function loadCustomerOrder(){
			customerOrderService
				.loadCustomerOrder(customerOrderId)
				.then(loadCustomerOrderSuccess);
			
			function loadCustomerOrderSuccess(customerOrder){
				vm.customerOrder = customerOrder;
			}
		}
	}
})();