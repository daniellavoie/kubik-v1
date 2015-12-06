(function(){
	var orderId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("PurchaseOrderDetailsCtrl", PurchaseOrderDetailsCtrl);
	
	function PurchaseOrderDetailsCtrl($scope, $http, $timeout, $controller){
		var vm = this;
		
		var productCardCtrl = $controller("ProductCardCtrl", {$scope : $scope});
		var productSearchCtrl = $controller("ProductSearchCtrl", {$scope : $scope});

		vm.search = {};
				
		loadOrder();
		
		vm.addOneProduct = addOneProduct;
		vm.calculateOrderQuantity = calculateOrderQuantity;
		vm.cancelOrder = cancelOrder;
		vm.confirmCancelOrder = confirmCancelOrder;
		vm.confirmSubmitOrder = confirmSubmitOrder;
		vm.createDetail = createDetail;
		vm.deleteDetail = deleteDetail;
		vm.getDetail = getDetail;
		vm.loadOrder = loadOrder;
		vm.openProductCard = openProductCard;
		vm.openProductSearch = openProductSearch;
		vm.openSupplierCard = openSupplierCard;
		vm.orderChanged = orderChanged;
		vm.redirectToPurchaseOrders = redirectToPurchaseOrders;
		vm.redirectToReceptions = redirectToReceptions;
		vm.saveOrder = saveOrder;
		vm.submitOrder = submitOrder;
		vm.submitOrderAndSendToDilicom = submitOrderAndSendToDilicom;
		vm.searchProduct = searchProduct;
		vm.searchProductKeyUp = searchProductKeyUp;
		vm.showOrderForm = showOrderForm;
		
		$scope.$on("productSelected", function(event, product) {
			vm.addOneProduct(product);
			
			productSearchCtrl.closeModal();
		});
		
		$scope.$on("supplierSaved", function(event, supplier){
			vm.loadOrder();
		});
		
		function addOneProduct(product){
			if(product.supplier.id != vm.order.supplier.id){
				vm.supplierNotMatching = product.supplier;
				
				$(".supplier-not-matching").modal();
			}else{
				var detail = vm.getDetail(product);
				if(detail != null){
					detail.quantity += 1;
					vm.saveSession();
				}else{
					vm.createDetail(product, 1)
				}
				
				$("html, body").animate({ scrollTop: $(document).height() }, 500);
			}
		}
		
		function calculateOrderQuantity(order){
			var quantity = 0;
			if(order != undefined){
				for(var detailIndex in order.details){
					var detail = order.details[detailIndex];
					
					quantity += detail.quantity;
				}
			}
			return quantity;
		}
		
		function cancelOrder(){
			vm.order.status = "CANCELED";
			
			vm.saveOrder(function(){
				$(".confirm-cancel").modal("hide");
				
				$(".redirection-modal").modal();
			});
		}
		
		function confirmCancelOrder(){
			$(".confirm-cancel").modal();
		}
		
		function confirmSubmitOrder (){
			$(".confirm-submit").modal();
		}
		
		function createDetail(selectedProduct, quantity){
			vm.order.details.push({
				purchaseOrder : {id : orderId},
				product : selectedProduct,
				quantity : quantity
			});
			
			vm.saveOrder();
		};
		
		function deleteDetail(orderDetail){
			for(var detailIndex in vm.order.details){
				if(vm.order.details[detailIndex].product.id == orderDetail.product.id){
					vm.order.details.splice(detailIndex, 1);
					
					vm.saveOrder();
					
					break;
				}
			}
		}
		
		function getDetail(product){
			for(var detailIndex in vm.order.details){
				if(vm.order.details[detailIndex].product.id == product.id){
					return vm.order.details[detailIndex];
				}
			}
			
			return null;	
		}
		
		function loadOrder(){
			$http.get(orderId).success(function(order){
				vm.order = order;

				$timeout(function(){
					if(vm.inputIdToFocus != undefined){
						$("#" + vm.inputIdToFocus).focus();
					}
				})
			});
		}
		
		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
		
		function openProductSearch(){
			$scope.$broadcast("openProductSearchModal");
		}
		
		function openSupplierCard($event, supplier){
			$event.stopPropagation();
			
			$scope.$broadcast("openSupplierCard", supplier);
		}
		
		function orderChanged($event){
			vm.inputIdToFocus = $event.target.id;
			if(vm.quantityChangedTimer != undefined) clearTimeout(vm.quantityChangedTimer);
		    
			vm.quantityChangedTimer = setTimeout(vm.saveOrder, 1000);
		}
		
		function redirectToPurchaseOrders(){
			location.href = "../purchaseOrder";
		}
		
		function redirectToReceptions(){
			location.href = "../reception";		
		}
		
		function saveOrder(success){
			vm.loading=true;
			
			angular.forEach(vm.order.details, cleanDetailCategory);
			
			$http.post(".", vm.order).success(orderSaved);
			
			function cleanDetailCategory(detail, index){
				detail.product.category = null;
			}
			
			function orderSaved(order){
				vm.order = order;
				vm.loading = false;
				
				vm.loadOrder();
				
				if(success != undefined){
					success();
				}
			}
		}
		
		function submitOrder(){
			vm.order.status = "SUBMITED";
			
			vm.saveOrder(orderSaved);
			
			function orderSaved(){
				$(".confirm-submit").modal("hide");
				
				$(".redirection-modal").modal();
			}
		}
		
		function submitOrderAndSendToDilicom(){
			vm.order.dilicomOrder = {creationDate : new Date(), status : "PENDING", purchaseOrder : {id : vm.order.id}};
			
			vm.submitOrder();
		}
		
		function searchProduct(){
			vm.search.typedEan13 = vm.search.ean13;
			
			if(vm.typedEan13 != "" && !vm.searchInProgress){
				vm.searchInProgress = true;
				
				$http.get("../product?ean13=" + vm.search.typedEan13).success(handleResults).finally(searchCompleted);

				vm.search.ean13 = "";
			}
			
			function handleResults(products){
				if(products.length == 0){
					$(".product-not-found").modal();
				}else {
					$(".product-not-found").modal("hide");
					
					if (products.length > 1){
						vm.kubikProductSearch.openSearchModal(vm.search.typedEan13);
					}else{
						vm.addOneProduct(products[0]);
					}
				}
			}
			
			function searchCompleted(){
				vm.searchInProgress = false;
			}
		}
		
		function searchProductKeyUp($event){
			if($event.keyCode == 13){
				vm.ean13 = this.ean13;
				
				vm.searchProduct();
			}
		}
		
		function showOrderForm(){
			window.open(vm.order.id + "/report", "Bon de commande", "pdf");
		}
	}
})();