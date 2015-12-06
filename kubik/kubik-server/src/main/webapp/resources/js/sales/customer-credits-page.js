(function(){
	angular
		.module("Kubik")
		.controller("CustomerCreditsCtrl", CustomerCreditsCtrl);
	
	function CustomerCreditsCtrl($scope, $http, $timeout){
		var vm = this;
		
		vm.customerCredit = {status : "OPEN"};
		vm.page = 0;
		vm.resultPerPage = 50;
		vm.sortBy = "date";
		vm.direction = "DESC";
		
		vm.changePage = changePage;
		vm.openCustomerCard = openCustomerCard;
		vm.createCustomerCredit = createCustomerCredit;
		vm.invoiceNumberKeyUp = invoiceNumberKeyUp;
		vm.loadCustomerCredits = loadCustomerCredits;
		vm.newCustomerCredit = newCustomerCredit;
		vm.openCustomerCard = openCustomerCard;
		vm.openCustomerCredit = openCustomerCredit;
		vm.showCustomerSelection = showCustomerSelection;
		vm.validateInvoiceNumber = validateInvoiceNumber;
		
		loadCustomerCredits();
		
		$scope.$on("customerSaved", function($event, customer){
			if(vm.customerCredit != null){
				vm.customerCredit.customer = customer;
				
				vm.createCustomerCredit();
				
				$scope.$broadcast("closeCustomerCard");
				$scope.$broadcast("closeCustomerSearchModal");
			}else{
				vm.loadCustomerCredits();
			}
		});
		
		$scope.$on("customerSelected", function($event, customer){
			vm.customerCredit.customer = customer;
			
			$scope.$broadcast("closeCustomerSearchModal");
			
			vm.loadCustomerCredits(); 
			
			vm.createCustomerCredit();
		});
		
		function changePage(page){
			vm.page = page;
			
			vm.loadCustomerCredits();
		}
		
		function createCustomerCredit(){
			$http.post("customerCredit", vm.customerCredit).success(function(customerCredit){
				location.href = "customerCredit/" + customerCredit.id;
			});
		}
		
		function invoiceNumberKeyUp($event){
			if($event.keyCode == 13){
				vm.validateInvoiceNumber();
			}
		}

		function loadCustomerCredits(){
			var params = {	page : vm.page,
							resultPerPage : vm.resultPerPage,
							sortBy : vm.sortBy,
							direction : vm.direction};
			
			$http.get("customerCredit?" + $.param(params)).success(function(customerCreditsPage){
				vm.customerCreditsPage = customerCreditsPage;
			});
		}
		
		function newCustomerCredit(){
			// Open the modal.
			$(".new-customer-credit-modal").on("shown.bs.modal", function(){
				$(".invoice-number").focus();
			}).modal({
				backdrop : "static",
				keyboard : "false"
			});
		}
		
		function openCustomerCard($event, customer){
			$event.stopPropagation();
			
			vm.customerCredit = null;
			
			$scope.$broadcast("openCustomerCard", customer);
		}
		
		function openCustomerCredit(customerCredit){
			location.href = "customerCredit/" + customerCredit.id;
		}
		
		function showCustomerSelection(){
			$scope.$broadcast("openCustomerSearchModal");
		}
		
		function validateInvoiceNumber(){
			$http.get("invoice?number=" + vm.customerCredit.invoice.number).success(function(invoice){
				vm.customerCredit.invoice = invoice;
				
				if(vm.customerCredit.invoice.customer == null){
					vm.showCustomerSelection();
				}else{
					vm.customerCredit.customer = vm.customerCredit.invoice.customer;
					
					vm.createCustomerCredit();
				}
			});
		}
	}
})();