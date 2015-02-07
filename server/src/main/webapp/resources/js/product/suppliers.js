var app = angular.module("KubikSuppliers", []);

app.controller("KubikSuppliersController", function($scope, $http, $timeout){
	$scope.supplierSaved = function(supplier){
		$scope.loadSuppliers();
	};
	
	$scope.loadSuppliers = function(){
		$http.get("supplier").success(function(suppliers){
			$scope.suppliers = suppliers;
		});
	};
	
	$scope.newSupplier = function(){
		$scope.supplierCard.openCard({});
	}
	
	$scope.supplierCard = new KubikSupplierCard({	supplierUrl : "supplier", 
													supplierSaved : $scope.supplierSaved});

	$scope.loadSuppliers();
});