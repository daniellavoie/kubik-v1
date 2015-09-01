var app = angular.module("KubikRmaDetails", []);
var kubikProductCard = new KubikProductCard({
	app : app,
	productUrl : "../product"
}); 

var rmaId = window.location.pathname.split("/")[2];

app.controller("KubikRmaDetailsController", function($scope, $http, $timeout){
	$scope.addProduct = function($event){
		$scope.error.productNotFound = false;
		if($event.keyCode == 13){
			$scope.showLoading();
			detailFound = false;
			for(var detailIndex in $scope.rma.details){
				var detail = $scope.rma.details[detailIndex];
				
				if(detail.product.ean13 == $scope.detail.product.ean13){
					detailFound = true;
					
					detail.quantity += 1;
					$scope.saveRma();
					$scope.detail.product.ean13 = ""
					break;
				}
			}
			
			if(!detailFound){
				$http.get(
					"../product?ean13=" + $scope.detail.product.ean13 + "&supplierEan13=" + $scope.rma.supplier.ean13
				).success(function(product){
					if(product != ""){
						$scope.rma.details.push({product : product, quantity : 1, rma : {id : $scope.rma.id}});
						
						$scope.saveRma();
					}else{
						$scope.error.productNotFound = true;
					}
				}).error(function(){
					$scope.error.productNotFound = true;
				}).finally(function(){
					$scope.hideLoading();
				});
			}
			
			$("#product-ean13").select();
		}
	};
	
	$scope.cancel = function(){
		$scope.rma.status = 'CANCELED';
		
		$scope.saveRma();
	};	
	
	$scope.changeRma = function(rmaId){
		location.href = rmaId;
	};
	
	$scope.changePaymentMethod = function(paymentMethodType){
		$scope.rma.paymentMethod.type = paymentMethodType;
		
		$scope.saveRma();
	};
	
	$scope.focus = function($event){
		$scope.inputIdToFocus = $event.target.id;
	};
	
	$scope.hideAlerts = function(){
		$(".alerts .alert").addClass("hidden");
	};
	
	$scope.hideLoading = function(){
		$(".loading").addClass("hidden");
	};
		
	$scope.loadRma = function(){
		$http.get(rmaId).success(function(rma){
			$scope.rma = rma;
			
			if($scope.inputIdToFocus != undefined){
				$timeout(function(){$("#" + $scope.inputIdToFocus).focus()});
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
	
	$scope.print = function(){
		window.open($scope.rma.id + "/report", "Avis de retour fournisseur", "pdf");
	}
	
	$scope.rmaChanged = function(){
		if($scope.rmaChangedTimer != undefined) clearTimeout($scope.rmaChangedTimer);
	    
		$scope.rmaChangedTimer = setTimeout($scope.saveRma, 1000);
	}
	
	$scope.saveRma = function(){
		$scope.showLoading();
		
		angular.forEach($scope.rma.details, cleanDetailCategory);
		
		$http.post(".", $scope.rma).finally(saveCompleted);
		
		function cleanDetailCategory(detail, index){
			detail.product.category = null;
		}
		
		function saveCompleted(){
			$scope.hideLoading();
			
			$scope.loadRma();
		}
	};
	
	$scope.showLoading = function(){
		$scope.hideAlerts();
		$(".loading").removeClass("hidden");
	}
	
	$scope.showProductNotFoundAlert = function(){
		$(".product-not-found").removeClass("hidden");
	}
	
	$scope.validate = function(){
		$scope.rma.status = "SHIPPED";
		
		$scope.saveRma();
	};

	$scope.kubikSupplierCard = new KubikSupplierCard({
		supplierSaved : function(){
			$scope.loadRma();
		}, 
		supplierUrl : "../supplier"
	});
	
	$http.get(rmaId + "/next").success(function(rmaId){
		if(rmaId == "") rmaId = null;
		$scope.nextRma = rmaId;
	});
	
	$http.get(rmaId + "/previous").success(function(rmaId){
		if(rmaId == "") rmaId = null;
		$scope.previousRma = rmaId;
	});
	
	$scope.error = {};
	$scope.detail = {product : {}};

	$scope.kubikProductCard = kubikProductCard;
	
	$scope.loadRma();
});