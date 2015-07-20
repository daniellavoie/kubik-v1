var app = angular.module("KubikDilicomOrders", []);

app.controller("KubikDilicomOrdersController", function($scope, $http){	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadDilicomOrders();
	};
	
	$scope.loadDilicomOrders = function(successCallback){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};
		
		$http.get("dilicomOrder?" + $.param(params)).success(function(dilicomOrdersPage){
			$scope.dilicomOrdersPage = dilicomOrdersPage;
			
			if(successCallback != undefined){
				successCallback();
			}
		});
	};	
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "creationDate";
	$scope.direction = "DESC";
	
	$scope.loadDilicomOrders();
});