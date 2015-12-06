(function(){
	var INVOICE_URL = "/invoice";
	
	angular
		.module("Kubik")
		.controller("CashRegisterCtrl", CashRegisterCtrl);
	
	function CashRegisterCtrl($scope, $http, $timeout){
		var vm = this;
		
		vm.focusEan13Input = true;
		
		vm.addCustomerToInvoice = addCustomerToInvoice;
		vm.addOneProduct = addOneProduct;
		vm.cancelInvoice = cancelInvoice;
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
		
		function checkoutInvoice(){
			$scope.$broadcast("checkoutInvoice", vm.invoice);
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
		
		function saveInvoice(){
			for(var detailIndex in vm.invoice.details){
				var detail = vm.invoice.details[detailIndex];
							
				detail.product = {
					id : detail.product.id,
					ean13 : detail.product.ean13,
					supplier : {id : detail.product.supplier.id}
				};
			}
			
			vm.loadInvoices(loadInvoiceSuccess);
			
			function loadInvoiceSuccess(invoices){
				var existingInvoice = vm.lookupInvoice(vm.invoice, invoices);
				
				if(existingInvoice != null && Math.round(vm.invoice.modificationDate / 1000) == Math.round(existingInvoice.modificationDate / 1000)){
					$http.post(INVOICE_URL, vm.invoice).success(saveInvoiceSuccess);
				}else{
					vm.warnInvoiceClosed();
					
					vm.refreshInvoices();
				}
				
				function saveInvoiceSuccess(invoice){
					vm.invoice = invoice;
					
					vm.refreshInvoices();
				} 
			}
		}
		
		function searchProductKeyUp($event){
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
			
			function searchProductSuccess(products){
				// Check if a product was found
				if(products.length == 0){
					// Show a warning explaining that the product does not exists.
					$(".product-not-found").modal();
				}else {
					$(".product-not-found").modal("hide");
					
					if (products.length > 1){
						vm.kubikProductSearch.openSearchModal(vm.typedEan13);
					}else{
						vm.addOneProduct(products[0]);
					}
				}
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
			
			// Load customer credit amount.
			if(invoice.customer != null){
				$http.get("customer/" + invoice.customer.id + "/customerCreditAmount").success(loadCustomerCreditAmountSuccess);
			}
			
			function loadCustomerCreditAmountSuccess(customerCreditAmount){
				$timeout(function(){
					vm.customerCreditAmount = customerCreditAmount;
				});
			}
		}
		
		function warnInvoiceClosed(){
			$(".invoice-closed-modal").modal();
			$(".payment-modal").modal("hide");
			
			$scope.$broadcast("clodeProductSearchModal");
		}
	}
})();

(function(){
	angular
		.module("Kubik")
		.controller("PaymentCtrl", PaymentCtrl);
	
	function PaymentCtrl($scope, $http, $timeout){
		var vm = this;
		
		vm.amountKeyPressed = amountKeyPressed;
		vm.checkoutInvoice = checkoutInvoice;
		vm.closeSale = closeSale;
		vm.loadPaymentMethods = loadPaymentMethods;
		vm.newPaymentMethodSelected = newPaymentMethodSelected;
		vm.openReceipt = openReceipt;
		vm.printReceipt = printReceipt;
		vm.selectPaymentMethod = selectPaymentMethod;
		vm.validatePayment = validatePayment;

		loadPaymentMethods();
		
		$scope.$on("checkoutInvoice", function($event, invoice){
			vm.checkoutInvoice(invoice);
		});
		
		$scope.$on("newPaymentMethod", function(event, paymentMethod){
			vm.newPaymentMethodSelected(paymentMethod);
		});
		
		function amountKeyPressed($event){
			if(($event.keycode >= 48 && $event.keycode <= 57) || ($event.keycode >= 96 && $event.keycode <= 105)){
				if(vm.amount == 0){
					vm.amount = "";
				}
			}else if($event.keycode == 188 || $event.keycode == 190){
				vm.dotEntered = true;
			}else{
				return false;
			}
		}
		
		function checkoutInvoice(invoice){
			vm.invoice = invoice;
			
			vm.invoice.payments = [];
			vm.amountLeft = vm.invoice.totalAmount;
			
			// Open the modal.
			$(".payment-modal").on("shown.bs.modal", function(){
				$(".payment-amount").focus();
			}).modal({
				backdrop : "static",
				keyboard : "false"
			});
		}
		
		function closeSale(){
			$(".payment-modal").modal("hide");
			
			$scope.$emit("saleOver");
		}
		
		function loadPaymentMethods(){
			vm.paymentMethod = null;
			
			$http.get("paymentMethod").success(loadPaymentMethodSuccess);
			
			function loadPaymentMethodSuccess(paymentMethods){
				vm.paymentMethods = paymentMethods;
			}
		}
		
		function newPaymentMethodSelected(paymentMethod){
			vm.paymentMethod = paymentMethod;
		}
		
		function openReceipt(){
			window.open("invoice/" + vm.invoice.id + "/receipt.pdf", "Ticket de caisse", "height=600,width=479");
			
			vm.closeSale();
		}
		
		function printReceipt(){
			$http.post("invoice/" + vm.invoice.id + "/receipt?print");
			
			vm.closeSale();
		}

		function selectPaymentMethod(paymentMethod){
			if(paymentMethod.type != "CASH" && (paymentMethod.type != "CREDIT" || vm.customerCreditAmount > vm.amountLeft)){
				$(".payment-amount").val(vm.amountLeft);
			}else if(paymentMethod.type == "CREDIT"){
				$(".payment-amount").val(vm.customerCreditAmount);
			}
			
			vm.paymentMethod = paymentMethod;
		}
		
		function validatePayment(){
			var $paymentAmount = $(".payment-amount");
			var paymentVal = parseFloat($paymentAmount.val().replace(",", "."));
			
			if(paymentVal  == 0){
				// Show warning.
				return;
			}
			
			if(vm.paymentMethod == null){
				// Show warning
				return;
			}
			
			// Checks if the payment already exists.
			var newPayment = null;
			for(var paymentIndex in vm.invoice.payments){
				var payment = vm.invoice.payments[paymentIndex];
				
				if(payment.paymentMethod.type == vm.paymentMethod.type){
					newPayment = payment;
					newPayment.amount += paymentVal;
					
					break;
				}
			}
			
			if(newPayment == null){
				newPayment = {
					invoice : {id : vm.invoice.id},
					amount : paymentVal,
					paymentMethod : vm.paymentMethod
				};
				
				vm.invoice.payments.push(newPayment);
			}
			
			vm.amountLeft = Number((vm.amountLeft - paymentVal).toFixed(2));
			
			if(vm.amountLeft <= 0){
				vm.invoice.status = {type : "PAID"};
				
				$http.post("invoice", vm.invoice).success(saveInvoiceSuccess);
			}
			
			$paymentAmount.val("");
			
			vm.paymentMethod = null;
			
			$(".payment-methods .active").removeClass("active");
			
			function saveInvoiceSuccess(invoice){
				vm.invoice = invoice;
			}
		}
	}
})();