var app = angular.module("KubikPurchaseOrderDetails", []);
var orderId = window.location.pathname.split("/")[2];

app.controller("KubikPurchaseOrderDetailsController", function($scope, $http, $timeout){
	$scope.$on("searchProductClicked", function(event, product){
		$scope.addProductToValidate({products : {products : [product]}});
	});
	
	$scope.$on("addProductSearch", function(event, productSearch){
		var product = productSearch.products[0];
		
		var detail = $scope.getDetail(product);
		if(detail != null){
			detail.quantity += 1;
			$scope.saveOrder();
		}else{
			productSearch.newProductQuantity = 1;

			if(productSearch.products.length > 1){
				productSearch.selectedProduct = product;
				$scope.productsToValidate[product.ean13] = productSearch;
			}else{
				$scope.createDetail(product, productSearch.newProductQuantity)
			}
		}
	});
	
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
				id : selectedProduct.id,
				ean13 : selectedProduct.ean13,
				supplier : {ean13 : selectedProduct.supplier.ean13, id : selectedProduct.supplier.id}
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
				
				$scope.saveOrder();
				
				break;
			}
		}
	};
	
	$scope.getDetail = function(product){
		for(var detailIndex in $scope.order.details){
			if($scope.order.details[detailIndex].product.ean13 == product.ean13){
				return $scope.order.details[detailIndex];
			}
		}
		
		return null;	
	};
	
	$scope.loadOrder = function(){
		$http.get(orderId).success(function(order){
			$scope.order = order;

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
	    
		$scope.quantityChangedTimer = setTimeout($scope.saveOrder, 1000);
	};
	
	$scope.redirectToPurchaseOrders = function(){
		location.href = "../purchaseOrder";
	};
	
	$scope.redirectToReceptions = function(){
		location.href = "../reception";		
	};
	
	$scope.saveOrder = function(success){
		$scope.loading=true;
		$http.post(".", $scope.order).success(function(){
			$scope.loading=false;
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