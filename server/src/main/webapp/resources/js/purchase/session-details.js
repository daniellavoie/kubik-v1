var app = angular.module("KubikPurchaseSessionDetails", []);
var sessionId = window.location.pathname.split("/")[2];

app.controller("SessionDetailsController", function($scope, $http, $timeout){
	$scope.$on("searchProductClicked", function(event, product){
		$scope.addProductToValidate({products : {products : [product]}});
	});
	
	$scope.$on("addProductSearch", function(event, productSearch){
		var product = productSearch.products[0];
		
		var detail = $scope.getDetail(product);
		if(detail != null){
			detail.quantity += 1;
			$scope.saveSession();
		}else{
			productSearch.newProductQuantity = 1;

			if(productSearch.products.length > 1){
				productSearch.selectedProduct = product;
				$scope.productsToValidate[product.ean13] = productSearch;
			}else{
				$scope.createDetail(product, productSearch.newProductQuantity)
			}
		}
		
		$("html, body").animate({ scrollTop: $(document).height() }, 500);
	});
	
	$scope.cancelSession = function(){
		$scope.session.status = "CANCELED";
		
		$scope.saveSession(function(){
			$(".confirm-cancel").modal("hide");
			
			$(".redirection-modal").modal();
		});
	};
	
	$scope.confirmCancelSession = function(){
		$(".confirm-cancel").modal();
	};
	
	$scope.confirmSubmitSession = function(){
		$(".confirm-submit").modal();
	};
	
	$scope.createDetail = function(selectedProduct, quantity){
		$scope.session.details.push({
			purchaseSession : {id : sessionId},
			product : {
				ean13 : selectedProduct.ean13,
				supplier : {ean13 : selectedProduct.supplier.ean13, id : selectedProduct.supplier.id}
			},
			quantity : quantity
		});
		
		delete $scope.productsToValidate[selectedProduct.ean13];
		
		$scope.saveSession();
	};
	
	$scope.deleteDetail = function(sessionDetail){
		for(var detailIndex in $scope.session.details){
			if($scope.session.details[detailIndex].product.ean13 == sessionDetail.product.ean13){
				$scope.session.details.splice(detailIndex, 1);
				
				break;
			}
		}
	};
	
	$scope.getDetail = function(product){
		for(var detailIndex in $scope.session.details){
			if($scope.session.details[detailIndex].product.ean13 == product.ean13){
				return $scope.session.details[detailIndex];
			}
		}
		
		return null;	
	};
	
	$scope.loadSession = function(){
		$http.get(sessionId).success(function(session){
			$scope.loading=false;
			$scope.session = session;
			
			$timeout(function(){
				$(".date").each(function(index, element){
					var $element = $(element);
					
					$element.val(moment(parseInt($element.val())).format("DD/MM/YYYY")).datepicker({format : 'dd/mm/yyyy'});
				});
			});
		});
	};
	
	$scope.quantityChanged = function($event){
		$scope.inputIdToFocus = $event.target.id;
		if($scope.quantityChangedTimer != undefined) clearTimeout($scope.quantityChangedTimer);
	    
		$scope.quantityChangedTimer = setTimeout($scope.saveSession, 1000);
	};
	
	$scope.redirectToPurchaseOrders = function(){
		location.href = "../purchaseOrder";
	};
	
	$scope.redirectToPurchaseSessions = function(){
		location.href = "../purchaseSession";		
	};
	
	$scope.saveSession = function(success){
		$scope.loading=true;

		$http.post(".", $scope.session).success(function(){
			$scope.$broadcast("sessionSaved");
			
			$scope.loadSession();
			
			if(success != undefined){
				success();
			}
		});
	};
	
	$scope.submitSession = function(){
		$scope.session.status = "SUBMITED";
		
		$scope.saveSession(function(){
			$(".confirm-submit").modal("hide");
			
			$(".redirection-modal").modal();
		});
	};

	$scope.kubikProductCard = new KubikProductCard();
	
	$scope.productsToValidate = {};
	$scope.loadSession();
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