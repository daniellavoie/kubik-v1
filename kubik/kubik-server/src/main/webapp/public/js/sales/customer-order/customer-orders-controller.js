(function(){
	angular
		.module("Kubik")
		.controller("CustomerOrdersCtrl", CustomerOrdersCtrl);
	
	function CustomerOrdersCtrl(customerOrderService, $location){
		var vm = this;

		vm.params = $location.search();
		
		vm.params.status = ["TO_CONFIRM", "CONFIRMED", "PROCESSED", "ERROR"];
		
		search();
		
		vm.changePage = changePage;
		vm.openOrder = openOrder;
		vm.search = search;
		
		function changePage(page){
			vm.params.page = page;
			
			search();
		}
		
		function openOrder($event, customerOrder){
			$event.stopPropagation();
			window.location.href = "/customer-order/" + customerOrder.id;
		}
		
		function search(){
			var params = vm.params;
			
			$location.search(params);
			
			customerOrderService
				.search(vm.params)
				.then(searchSuccess);
			
			function searchSuccess(customerOrdersPage){
				vm.customerOrdersPage = customerOrdersPage;
			}
		}
	}
})();