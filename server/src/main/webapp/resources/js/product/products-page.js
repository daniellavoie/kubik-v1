var app = angular.module("KubikProductsPage", []);
var kubikProductSearch = new KubikProductSearch({
	app : app,
	productUrl : "product"
});

app.controller("KubikProductsPageController", function($scope, $http, $timeout){	
	$scope.kubikProductSearch = kubikProductSearch;
});