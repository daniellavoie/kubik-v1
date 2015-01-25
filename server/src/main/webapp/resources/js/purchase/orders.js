var app = angular.module("KubikPurchaseOrders", []);

app.controller("KubikPurchaseOrdersController", function($scope, $http){
	$scope.calculateOrderQuantity = function(order){
		var quantity = 0;
		for(var detailIndex in order.details){
			var detail = order.details[detailIndex];
			
			quantity += detail.quantity;
		}
		
		return quantity;
	};
	
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