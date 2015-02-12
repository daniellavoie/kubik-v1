var app = angular.module("KubikInvoicesPage", []);

app.controller("KubikInvoicesPageController", function($scope, $http, $timeout){
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadInvoices();
	}
	
	$scope.loadInvoices = function(){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};
		
		$http.get("invoice?" + $.param(params)).success(function(invoicePage){
			$scope.invoicePage = invoicePage;
		});
	};
	
	$scope.openCustomerCard = function(customer, $event){
		try{
			$scope.kubikCustomerCard.openCard(customer);
		}finally{
			$event.stopPropagation();
		}
	}
	
	$scope.openInvoice = function(invoice){
		location.href = "invoice/" + invoice.id;
	}
	
	$scope.kubikCustomerCard = new KubikCustomerCard({customerUrl : "customer", customerSaved : function(){
		$scope.loadInvoices();
	}});
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "paidDate";
	$scope.direction = "DESC";
	
	$scope.loadInvoices();
});