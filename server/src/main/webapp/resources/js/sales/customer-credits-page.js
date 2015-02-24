var app = angular.module("KubikCustomerCreditsPage", []);

app.controller("KubikCustomerCreditsPageController", function($scope, $http, $timeout){
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadCustomerCredits();
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
	
	$scope.openCustomerCard = function(customer, $event){
		try{
			$scope.kubikCustomerCard.openCard(customer);
		}finally{
			$event.stopPropagation();
		}
	}
	
	$scope.openCustomerCredit = function(customerCredit){
		location.href = "customerCredit/" + invoice.id;
	}
	
	$scope.kubikCustomerCard = new KubikCustomerCard({customerUrl : "customer", customerSaved : function(){
		$scope.loadCustomerCredits();
	}});
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "date";
	$scope.direction = "DESC";
	
	$scope.loadCustomerCredits();
});