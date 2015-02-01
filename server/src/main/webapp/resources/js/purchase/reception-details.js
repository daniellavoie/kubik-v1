var app = angular.module("KubikReceptionDetails", []);
var receptionId = window.location.pathname.split("/")[2];

app.controller("KubikReceptionDetailsController", function($scope, $http, $timeout){	
	$scope.cancelReception = function(){
		$scope.order.status = "CANCELED";
		
		$scope.saveOrder(function(){
			$(".confirm-cancel").modal("hide");
			
			$(".redirection-modal").modal();
		});
	};
	
	$scope.confirmReceptionValidation = function(){
		$(".confirm-validation").modal();
	};
	
	$scope.loadReception = function(){
		$http.get(receptionId).success(function(reception){
			$scope.reception = reception;

			$timeout(function(){
				if($scope.inputIdToFocus != undefined){
					$("#" + $scope.inputIdToFocus).focus();
				}
			})
		});
	};
	
	$scope.quantityChanged = function($event){
		$scope.inputIdToFocus = $event.target.id;
		if($scope.quantityChangedTimer != undefined) clearTimeout($scope.quantityChangedTimer);
	    
		$scope.quantityChangedTimer = setTimeout($scope.saveReception, 1000);
	};
	
	$scope.redirectToPurchaseOrders = function(){
		location.href = "../purchaseOrder";
	};
	
	$scope.redirectToReceptions = function(){
		location.href = "../reception";		
	};
	
	$scope.saveReception = function(success){
		for(var detailIndex in $scope.reception.details){
			var detail = $scope.reception.details[detailIndex];
						
			detail.product = {
				id : detail.product.id,
				ean13 : detail.product.ean13,
				supplier : {id : detail.product.supplier.id}
			};
		}
		$http.post(".", $scope.reception).success(function(){
			$scope.$broadcast("receptionSaved");
			
			$scope.loadReception();
			
			if(success != undefined){
				success();
			}
		});
	};
	
	$scope.validateReception = function(){
		$scope.reception.status = "CLOSED";
		
		$scope.saveReception(function(){
			$(".confirm-validation").modal("hide");
			
			$(".redirection-modal").modal();
		});
	};

	$scope.kubikProductCard = new KubikProductCard({productUrl : "../product"});
	
	$scope.loadReception();
});