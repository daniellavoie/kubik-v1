(function(){
	var customerCreditId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("CustomerCreditDetailsCtrl", CustomerCreditDetailsCtrl);
	
	function CustomerCreditDetailsCtrl($scope, $http, $timeout){
		var vm = this;
		
		vm.addProduct = addProduct;
		vm.cancel = cancel;
		vm.changeCustomerCredit = changeCustomerCredit;
		vm.changePaymentMethod = changePaymentMethod;
		vm.focus = focus;
		vm.hideAlerts = hideAlerts;
		vm.hideLoading = hideLoading;
		vm.loadCustomerCredit = loadCustomerCredit;
		vm.loadNextCustomerCredit = loadNextCustomerCredit;
		vm.loadPreviousCustomerCredit = loadPreviousCustomerCredit;
		vm.openInvoice = openInvoice;
		vm.openCustomerCard = openCustomerCard;
		vm.openProductCard = openProductCard;
		vm.showLoading = showLoading;
		vm.quantityChanged = quantityChanged;
		vm.saveCustomerCredit = saveCustomerCredit;
		vm.showProductNotFoundAlert = showProductNotFoundAlert;
		vm.validate = validate;
		
		loadCustomerCredit();
		loadNextCustomerCredit();
		loadPreviousCustomerCredit();
		
		$scope.$on("customerSaved", function($event, customer){
			vm.loadCustomerCredit();
		});
		
		function addProduct($event){
			if($event.keyCode == 13){
				vm.showLoading();
				detailFound = false;
				for(var detailIndex in vm.customerCredit.details){
					var detail = vm.customerCredit.details[detailIndex];
					
					if(detail.product.ean13 == vm.detail.product.ean13){
						detailFound = true;
						
						detail.quantity += 1;
						vm.saveCustomerCredit();
						vm.detail.product.ean13 = "";
						
						break;
					}
				}
				
				if(!detailFound){
					var url = "../invoice/" + vm.customerCredit.invoice.id + "/detail/product/ean13/" + vm.detail.product.ean13;
					
					$http.get(url)
						.success(searchInvoiceForProductSuccess)
						.error(searchInvoiceForProductError);
				}
			}
			
			function searchInvoiceForProductSuccess(invoiceDetail){
				if(invoiceDetail != ""){
					vm.customerCredit.details.push({customerCredit : {id : vm.customerCredit.id}, product : invoiceDetail.product, quantity : 1, maxQuantity : invoiceDetail.quantity});
					
					vm.saveCustomerCredit();
				} else {
					searchInvoiceForProductError();
				}
			}
			
			function searchInvoiceForProductError(){
				vm.hideLoading();
				vm.showProductNotFoundAlert();
			}
		}
		
		function cancel(){
			vm.customerCredit.status = 'CANCELED';

			vm.saveCustomerCredit();
		}
		
		function changeCustomerCredit(customerCreditId){
			location.href = customerCreditId;
		}

		function changePaymentMethod(paymentMethodType){
			vm.customerCredit.paymentMethod.type = paymentMethodType;
			
			vm.saveCustomerCredit();
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
		
		function loadCustomerCredit(){
			$http.get(customerCreditId).success(function(customerCredit){
				vm.customerCredit = customerCredit;
				
				if(vm.inputIdToFocus != undefined){
					$timeout(function(){$("#" + vm.inputIdToFocus).focus()});
				}
			});
		}
		
		function loadNextCustomerCredit(){
			$http.get(customerCreditId + "/next").success(function(nextCustomerCreditId){
				if(nextCustomerCreditId == "") nextCustomerCreditId = null;
				vm.nextCustomerCredit = nextCustomerCreditId;
			});	
		}
		
		function loadPreviousCustomerCredit(){
			$http.get(customerCreditId + "/previous").success(function(previousCustomerCreditId){
				if(previousCustomerCreditId == "") previousCustomerCreditId = null;
				vm.previousCustomerCredit = previousCustomerCreditId;
			});	
		}
		
		function openInvoice(){
			location.href = "../invoice/" + vm.customerCredit.invoice.id;
		}
		
		function openCustomerCard($event, customer){
			$event.stopPropagation();
			
			$scope.$broadcast("openCustomerCard", customer);
		}
		
		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
		
		function showLoading(){
			vm.hideAlerts();
			
			$(".loading").removeClass("hidden");
		}

		function quantityChanged(){
			if(vm.quantityChangedTimer != undefined) clearTimeout(vm.quantityChangedTimer);
		    
			vm.quantityChangedTimer = setTimeout(vm.saveCustomerCredit, 1000);
		}
		
		function saveCustomerCredit(){
			vm.showLoading();
			
			angular.forEach(vm.customerCredit.details, cleanDetail);
			
			vm.customerCredit.invoice = {id : vm.customerCredit.invoice.id};
			
			$http.post(".", vm.customerCredit).finally(saveCustomerCreditCompleted);
			
			function saveCustomerCreditCompleted(){
				vm.hideLoading();
				
				vm.loadCustomerCredit();
			}
			
			function cleanDetail(detail, index){
				detail.product.category = null;
			}
		}
		
		function showProductNotFoundAlert(){
			$(".product-not-found").removeClass("hidden");
		}
		
		function validate(){
			vm.customerCredit.status = "COMPLETED";
			
			vm.saveCustomerCredit();
		}
	}
})();