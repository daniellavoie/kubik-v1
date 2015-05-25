var app = angular.module("KubikRestocks", []);

app.controller("KubikRestocksController", function($scope, $http){	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadRestocks();
	};
	
	$scope.loadRestocks = function(successCallback){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};
		
		$http.get("restock?" + $.param(params)).success(function(restocksPage){
			$scope.restocksPage = restocksPage;
			
			if(successCallback != undefined){
				successCallback();
			}
		});
	};
	
	$scope.openRestock = function(restock){
		$scope.restock = restock;

		$http.get("product/" + restock.product.id + "/productStats").success(function(productStats){
			$scope.productStats = productStats;
			
			$(".restock-modal").modal();
		});
	};
	
	$scope.validateRestock = function(status, openNextRestock){
		$scope.restock.status = status;
		
		$http.post("restock/", $scope.restock).success(function(){
			$scope.loadRestocks(function(){
				if(openNextRestock != undefined && openNextRestock && $scope.restocksPage.content.length > 0){
					$scope.openRestock($scope.restocksPage.content[0]);					
				}else{
					$(".restock-modal").modal("hide");
				}
			});
		});
	}
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "openDate";
	$scope.direction = "DESC";
	
	$scope.loadRestocks();
});