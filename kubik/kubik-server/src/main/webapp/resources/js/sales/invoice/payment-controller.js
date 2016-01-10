(function(){
	var INVOICE_URL = "/invoice";
	var PAYMENT_METHOD_URL = "/paymentMethod";
	
	angular
		.module("Kubik")
		.controller("PaymentCtrl", PaymentCtrl);
	
	function PaymentCtrl(customerService, $scope, $http){
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
			
			if(vm.invoice.customer != null){
				customerService
					.loadCustomerCreditAmount(vm.invoice.customer.id)
					.then(loadCustomerCreditAmountSuccess);
			}else
				openModal();
			
			function loadCustomerCreditAmountSuccess(customerCreditAmount){
				vm.customerCreditAmount = customerCreditAmount;
				
				openModal();
			}
		}
		
		function closeSale(){
			$(".payment-modal").modal("hide");
			
			$scope.$emit("saleOver");
		}
		
		function loadPaymentMethods(){
			vm.paymentMethod = null;
			
			$http.get(PAYMENT_METHOD_URL).success(loadPaymentMethodSuccess);
			
			function loadPaymentMethodSuccess(paymentMethods){
				vm.paymentMethods = paymentMethods;
			}
		}
		
		function newPaymentMethodSelected(paymentMethod){
			vm.paymentMethod = paymentMethod;
		}
		
		function openModal(){
			$(".payment-modal").on("shown.bs.modal", function(){
				$(".payment-amount").focus();
			}).modal({
				backdrop : "static",
				keyboard : "false"
			});
		}
		
		function openReceipt(){
			window.open("/invoice/" + vm.invoice.id + "/receipt.pdf", "Ticket de caisse", "height=600,width=479");
			
			vm.closeSale();
		}
		
		function printReceipt(){
			$http.post(INVOICE_URL + "/" + vm.invoice.id + "/receipt?print");
			
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
				
				$http.post(INVOICE_URL, vm.invoice).success(saveInvoiceSuccess);
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