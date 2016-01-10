(function(){
	var customerOrderId = window.location.pathname.split("/")[2];
	
	angular
		.module("kos")
		.controller("CustomerOrdersHistoryCtrl", CustomerOrdersHistoryCtrl);
	
	function CustomerOrdersHistoryCtrl(customerOrderService, $location){
		var vm = this;
		
		vm.params = $location.search();
		
		if(vm.params.sort == undefined)
			vm.params.sort = "id,DESC";
		
		loadCustomerOrdersHistory();
		
		vm.changePage = changePage;
		vm.openCustomerOrder = openCustomerOrder;
		
		function changePage(page){
			vm.params.page = page;
			
			loadCustomerOrdersHistory();
		}
		
		function loadCustomerOrdersHistory(){
			vm.loading = true;
			
			$location.search(vm.params);
			
			customerOrderService
				.loadCustomerOrdersHistory(vm.params)
				.then(loadCustomerOrdersHistorySuccess)
				.finally(loadCustomerOrdersHistoryCompleted);
			
			function loadCustomerOrdersHistoryCompleted(){
				vm.loading = false;
			}
			
			function loadCustomerOrdersHistorySuccess(historyPage){
				vm.historyPage = historyPage;
			}
		}
		
		function openCustomerOrder(customerOrderId){
			window.location.href = "/commande-client/" + customerOrderId;
		}
	}
})();