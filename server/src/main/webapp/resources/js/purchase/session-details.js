var app = angular.module("KubikPurchaseSessionDetails", []);
var sessionId = window.location.pathname.split("/")[2];

var kubikProductSearch = new KubikProductSearch({
	app : app,
	modal : $(".products-modal"),
	productUrl : "../product"
});

var kubikReferenceSearch = new KubikReferenceSearch({
	app : app,
	modal : $(".references-modal"),
	referenceUrl : "../reference"
});

app.controller("SessionDetailsController", function($scope, $http, $timeout){
	$scope.$on("openProductCard", function(event, product){
		$scope.kubikProductCard.openCard(product);
	});
	
	$scope.$on("openReferenceCard", function(event, reference){
		$scope.kubikReferenceCard.openCard(reference);
	});
		
	$scope.addOneProduct = function(product){
		var detail = $scope.getDetail(product);
		if(detail != null){
			detail.quantity += 1;
			$scope.saveSession();
		}else{
			$scope.createDetail(product, 1)
		}
		
		$("html, body").animate({ scrollTop: $(document).height() }, 500);
	};	
	
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
			product : selectedProduct,
			quantity : quantity
		});
		
		$scope.saveSession();
	};
	
	$scope.createProductFromReference = function(reference){
		$http.post("../reference/" + reference.id + "?createProduct").success(function(product){
			$scope.addOneProduct(product);			
		});
	};
	
	$scope.deleteDetail = function(sessionDetail){
		for(var detailIndex in $scope.session.details){
			if($scope.session.details[detailIndex].product.id == sessionDetail.product.id){
				$scope.session.details.splice(detailIndex, 1);
				
				break;
			}
		}
	};
	
	$scope.getDetail = function(product){
		for(var detailIndex in $scope.session.details){
			if($scope.session.details[detailIndex].product.id == product.id){
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
		$scope.loading = true;

		angular.forEach($scope.session.details, cleanDetailCategory);
		
		$http.post(".", $scope.session).success(sessionSaved);
		
		function cleanDetailCategory(detail, index){
			detail.product.category = null;
		}
		
		function sessionSaved(session){
			$scope.session = session;
			
			$scope.$broadcast("sessionSaved");
			
			$scope.loadSession();
			
			if(success != undefined){
				success();
			}
		}
	};
	
	$scope.searchProduct = function(){
		$scope.search.typedEan13 = $scope.search.ean13;
		
		if($scope.search.typedEan13 != "" && !$scope.searchInProgress){
			$scope.searchInProgress = true;
			$http.get("../product?ean13=" + $scope.search.typedEan13).success(function(products){
				// Check if a product was found
				if(products.length == 0){
					$scope.searchReference();
				}else {
					$(".product-not-found").modal("hide");
					
					if (products.length > 1){
						$scope.kubikProductSearch.kubikProductSearch.openSearchModal($scope.search.typedEan13);
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
	
	$scope.searchReference = function(){
		$scope.searchInProgress = true;
		$http.get("../reference?search&query=" + $scope.typedEan13).success(function(referencesPage){
			// Check if a product was found
			if(referencesPage.content.length == 0){
				// Show a warning explaining that the product does not exists.
				$(".product-not-found").modal();
			}else {
				$(".product-not-found").modal("hide");
				
				if (referencesPage.content.length > 1){
					$scope.kubikReferenceSearch.openSearchModal($scope.typedEan13);
				}else{
					$scope.createProductFromReference(referencesPage.content[0]);
				}
			}
		}).finally(function(){
			$scope.searchInProgress = false;
		});
	};
	
	$scope.submitSession = function(){
		$scope.session.status = "SUBMITED";
		
		$scope.saveSession(function(){
			$(".confirm-submit").modal("hide");
			
			$(".redirection-modal").modal();
		});
	};

	$scope.kubikReferenceCard = new KubikReferenceCard();
	
	$scope.kubikProductSearch = kubikProductSearch;
	$scope.kubikProductCard = kubikProductSearch.kubikProductCard;
	$scope.kubikProductSearch.productSelected = function(product){
		$scope.addOneProduct(product);
		
		$scope.kubikProductSearch.closeSearchModal();
	};
	
	$scope.kubikReferenceSearch = kubikReferenceSearch;
	$scope.kubikReferenceSearch.referenceSelected = function(reference){
		$scope.createProductFromReference(reference);
		
		$scope.kubikReferenceSearch.closeSearchModal();
	};

	$scope.search = {};
	$scope.loadSession();
});