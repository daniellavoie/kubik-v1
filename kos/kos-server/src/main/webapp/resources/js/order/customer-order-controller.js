(function(){
	var customerOrderId = window.location.pathname.split("/")[2];
	
	angular
		.module("kos")
		.controller("CustomerOrderCtrl", CustomerOrderCtrl);
	
	function CustomerOrderCtrl($scope, customerOrderService){
		var vm = this;

		$scope.$emit("updateTitle", "Panier");
		
		loadCustomerOrder();
		
		vm.deleteDetail = deleteDetail;
		vm.saveCustomerOrder = saveCustomerOrder;
		
		function deleteDetail($event, detail){
			$event.stopPropagation();
			
			for(var detailIndex in vm.customerOrder.customerOrderDetails){
				if(vm.customerOrder.customerOrderDetails[detailIndex].id == detail.id){
					vm.customerOrder.customerOrderDetails.splice(detailIndex, 1);
					
					break;
				}
			}
			
			vm.saveCustomerOrder();
		}
		
		function loadCustomerOrder(){
			customerOrderService
				.loadCustomerOrder(customerOrderId)
				.then(loadCustomerOrderSuccess);
			
			function loadCustomerOrderSuccess(customerOrder){
				vm.customerOrder = customerOrder;
			}
		}
		
		function saveCustomerOrder(){
			vm.loading = true;
			
			customerOrderService
				.saveCustomerOrder(vm.customerOrder)
				.then(saveCustomerOrderSuccess)
				.finally(saveCustomerOrderCompleted);
			
			function saveCustomerOrderCompleted(){
				vm.loading = false;
			}
			
			function saveCustomerOrderSuccess(customerOrder){
				vm.customerOrder = customerOrder;
			}
		}
	}
})();