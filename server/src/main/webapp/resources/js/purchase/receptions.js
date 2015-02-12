var app = angular.module("KubikReceptions", []);

app.controller("KubikReceptionsController", function($scope, $http){
	$scope.calculateReceptionQuantity = function(reception){
		var quantity = 0;
		if(reception != undefined){
			for(var detailIndex in reception.details){
				var detail = reception.details[detailIndex];
				
				quantity += detail.quantityToReceive;
			}
		}
		
		return quantity;
	};
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadReceptions();
	}
	
	$scope.loadReceptions = function(){
		var params = {	page : $scope.page,
				resultPerPage : $scope.resultPerPage,
				sortBy : $scope.sortBy,
				direction : $scope.direction};

		$http.get("reception?" + $.param(params)).success(function(receptionsPage){
			$scope.receptionsPage = receptionsPage;
		});
	};
	
	$scope.openReception = function(id){
		window.location.href = "reception/" + id;
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
			$scope.loadReceptions();
		}, 
		supplierUrl : "supplier"
	});
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "deliveryDate";
	$scope.direction = "DESC";
	
	$scope.loadReceptions();
});