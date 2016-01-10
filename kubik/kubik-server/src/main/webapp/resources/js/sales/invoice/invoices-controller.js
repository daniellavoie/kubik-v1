(function(){
	angular
		.module("Kubik")
		.controller("InvoicesCtrl", InvoicesCtrl);
	
	function InvoicesCtrl(invoiceService, $scope, $location){
		var vm = this;
		
		vm.params = $location.search();
		
		if(vm.params.sort == undefined)
			vm.params.sort = ["paidDate,DESC", "date,DESC"];
		
		if(vm.params.size == undefined)
			vm.params.size = 50;
		
		if(vm.params.status == undefined)
			vm.params.status = "PAID";
		
		vm.invoice = {};
		
		search();
		$(".search-invoice").select();
		
		vm.changePage = changePage;
		vm.changeStatus = changeStatus;
		vm.findByNumber = findByNumber;
		vm.findByNumberKeyPressed = findByNumberKeyPressed;
		vm.hideErrors = hideErrors;
		vm.openCustomerCard = openCustomerCard;
		vm.openCustomerSearch = openCustomerSearch;
		vm.openInvoice = openInvoice;
		vm.search = search;

		$scope.$on("customerSaved", customerSavedEvent);
		
		$scope.$on("customerSelected", customerSelectedEvent);
		
		function changePage(page){
			vm.params.page = page;
			
			search();
		}
		
		function changeStatus(status){
			vm.params.status = status;
			
			search();
		}
		
		function customerSavedEvent($event, customer){
			search();
		}
		
		function customerSelectedEvent($event, customer){
			generateNewOrder(customer.id);
		}

		function findByNumber(){
			invoiceService
				.findByNumber(vm.invoice.number)
				.then(findByNumberSuccess);
			
			function findByNumberSuccess(invoice){
				if(invoice != "")
					window.location.href = "/invoice/" + invoice.id;
				else{
					vm.error = "Aucune facture retrouvée pour le numéro " + vm.invoice.number;
					
					$(".search-invoice").select();
				}
			}
		}
		
		function findByNumberKeyPressed($event){
			if($event.keyCode == 13){
				findByNumber();
			}
		}
		
		function generateNewOrder(customerId){
			invoiceService
				.generateNewOrder(customerId)
				.then(generateNewOrderSuccess);
			
			function generateNewOrderSuccess(invoice){
				window.location.href = "/invoice/" + invoice.id;
			}
		}
		
		function hideErrors(){
			vm.error = null;
		}
		
		function openCustomerCard($event, customer){
			$event.stopPropagation();
			
			$scope.$broadcast("openCustomerCard", customer);
		}
		
		function openCustomerSearch($event){
			$event.stopPropagation();
			
			$scope.$broadcast("openCustomerSearchModal");
		}

		function openInvoice(invoice){
			location.href = "invoice/" + invoice.id;
		}
		
		function search(){
			$location.search(vm.params);
			
			invoiceService
				.search(vm.params)
				.then(searchSuccess);
			
			function searchSuccess(invoicePage){
				vm.invoicePage = invoicePage;
			}
		}
	}
})();