var app = angular.module("KubikReceptionDetails", []);
var receptionId = window.location.pathname.split("/")[2];
var kubikProductCard = new KubikProductCard({
	app : app, 
	productUrl : "../product"
});

app.controller("KubikReceptionDetailsController", function($scope, $http, $timeout){
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
	
	$scope.receptionChanged = function($event){
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

	$scope.kubikProductCard = kubikProductCard;

	$scope.kubikSupplierCard = new KubikSupplierCard({
		app : app,
		supplierSaved : function(){
			$scope.loadReception();
		}, 
		supplierUrl : "../supplier"
	});
	
	$scope.loadReception();
});