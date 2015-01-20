var app = angular.module("KubikProductsPage", []);
var kubikProductSearch = new KubikProductSearch({
	app : app,
	productUrl : "product"
});

app.controller("KubikProductsPageController", function($scope, $http, $timeout){
	$scope.$on("openProductCard", function(event, product){
		$scope.kubikProductCard.openCard(product.id);
	})
	
	$scope.kubikProductSearch = kubikProductSearch;
	
	$scope.kubikProductCard = new KubikProductCard({productUrl : "product", productSaved : function(){
		$scope.$broadcast("search");
	}});
});