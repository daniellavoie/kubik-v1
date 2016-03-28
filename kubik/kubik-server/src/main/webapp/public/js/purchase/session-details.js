(function(){
	var sessionId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("SessionDetailsCtrl", SessionDetailsCtrl);

	function SessionDetailsCtrl(productService, $scope, $http, $timeout, $controller){
		var vm = this;
		
		vm.search = {};
		
		loadSession();
		
		vm.addOneProduct = addOneProduct;
		vm.cancelSession = cancelSession;
		vm.closeReferenceSearchModal = closeReferenceSearchModal;
		vm.confirmCancelSession = confirmCancelSession;
		vm.confirmSubmitSession = confirmSubmitSession;
		vm.createDetail = createDetail;
		vm.createProductFromReference = createProductFromReference;
		vm.deleteDetail = deleteDetail;
		vm.getDetail = getDetail;
		vm.loadSession = loadSession;
		vm.openProductCard = openProductCard;
		vm.openProductSearch = openProductSearch;
		vm.openReferenceCard = openReferenceCard;
		vm.openReferenceSearch = openReferenceSearch;
		vm.quantityChanged = quantityChanged;
		vm.redirectToPurchaseOrders = redirectToPurchaseOrders;
		vm.redirectToPurchaseSessions = redirectToPurchaseSessions;
		vm.saveSession = saveSession;
		vm.searchProduct = searchProduct;
		vm.searchProductKeyUp = searchProductKeyUp;
		vm.searchReference = searchReference;
		vm.submitSession = submitSession;
		
		$scope.$on("productSelected", function(event, product){
			vm.addOneProduct(product);
		});
		
		$scope.$on("referenceSelected", function(event, reference){
			vm.createProductFromReference(reference);
			
			vm.closeReferenceSearchModal();
		});
			
		function addOneProduct(product){
			var detail = vm.getDetail(product);
			if(detail != null){
				detail.quantity += 1;
				vm.saveSession();
			}else{
				vm.createDetail(product, 1)
			}
			
			$("html, body").animate({ scrollTop: $(document).height() }, 500);
		}
		
		function cancelSession(){
			vm.session.status = "CANCELED";
			
			vm.saveSession(function(){
				$(".confirm-cancel").modal("hide");
				
				$(".redirection-modal").modal();
			});
		}
		
		function closeReferenceSearchModal(){
			$scope.$broadcast("closeReferenceSearchModal");
		}
		
		function confirmCancelSession(){
			$(".confirm-cancel").modal();
		}
		
		function confirmSubmitSession(){
			$(".confirm-submit").modal();
		}
		
		function createDetail(selectedProduct, quantity){
			vm.session.details.push({
				purchaseSession : {id : sessionId},
				product : selectedProduct,
				quantity : quantity
			});
			
			vm.saveSession();
		}
		
		function createProductFromReference(reference){
			$http.post("../reference/" + reference.id + "?createProduct").success(function(product){
				vm.addOneProduct(product);			
			});
		}
		
		function deleteDetail(sessionDetail){
			for(var detailIndex in vm.session.details){
				if(vm.session.details[detailIndex].product.id == sessionDetail.product.id){
					vm.session.details.splice(detailIndex, 1);
					
					break;
				}
			}
		}
		
		function getDetail(product){
			for(var detailIndex in vm.session.details){
				if(vm.session.details[detailIndex].product.id == product.id){
					return vm.session.details[detailIndex];
				}
			}
			
			return null;	
		}
		
		function loadSession(){
			$http.get(sessionId).success(function(session){
				vm.loading=false;
				vm.session = session;
				
				$timeout(function(){
					$(".date").each(function(index, element){
						var $element = $(element);
						
						$element.val(moment(parseInt($element.val())).format("DD/MM/YYYY")).datepicker({format : 'dd/mm/yyyy'});
					});
				});
			});
		}
		
		function openProductCard(product){
			$scope.$broadcast("openProductCard", product);
		}
		
		function openReferenceCard(reference){
			$scope.$broadcast("openReferenceCard", reference);
		}
		
		function openProductSearch(options){
			$scope.$broadcast("openProductSearchModal", options);
		}
		
		function openReferenceSearch(options){
			$scope.$broadcast("openReferenceSearchModal", options);
		}
		
		function quantityChanged($event){
			vm.inputIdToFocus = $event.target.id;
			if(vm.quantityChangedTimer != undefined) clearTimeout(vm.quantityChangedTimer);
		    
			vm.quantityChangedTimer = setTimeout(vm.saveSession, 1000);
		}
		
		function redirectToPurchaseOrders(){
			location.href = "../purchaseOrder";
		}
		
		function redirectToPurchaseSessions(){
			location.href = "../purchaseSession";		
		}
		
		function saveSession(success){
			vm.loading = true;

			vm.session.minDeliveryDate = moment(vm.session.minDeliveryDate, "DD/MM/YYYY").toDate();
			vm.session.maxDeliveryDate = moment(vm.session.maxDeliveryDate, "DD/MM/YYYY").toDate();
			
			angular.forEach(vm.session.details, cleanDetailCategory);
			
			$http.post(".", vm.session).success(sessionSaved);
			
			function cleanDetailCategory(detail, index){
				detail.product.category = null;
			}
			
			function sessionSaved(session){
				vm.session = session;
				
				$scope.$broadcast("sessionSaved");
				
				vm.loadSession();
				
				if(success != undefined){
					success();
				}
			}
		}
		
		function searchProduct(){
			vm.search.typedEan13 = vm.search.ean13;
			
			if(vm.search.typedEan13 != "" && !vm.searchInProgress){
				vm.searchInProgress = true;
				
				productService
					.findByEan13(vm.search.typedEan13)
					.then(findByEan13Success)
					.finally(findByEan13Completed);
				
				vm.search.ean13 = "";
				
				function findByEan13Completed(){
					vm.searchInProgress = false;
				}
				
				function findByEan13Success(product){
					// Check if a product was found
					if(product == undefined || product == ""){
						vm.searchReference();
					}else {
						$(".product-not-found").modal("hide");
						
						vm.addOneProduct(product);
					}
				}
			}
		}
		
		function searchProductKeyUp($event){
			if($event.keyCode == 13){
				vm.ean13 = this.ean13;
				
				vm.searchProduct();
			}
		}
		
		function searchReference(){
			vm.searchInProgress = true;
			$http.get("../reference?search&query=" + vm.search.typedEan13).success(function(referencesPage){
				// Check if a product was found
				if(referencesPage.content.length == 0){
					// Show a warning explaining that the product does not exists.
					$(".product-not-found").modal();
				}else {
					$(".product-not-found").modal("hide");
					
					if (referencesPage.content.length > 1){
						vm.openReferenceSearch({query : vm.search.typedEan13});
					}else{
						vm.createProductFromReference(referencesPage.content[0]);
					}
				}
			}).finally(function(){
				vm.searchInProgress = false;
			});
		}
		
		function submitSession(){
			vm.session.status = "SUBMITED";
			
			vm.saveSession(sessionSaved);
			
			function sessionSaved(){
				$(".confirm-submit").modal("hide");
				
				$(".redirection-modal").modal();
			}
		}
	}
})();