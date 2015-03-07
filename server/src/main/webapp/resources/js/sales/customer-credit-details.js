var app = angular.module("KubikCustomerCreditDetails", []);
var customerCreditId = window.location.pathname.split("/")[2];

app.controller("KubikCustomerCreditDetailsController", function($scope, $http, $timeout){
	$scope.addProduct = function($event){
		if($event.keyCode == 13){
			$scope.showLoading();
			detailFound = false;
			for(var detailIndex in $scope.customerCredit.details){
				var detail = $scope.customerCredit.details[detailIndex];
				
				if(detail.product.ean13 == $scope.detail.product.ean13){
					detailFound = true;
					
					detail.quantity += 1;
					$scope.saveCustomerCredit();
					$scope.detail.product.ean13 = ""
					break;
				}
			}
			
			if(!detailFound){
				$http.get(
					"../invoice/" + $scope.customerCredit.invoice.id + "/detail/product/ean13/" + $scope.detail.product.ean13
				).success(function(invoiceDetail){
					$scope.customerCredit.details.push({product : invoiceDetail.product, quantity : 1, maxQuantity : invoiceDetail.quantity});
					
					$scope.saveCustomerCredit();
				}).error(function(){
					$scope.hideLoading();
					$scope.showProductNotFoundAlert();
				});
			}
		}
	};
	
	$scope.cancel = function(){
		$scope.customerCredit.status = 'CANCELED';
		
		$scope.saveCustomerCredit();
	};	
	
	$scope.changeCustomerCredit = function(customerCreditId){
		location.href = customerCreditId;
	};
	
	$scope.changePaymentMethod = function(paymentMethodType){
		$scope.customerCredit.paymentMethod.type = paymentMethodType;
		
		$scope.saveCustomerCredit();
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
		
	$scope.loadCustomerCredit = function(){
		$http.get(customerCreditId).success(function(customerCredit){
			$scope.customerCredit = customerCredit;
			
			if($scope.inputIdToFocus != undefined){
				$timeout(function(){$("#" + $scope.inputIdToFocus).focus()});
			}
		});
	};
	
	$scope.openInvoice = function(){
		location.href = "../invoice/" + $scope.customerCredit.invoice.id;
	};
	
	$scope.quantityChanged = function(){
		if($scope.quantityChangedTimer != undefined) clearTimeout($scope.quantityChangedTimer);
	    
		$scope.quantityChangedTimer = setTimeout($scope.saveCustomerCredit, 1000);
	}
	
	$scope.saveCustomerCredit = function(){
		$scope.showLoading();
		
		$http.post(".", $scope.customerCredit).finally(function(){
			$scope.hideLoading();
			
			$scope.loadCustomerCredit();
		});
	};
	
	$scope.showLoading = function(){
		$scope.hideAlerts();
		$(".loading").removeClass("hidden");
	}
	
	$scope.showProductNotFoundAlert = function(){
		$(".product-not-found").removeClass("hidden");
	}
	
	$scope.validate = function(){
		$scope.customerCredit.status = "COMPLETED";
		
		$scope.saveCustomerCredit();
	};
	
	$scope.kubikCustomerCard = new KubikCustomerCard({customerUrl : "../customer", customerSaved : function(){
		$scope.loadCustomerCredit();
	}});
	
	$http.get(customerCreditId + "/next").success(function(customerCreditId){
		if(customerCreditId == "") customerCreditId = null;
		$scope.nextCustomerCredit = customerCreditId;
	});
	
	$http.get(customerCreditId + "/previous").success(function(customerCreditId){
		if(customerCreditId == "") customerCreditId = null;
		$scope.previousCustomerCredit = customerCreditId;
	});

	$scope.kubikProductCard = new KubikProductCard();
	
	$scope.loadCustomerCredit();
});