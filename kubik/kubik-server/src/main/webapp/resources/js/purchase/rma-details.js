(function(){
	var rmaId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("RmaDetailsCtrl", RmaDetailsCtrl);
	
	function RmaDetailsCtrl($scope, $http, $timeout){
		var vm = this;
		
		vm.error = {};
		vm.detail = {product : {}};
		
		vm.addProduct = addProduct;
		vm.cancel = cancel;
		vm.changeRma = changeRma;
		vm.changePaymentMethod = changePaymentMethod;
		vm.focus = focus;
		vm.hideAlerts = hideAlerts;
		vm.hideLoading = hideLoading;
		vm.loadNextRma = loadNextRma;
		vm.loadRma = loadRma;
		vm.loadPreviousRma = loadPreviousRma;
		vm.openProductCard = openProductCard;
		vm.openSupplierCard = openSupplierCard;
		vm.print = print; 
		vm.rmaChanged = rmaChanged;
		vm.saveRma = saveRma;
		vm.showLoading = showLoading;
		vm.showProductNotFoundAlert = showProductNotFoundAlert;
		vm.validate = validate;
		
		loadRma();
		loadNextRma();
		loadPreviousRma();
		
		$scope.$on("supplierSaved", function($event, supplier){
			vm.loadRma();
		});
		
		function addProduct($event){
			vm.error.productNotFound = false;
			
			if($event.keyCode == 13){
				vm.showLoading();
				detailFound = false;
				for(var detailIndex in vm.rma.details){
					var detail = vm.rma.details[detailIndex];
					
					if(detail.product.ean13 == vm.detail.product.ean13){
						detailFound = true;
						
						detail.quantity += 1;
						vm.saveRma();
						vm.detail.product.ean13 = ""
						break;
					}
				}
				
				if(!detailFound){
					var url = "../product?ean13=" + vm.detail.product.ean13 + "&supplierEan13=" + vm.rma.supplier.ean13
					
					$http.get(url)
						.success(productLoadSuccess)
						.error(productLoadOnError)
						.finally(productLoadCompleted);
				}
				
				$("#product-ean13").select();
			}
			
			function productLoadSuccess(product){
				if(product != ""){
					vm.rma.details.push({product : product, quantity : 1, rma : {id : vm.rma.id}});
					
					vm.saveRma();
				}else{
					vm.error.productNotFound = true;
				}
			}
			
			function productLoadOnError(){
				vm.error.productNotFound = true;
			}
			
			function productLoadCompleted(){
				vm.hideLoading();
			}
		}
		
		function cancel(){
			vm.rma.status = 'CANCELED';
			
			vm.saveRma();
		}
		
		function changeRma(rmaId){
			location.href = rmaId;
		}
		
		function changePaymentMethod(paymentMethodType){
			vm.rma.paymentMethod.type = paymentMethodType;
			
			vm.saveRma();
		}
		
		function focus($event){
			vm.inputIdToFocus = $event.target.id;
		}

		function hideAlerts(){
			$(".alerts .alert").addClass("hidden");
		}

		function hideLoading(){
			$(".loading").addClass("hidden");
		}
		
		function loadNextRma(){
			$http.get(rmaId + "/next").success(nextRmaLoadedSuccess);
			
			function nextRmaLoadedSuccess(nextRmaId){
				if(nextRmaId == "") nextRmaId = null;
				vm.nextRma = nextRmaId;
			}
		}
		
		function loadPreviousRma(){
			$http.get(rmaId + "/previous").success(previousRmaLoadedSuccess);
			
			function previousRmaLoadedSuccess(previousRmaId){
				if(previousRmaId == "") previousRmaId = null;
				vm.previousRma = previousRmaId;
			}
		}
		
		function loadRma(){
			$http.get(rmaId).success(rmaLoaded);
			
			function rmaLoaded(rma){
				vm.rma = rma;
				
				if(vm.inputIdToFocus != undefined){
					$timeout(function(){
						$("#" + vm.inputIdToFocus).focus()
					});
				}
			}
		}
		
		function openProductCard($event, product) {
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}

		function openSupplierCard($event, supplier){
			$event.stopPropagation();

			$scope.$broadcast("openSupplierCard", supplier);
		}
		
		function print(){
			window.open(vm.rma.id + "/report", "Avis de retour fournisseur", "pdf");
		}

		function rmaChanged(){
			if(vm.rmaChangedTimer != undefined) clearTimeout(vm.rmaChangedTimer);
		    
			vm.rmaChangedTimer = setTimeout(vm.saveRma, 1000);
		}

		function saveRma(){
			vm.showLoading();
			
			angular.forEach(vm.rma.details, cleanDetailCategory);
			
			$http.post(".", vm.rma).finally(saveCompleted);
			
			function cleanDetailCategory(detail, index){
				detail.product.category = null;
			}
			
			function saveCompleted(){
				vm.hideLoading();
				
				vm.loadRma();
			}
		}

		function showLoading(){
			vm.hideAlerts();
			
			$(".loading").removeClass("hidden");
		}

		function showProductNotFoundAlert(){
			$(".product-not-found").removeClass("hidden");
		}
		
		function validate(){
			vm.rma.status = "SHIPPED";
			
			vm.saveRma();
		}
	}
})();