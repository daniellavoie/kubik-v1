var app = angular.module("KubikCustomersPage", []);

var kubikCustomerSearch = new KubikCustomerSearch({
	app : app,
	customerUrl : "customer"
});

app.controller("KubikCustomersPageController", function($scope, $http, $timeout){
	$scope.$on("openCustomerCard", function(event, customer){
		$scope.kubikCustomerCard.openCard(customer);
	})
	
	$scope.kubikCustomerSearch = kubikCustomerSearch;
	
	$scope.kubikCustomerCard = new KubikCustomerCard({customerUrl : "customer", customerSaved : function(){
		$scope.$broadcast("search");
	}});
});