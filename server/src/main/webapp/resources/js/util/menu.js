var app = angular.module("KubikMenu", []);

app.controller("KubikMenuController", function($scope, $http){
	$http.get("/notification/NEW/count").success(function(notificationCount){
		$scope.notificationCount = notificationCount;
	});
	
	$http.get("/product?category").success(function(productsWithoutCategory){
		$scope.productsWithoutCategory = productsWithoutCategory;
	});
	
	$http.get("/restock/OPEN/count").success(function(restockCount){
		$scope.restockCount = restockCount;
	});
});

angular.bootstrap($("nav.navbar")[0],['KubikMenu']);