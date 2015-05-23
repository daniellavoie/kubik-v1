var app = angular.module("KubikProductsStats", []);
var orderId = window.location.pathname.split("/")[2];

app.controller("KubikProductsStatsController", function($scope, $http, $timeout){	

	$scope.$on("openProductCard", function(event, product){
		$scope.kubikProductCard.openCard(product);
	});
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadProductsStats();
	}
	
	$scope.loadProductsStats = function(){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage};
		
		$http.get("productStats?" + $.param(params)).success(function(productsStatsPage){
			$scope.productsStatsPage = productsStatsPage;
		});
	};

	$scope.kubikProductCard = new KubikProductCard({
		productSaved : function(){
			$scope.loadOrder();
		}, 
		productUrl : "product"
	});
	
	$scope.page = 0;
	$scope.resultPerPage = 200;
	
	$scope.loadProductsStats();
});