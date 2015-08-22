var app = angular.module("KubikProductCategoriesPage", []);
var kubikProductCategories = new KubikProductCategories({
	app : app,
	categoriesUrl : "category"
});

app.controller("KubikProductCategoriesPageController", function($scope, $http, $timeout){	
	$scope.kubikProductCategories = kubikProductCategories;
});