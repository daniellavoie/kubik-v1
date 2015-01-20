var app = angular.module("KubikPurchaseOrderDetails", []);
var orderId = window.location.pathname.split("/")[2];

app.controller("KubikPurchaseOrderDetailsController", function($scope, $http, $timeout){
	$scope.$on("searchProductClicked", function(event, product){
		$scope.addProductToValidate({products : {products : [product]}});
	});
	
	$scope.$on("addProductSearch", function(event, productSearch){
		$scope.addProductToValidate(productSearch);
	});
	
	$scope.addProductToValidate = function(productSearch){
		var alreadyPresent = false;
		for(var detailIndex in $scope.order.details){
			var product = $scope.order.details[detailIndex].product;
			
			if(product.ean13 == productSearch.products[0].ean13){
				alreadyPresent = true;
			}
		}
		
		if(!alreadyPresent){
			productSearch.newProductQuantity = 1;
			productSearch.selectedProduct = productSearch.products[0];
			$scope.productsToValidate[productSearch.selectedProduct.ean13] = productSearch;
		}
	};
	
	$scope.cancelOrder = function(){
		$scope.order.status = "CANCELED";
		
		$scope.saveOrder(function(){
			$(".confirm-cancel").modal("hide");
			
			$(".redirection-modal").modal();
		});
	};
	
	$scope.confirmCancelOrder = function(){
		$(".confirm-cancel").modal();
	};
	
	$scope.confirmSubmitOrder = function(){
		$(".confirm-submit").modal();
	};
	
	$scope.createDetail = function(selectedProduct, quantity){
		$scope.order.details.push({
			purchaseOrder : {id : orderId},
			product : {
				ean13 : selectedProduct.ean13,
				supplier : {id : selectedProduct.supplier.id}
			},
			quantity : quantity
		});
		
		delete $scope.productsToValidate[selectedProduct.ean13];
		
		$scope.saveOrder();
	};
	
	$scope.deleteDetail = function(orderDetail){
		for(var detailIndex in $scope.order.details){
			if($scope.order.details[detailIndex].product.ean13 == orderDetail.product.ean13){
				$scope.order.details.splice(detailIndex, 1);
				
				break;
			}
		}
	};
	
	$scope.loadOrder = function(){
		$http.get(orderId).success(function(order){
			$scope.order = order;
		});
	};
	
	$scope.redirectToPurchaseOrders = function(){
		location.href = "../purchaseOrder";
	};
	
	$scope.redirectToReceptions = function(){
		location.href = "../reception";		
	};
	
	$scope.saveOrder = function(success){
		for(var detailIndex in $scope.order.details){
			var detail = $scope.order.details[detailIndex];
						
			detail.product = {
				id : detail.product.id,
				ean13 : detail.product.ean13,
				supplier : {id : detail.product.supplier.id}
			};
		}
		$http.post(".", $scope.order).success(function(){
			$scope.$broadcast("orderSaved");
			
			$scope.loadOrder();
			
			if(success != undefined){
				success();
			}
		});
	};
	
	$scope.submitOrder = function(){
		$scope.order.status = "SUBMITED";
		
		$scope.saveOrder(function(){
			$(".confirm-submit").modal("hide");
			
			$(".redirection-modal").modal();
		});
	};
	
	$scope.submitOrderAndSendToDilicom = function(){
		$scope.order.sentToDilicom = true;
		
		$scope.submitOrder();
	}

	$scope.kubikProductCard = new KubikProductCard();
	
	$scope.productsToValidate = {};
	$scope.loadOrder();
});

app.controller("NewProductController", function($scope, $http, $timeout){
	$scope.$on("referenceSelectedFromSearch", function(event, reference){
		$http.get("../productSearch/" + reference.ean13 + "/" + reference.supplierEan13).success(function(productSearch){
			if(productSearch.products.length > 0){
				$scope.$emit("addProductSearch", productSearch);
			}
		});
	});
	
	$scope.changeSelectedProduct = function(){
		for(productIndex in $scope.productSearch.products){
			var product = $scope.productSearch.products[productIndex];
			
			if(product.supplier.ean13 == $(".new-product select.supplier").val()){
				$scope.selectedProduct = product;
			}
		}
	};
	
	$scope.keyup = function($event){
		if($event.keyCode == 13){
			$http.get("../productSearch/" + $(".ean13").val()).success(function(productSearch){
				if(productSearch.products.length > 0){
					$scope.$emit("addProductSearch", productSearch);
					$(".ean13").val("");
				}
			});
		}
	};
});

app.controller("ProductSelectorController", function($scope){
	$scope.displayCsvSelector = function(){
		$scope.$selectorEan13Content.addClass("hidden");
		$scope.$selectorCsvContent.removeClass("hidden");
		$scope.$selectorButton.html("Csv");
	};
	
	$scope.displayEan13Selector = function(){
		$scope.$selectorEan13Content.removeClass("hidden");
		$scope.$selectorCsvContent.addClass("hidden");			
		$scope.$selectorButton.html("Ean13");
	};
	
	$scope.openSearchReference = function(){
		$scope.$selectorEan13Content.addClass("hidden");
		$scope.$selectorCsvContent.addClass("hidden");	
		$scope.kubikReferenceSearch.openSearch();			
		$scope.$selectorButton.html("Recherche");
	};

	$scope.$selectorContent = $(".selector .selector-content");
	$scope.$selectorCsvContent = $scope.$selectorContent.find(".csv");  
	$scope.$selectorEan13Content = $scope.$selectorContent.find(".ean13");  
	$scope.$selectorButton = $(".selector .selector-button-label");
	
	// Initialise SearchReferenceController
	$scope.kubikReferenceSearch = new KubikReferenceSearch({
		searchUrl : "../../reference",
		searchResultUrl : "../../reference",
		$modalContainer : $(".search-reference"),
		referenceClicked : function(reference){
			$scope.$emit("referenceSelectedFromSearch", reference);
		}
	});
});