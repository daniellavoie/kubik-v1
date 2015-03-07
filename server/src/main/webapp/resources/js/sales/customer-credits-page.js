var app = angular.module("KubikCustomerCreditsPage", []);
var kukikCustomerSearch = new KubikCustomerSearch({
	app : app,
	customerUrl : "customer",
	modal : true,
	$container : $(".customers-modal")
});

app.controller("KubikCustomerCreditsPageController", function($scope, $http, $timeout){
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadCustomerCredits();
	};
	
	$scope.createCustomerCredit = function(){
		$http.post("customerCredit", $scope.customerCredit).success(function(customerCredit){
			location.href = "customerCredit/" + customerCredit.id;
		});
	};
	
	$scope.invoiceNumberKeyUp= function($event){
		if($event.keyCode == 13){
			$scope.validateInvoiceNumber();
		}
	}
	
	$scope.loadCustomerCredits = function(){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};
		
		$http.get("customerCredit?" + $.param(params)).success(function(customerCreditsPage){
			$scope.customerCreditsPage = customerCreditsPage;
		});
	};
	
	$scope.newCustomerCredit = function(){
		// Open the modal.
		$(".new-customer-credit-modal").modal({
			backdrop : "static",
			keyboard : "false"
		}).on("shown.bs.modal", function(){
			$(".invoice-number").focus();
		});
	};
	
	$scope.openCustomerCard = function(customer, $event){
		try{
			$scope.customerCredit = null;
			$scope.kubikCustomerCard.openCard(customer);
		}finally{
			$event.stopPropagation();
		}
	};
	
	$scope.openCustomerCredit = function(customerCredit){
		location.href = "customerCredit/" + customerCredit.id;
	};
	
	$scope.showCustomerSelection = function(){
		$scope.kubikCustomerSearch.openSearchModal();
	}
	
	$scope.validateInvoiceNumber = function(){
		$http.get("invoice?number=" + $scope.customerCredit.invoice.number).success(function(invoice){
			$scope.customerCredit.invoice = invoice;
			
			if($scope.customerCredit.invoice.customer == null){
				$scope.showCustomerSelection();
			}else{
				$scope.customerCredit.customer = $scope.customerCredit.invoice.customer;
				
				$scope.createCustomerCredit();
			}
		});
	};
	
	$scope.kubikCustomerSearch = kukikCustomerSearch;
	$scope.kubikCustomerSearch.customerSelected = function(customer){
		$scope.customerCredit.customer = customer;
		
		$scope.kubikCustomerSearch.closeSearchModal();
		
		$scope.loadCustomerCredits(); 
		
		$scope.createCustomerCredit();
	};
	
	$scope.kubikCustomerCard = new KubikCustomerCard({
		customerUrl : "customer", 
		customerSaved : function(customer){
			if($scope.customerCredit != null){
				$scope.customerCredit.customer = customer;
				
				$scope.createCustomerCredit();
				
				$scope.kubikCustomerCard.closeCard();
				$scope.kubikCustomerSearch.closeSearchModal();
			}else{
				$scope.loadCustomerCredits();
			}
		}
	});
	
	$scope.customerCredit = {status : "OPEN"};
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "date";
	$scope.direction = "DESC";
	
	$scope.loadCustomerCredits();
});