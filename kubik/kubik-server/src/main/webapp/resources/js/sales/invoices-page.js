(function(){
	angular
		.module("Kubik")
		.controller("InvoicesCtrl", InvoicesCtrl);
	
	function InvoicesCtrl($scope, $http, $timeout){
		var vm = this;
		
		vm.page = 0;
		vm.resultPerPage = 50;
		vm.sortBy = "paidDate";
		vm.direction = "DESC";
		
		vm.invoice = {};
		
		vm.changePage = changePage;
		vm.hideErrors = hideErrors;
		vm.loadInvoices = loadInvoices;
		vm.openCustomerCard = openCustomerCard;
		vm.openInvoice = openInvoice;
		vm.searchInvoice = searchInvoice;
		vm.searchInvoiceKeyPressed = searchInvoiceKeyPressed;
		
		loadInvoices();

		$(".search-invoice").select();
		
		$scope.$on("customerSaved", function($event, customer){
			vm.loadInvoices();
		});
		
		function changePage(page){
			vm.page = page;
			
			vm.loadInvoices();
		}
		
		function hideErrors(){
			vm.error = null;
		}
		
		function loadInvoices(){
			var params = {	page : vm.page,
							resultPerPage : vm.resultPerPage,
							sortBy : vm.sortBy,
							direction : vm.direction};
			
			$http.get("invoice?" + $.param(params)).success(function(invoicePage){
				vm.invoicePage = invoicePage;
			});
		}
		
		function openCustomerCard($event, customer){
			$event.stopPropagation();
			
			$scope.$broadcast("openCustomerCard", customer);
		}

		function openInvoice(invoice){
			location.href = "invoice/" + invoice.id;
		}

		function searchInvoice(){
			$http.get("/invoice?number=" + vm.invoice.number).success(function(invoice){
				if(invoice == ""){
					vm.error = "Aucune facture retrouvée pour le numéro " + vm.invoice.number;
					
					$(".search-invoice").select();
					
					return;
				}
				window.location.href = "/invoice/" + invoice.id;
			});
		}
		
		function searchInvoiceKeyPressed($event){
			if($event.keyCode == 13){
				vm.searchInvoice();
			}
		}
	}
})();