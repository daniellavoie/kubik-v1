var app = angular.module("KubikPurchaseOrderDetails", []);
var orderId = window.location.pathname.split("/")[2];

var kubikProductSearch = new KubikProductSearch({
	app : app,
	modal : $(".products-modal"),
	productUrl : "../product"
});

app.controller("KubikPurchaseOrderDetailsController", function($scope, $http, $timeout){	

	$scope.$on("openProductCard", function(event, product){
		$scope.kubikProductCard.openCard(product);
	});
		
	$scope.addOneProduct = function(product){
		if(product.supplier.id != $scope.order.supplier.id){
			$scope.supplierNotMatching = product.supplier;
			
			$(".supplier-not-matching").modal();
		}else{
			var detail = $scope.getDetail(product);
			if(detail != null){
				detail.quantity += 1;
				$scope.saveSession();
			}else{
				$scope.createDetail(product, 1)
			}
			
			$("html, body").animate({ scrollTop: $(document).height() }, 500);
		}
	};	
	
	$scope.calculateOrderQuantity = function(order){
		var quantity = 0;
		if(order != undefined){
			for(var detailIndex in order.details){
				var detail = order.details[detailIndex];
				
				quantity += detail.quantity;
			}
		}
		return quantity;
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
			product : selectedProduct,
			quantity : quantity
		});
		
		$scope.saveOrder();
	};
	
	$scope.deleteDetail = function(orderDetail){
		for(var detailIndex in $scope.order.details){
			if($scope.order.details[detailIndex].product.id == orderDetail.product.id){
				$scope.order.details.splice(detailIndex, 1);
				
				$scope.saveOrder();
				
				break;
			}
		}
	};
	
	$scope.getDetail = function(product){
		for(var detailIndex in $scope.order.details){
			if($scope.order.details[detailIndex].product.id == product.id){
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
	
	$scope.orderChanged = function($event){
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
		$scope.order.dilicomOrder = true;
		
		$scope.submitOrder();
	}

	$scope.kubikProductCard = new KubikProductCard({
		productSaved : function(){
			$scope.loadOrder();
		}, 
		productUrl : "../product"
	});

	$scope.kubikSupplierCard = new KubikSupplierCard({
		supplierSaved : function(){
			$scope.loadOrder();
		}, 
		supplierUrl : "../supplier"
	});
	
	$scope.searchProduct = function(){
		$scope.search.typedEan13 = $scope.search.ean13;
		
		if($scope.typedEan13 != "" && !$scope.searchInProgress){
			$scope.searchInProgress = true;
			$http.get("../product?ean13=" + $scope.search.typedEan13).success(function(products){
				if(products.length == 0){
					$(".product-not-found").modal();
				}else {
					$(".product-not-found").modal("hide");
					
					if (products.length > 1){
						$scope.kubikProductSearch.openSearchModal($scope.search.typedEan13);
					}else{
						$scope.addOneProduct(products[0]);
					}
				}
			}).finally(function(){
				$scope.searchInProgress = false;
			});

			$scope.search.ean13 = "";
		}
	};
	
	$scope.searchProductKeyUp = function($event){
		if($event.keyCode == 13){
			$scope.ean13 = this.ean13;
			
			$scope.searchProduct();
		}
	};

	$scope.kubikProductCard = new KubikProductCard();
	
	$scope.kubikProductSearch = kubikProductSearch;
	$scope.kubikProductSearch.productSelected = function(product){
		$scope.addOneProduct(product);
		
		$scope.kubikProductSearch.closeSearchModal();
	};
	
	$scope.search = {};
	$scope.loadOrder();
});