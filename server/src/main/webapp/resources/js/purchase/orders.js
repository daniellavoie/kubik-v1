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
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadOrders();
	}
	
	$scope.loadOrders = function(){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};
		
		$http.get("purchaseOrder?" + $.param(params)).success(function(ordersPage){
			$scope.ordersPage = ordersPage;
		});
	};
	
	$scope.openOrder = function(id){
		window.location.href = "purchaseOrder/" + id;
	};
	
	$scope.openSupplierCard = function(supplier, $event){
		try{
			$scope.kubikSupplierCard.openCard(supplier);
		}finally{
			$event.stopPropagation();
		}
	}

	$scope.kubikSupplierCard = new KubikSupplierCard({
		supplierSaved : function(){
			$scope.loadOrders();
		}, 
		supplierUrl : "supplier"
	});
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "date";
	$scope.direction = "DESC";
	
	$scope.loadOrders();
});