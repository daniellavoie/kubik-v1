(function(){
	var INVOICE_URL = "/invoice";
	
	angular
		.module("Kubik")
		.controller("CashRegisterCtrl", CashRegisterCtrl);
	
	function CashRegisterCtrl(errorTraceService, $scope, $http, $timeout){
		var vm = this;
		
		vm.focusEan13Input = true;
		
		vm.addCustomerToInvoice = addCustomerToInvoice;
		vm.addOneProduct = addOneProduct;
		vm.cancelInvoice = cancelInvoice;
		vm.changeRebateType = changeRebateType;
		vm.checkoutInvoice = checkoutInvoice;
		vm.confirmCancelInvoice = confirmCancelInvoice;
		vm.invoiceChanged = invoiceChanged;
		vm.isSelectedInvoice = isSelectedInvoice;
		vm.loadInvoices = loadInvoices;
		vm.lookupInvoice = lookupInvoice;
		vm.newInvoice = newInvoice;
		vm.openCustomerCard = openCustomerCard;
		vm.openCustomerSearch = openCustomerSearch;
		vm.openProductCard = openProductCard;
		vm.openProductSearch = openProductSearch;
		vm.refreshInvoices = refreshInvoices;
		vm.removeCustomerFromInvoice = removeCustomerFromInvoice;
		vm.removeInvoiceDetail = removeInvoiceDetail;
		vm.saveInvoice = saveInvoice;
		vm.searchProductKeyUp = searchProductKeyUp;
		vm.searchProduct = searchProduct;
		vm.showInvoice = showInvoice;
		vm.warnInvoiceClosed = warnInvoiceClosed;
		
		refreshInvoices();
		$(".ean13-input").focus();
		
		$scope.$on("customerSaved", function($event, customer){
			vm.addCustomerToInvoice(customer);
		});
		
		$scope.$on("customerSelected", function($event, customer){
			vm.addCustomerToInvoice(customer);
		});
		
		$scope.$on("productSelected", function($event, product){
			vm.addOneProduct(product);
		});
		
		$scope.$on("saleOver", function($event){
			vm.invoice = null;
			
			vm.refreshInvoices();
		});
		
		function addCustomerToInvoice(customer){
			vm.invoice.customer = customer;
			
			vm.saveInvoice();

			$scope.$broadcast("closeCustomerCard");
			$scope.$broadcast("closeCustomerSearchModal");
		}
		
		function addOneProduct(product){
			// Checks if the product is already found within the invoice.
			var productInInvoice = false;
			
			for(var detailIndex in vm.invoice.details){
				var detail = vm.invoice.details[detailIndex];
				
				if(detail.product.ean13 == product.ean13 && detail.product.supplier.ean13 == product.supplier.ean13){
					detail.quantity += 1;
					productInInvoice = true;
					
					break;
				}
			}
			
			if(!productInInvoice){
				vm.invoice.details.push({
					invoice : {id : vm.invoice.id},
					product : {
						id : product.id,
						ean13 : product.ean13,
						supplier : {
							id : product.supplier.id
						}
					},
					quantity : 1
				});	
			}
			
			vm.saveInvoice();
		}
		
		function cancelInvoice(){
			vm.invoice.status = {type : "CANCELED"};
			
			vm.saveInvoice();
			
			$(".confirm-cancel").modal("hide");
		}
		
		function changeRebateType(rebateType){
			vm.invoice.rebateType = rebateType;
			
			vm.saveInvoice();
		}
		
		function checkoutInvoice(){
			vm.saveInvoice(saveInvoiceSuccess);
			
			function saveInvoiceSuccess(){
				$scope.$broadcast("checkoutInvoice", vm.invoice)
			}
		}
		
		function confirmCancelInvoice(){
			$(".confirm-cancel").modal({
				backdrop : "static",
				keyboard : "false"
			});
		}
		
		function invoiceChanged($event){
			vm.inputIdToFocus = $event.target.id;
			if(vm.invoiceChangedTimer != undefined) clearTimeout(vm.invoiceChangedTimer);
		    
			vm.invoiceChangedTimer = setTimeout(vm.saveInvoice, 1000);
		}
		
		function isSelectedInvoice(invoice){
			if(vm.invoice == undefined){
				return false;
			}
			
			return vm.invoice.id == invoice.id;
		}
		
		function loadInvoices(callbackFn){
			$http.get("cashRegister/session/invoice").success(callbackFn);
		}

		function lookupInvoice(invoice, invoices){
			for(var invoiceIndex in invoices){
				var invoice = invoices[invoiceIndex];
				
				if(invoice.id == vm.invoice.id){
					return invoice;
				}
			}
			
			return null;
		}

		function newInvoice(){
			$http.get("cashRegister/session/invoice?new").success(newInvoiceLoadSuccess);
			
			function newInvoiceLoadSuccess(invoice){
				vm.invoice = invoice;
				
				vm.refreshInvoices();
			}
		}
		
		function openCustomerCard($event, customer){
			$event.stopPropagation();
			
			$scope.$broadcast("openCustomerCard", customer);
		}
		
		function openCustomerSearch($event, customer){
			$event.stopPropagation();
			
			$scope.$broadcast("openCustomerSearchModal");
		}
		
		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
		
		function openProductSearch($event){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductSearchModal");
		}
		
		function refreshInvoices(callbackFn){
			vm.loadInvoices(function(invoices){
				vm.invoices = invoices;
				
				vm.searchInProgress = false;
				
				
				if(vm.invoice == null || vm.invoice.status.type == "CANCELED" || vm.invoice.status.type == "PAID"){
					vm.showInvoice(invoices[0]);
				}else{
					var invoice = vm.lookupInvoice(vm.invoice, invoices);
					
					if(invoice == null){
						vm.warnInvoiceClosed();
						vm.showInvoice(invoices[0]);
					}else{
						vm.showInvoice(invoice);
					}
				}
				
				if(callbackFn != undefined){
					callbackFn();
				}
				
				$timeout(function(){
					if(vm.inputIdToFocus != undefined){
						$("#" + vm.inputIdToFocus).focus();
					}
				})
			});
		}
		
		function removeCustomerFromInvoice(){
			vm.invoice.customer = null;
			
			vm.saveInvoice();
		}
		
		function removeInvoiceDetail(invoiceDetail){
			for(var detailIndex in vm.invoice.details){
				if(vm.invoice.details[detailIndex].id == invoiceDetail.id){
					vm.invoice.details.splice(detailIndex, 1);
					
					break;
				}
			}
			
			vm.saveInvoice();
		}
		
		function saveInvoice(successCallback){
			vm.loading = true;
			
			vm.loadInvoices(loadInvoiceSuccess);
			
			function loadInvoiceSuccess(invoices){
				var existingInvoice = vm.lookupInvoice(vm.invoice, invoices);
				
				if(existingInvoice != null && Math.round(vm.invoice.modificationDate / 1000) - existingInvoice.modificationDate / 1000 == 0)
					$http
						.post(INVOICE_URL, vm.invoice)
						.success(saveInvoiceSuccess)
						.finally(saveInvoiceCompleted);
				else{
					if(existingInvoice != null)
						errorTraceService.postError({
							message : "Invoice " + vm.invoice.id + " state changed has changed it was loaded from cash register. Current modification date : " + 
							Math.round(vm.invoice.modificationDate / 1000) + ". New modification date : " + existingInvoice != null ? existingInvoice.modificationDate / 1000 : "null"});
					
					vm.loading = false;
					vm.warnInvoiceClosed();
					
					vm.refreshInvoices();
				}
				
				function saveInvoiceCompleted(){
					vm.loading = false;
				}
				
				function saveInvoiceSuccess(invoice){
					vm.invoice = invoice;
					
					vm.refreshInvoices();
					
					if(successCallback != undefined)
						successCallback();
				} 
			}
		}
		
		function searchProductKeyUp($event){
			vm.inputIdToFocus = $event.target.id;
			
			if($event.keyCode == 13){
				vm.searchProduct();
			}
		}
		
		function searchProduct(){
			vm.typedEan13 = vm.ean13;
			
			
			if(vm.typedEan13 != "" && !vm.searchInProgress){
				vm.searchInProgress = true;
				
				$http.get("product?ean13=" + vm.typedEan13)
					.success(searchProductSuccess)
					.finally(searchProductCompleted);

				vm.ean13 = "";
			}
			
			function searchProductSuccess(product){
				if(product != null && product != "")
					vm.addOneProduct(product);
			}
			
			function searchProductCompleted(){
				vm.searchInProgress = false;
			}
		}
		
		function showInvoice(invoice){
			vm.invoice = invoice;
			
			$timeout(function(){
				$(".invoice-tabs > li").removeClass("active");
				$("#invoice-tab-" + invoice.id).addClass("active");
				
				if(invoice.details.length == 0){
					$(".checkout").attr("disabled", "disabled");
				}else{
					$(".checkout").removeAttr("disabled");
				}
			});
		}
		
		function warnInvoiceClosed(){
			$(".invoice-closed-modal").modal();
			$(".payment-modal").modal("hide");
			
			$scope.$broadcast("clodeProductSearchModal");
		}
	}
})();