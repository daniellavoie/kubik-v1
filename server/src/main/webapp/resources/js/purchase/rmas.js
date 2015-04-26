var app = angular.module("KubikRmas", []);

app.controller("KubikRmasController", function($scope, $http){
	$scope.calculateRmaQuantity = function(rma){
		var quantity = 0;
		for(var detailIndex in rma.details){
			var detail = rma.details[detailIndex];
			
			quantity += detail.quantity;
		}
		
		return quantity;
	};
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadRmas();
	}
	
	$scope.createRma = function(){
		$http.post("rma", $scope.rma).success(function(rma){
			location.href = "rma/" + rma.id;
		});
	};
	
	$scope.loadRmas = function(){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};
		
		$http.get("rma?" + $.param(params)).success(function(rmasPage){
			$scope.rmasPage = rmasPage;
		});
	};
		
	$scope.newRma = function(){
		// Open the modal.
		$(".new-rma-modal").modal({
			backdrop : "static",
			keyboard : "false"
		}).on("shown.bs.modal", function(){
			$(".supplier-ean13").focus();
		});
	};
	
	$scope.openOrder = function(id){
		window.location.href = "rma/" + id;
	};
	
	$scope.supplierEan13KeyUp= function($event){
		if($event.keyCode == 13){
			$scope.validateSupplierEan13();
		}
	}
	
	$scope.validateSupplierEan13 = function(){
		$scope.error.supplierNotFound = false;
		$http.get("supplier?ean13=" + $scope.rma.supplier.ean13).success(function(supplier){
			if(supplier != ""){
				$scope.rma.supplier = supplier;
				
				$scope.createRma();
			}else{
				$scope.error.supplierNotFound = true;
			}
		});
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
			$scope.loadRmas();
		}, 
		supplierUrl : "supplier"
	});
	
	$scope.rma = {supplier : {}, status : "OPEN"};
	$scope.error = {};
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "openDate";
	$scope.direction = "DESC";
	
	$scope.loadRmas();
});