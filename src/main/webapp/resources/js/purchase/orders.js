var app = angular.module("KubikPurchaseOrders", []);

app.controller("KubikPurchaseOrdersController", function($scope, $http){
	$scope.loadOrders = function(){
		$http.get("purchaseOrder").success(function(orders){
			$scope.orders = orders;
		});
	};
	
	$scope.openOrder = function(id){
		window.location.href = "purchaseOrder/" + id;
	};
	
	$scope.loadOrders();
});