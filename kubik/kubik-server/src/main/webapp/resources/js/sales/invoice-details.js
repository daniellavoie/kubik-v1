(function(){
	var invoiceId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("InvoiceDetailsCtrl", InvoiceDetailsCtrl);
	
	function InvoiceDetailsCtrl($scope, $http){
		var vm = this;

		vm.changeInvoice = changeInvoice;
		vm.changePaymentMethod = changePaymentMethod;
		vm.confirmRefund = confirmRefund;
		vm.loadInvoice = loadInvoice;
		vm.loadNextInvoice = loadNextInvoice;
		vm.loadPreviousInvoice = loadPreviousInvoice;
		vm.openCustomerCard = openCustomerCard;
		vm.openProductCard = openProductCard;
		vm.openReceipt = openReceipt;
		vm.printReceipt = printReceipt;
		vm.refund = refund;
		
		loadInvoice();
		loadNextInvoice();
		loadPreviousInvoice();		
		
		$scope.$on("customerSaved", function($event, customer){
			vm.loadInvoice();
		});
		
		function changeInvoice(invoiceId){
			location.href = invoiceId;
		}
		
		function changePaymentMethod(payment, paymentMethodType){
			payment.paymentMethod.type = paymentMethodType;
			
			$http.post("/invoice", vm.invoice).success(changePaymentMethodSuccess);
			
			function changePaymentMethodSuccess(invoice){
				vm.invoice = invoice;
			}
		}
		
		function confirmRefund(){
			$(".refund-modal").modal();
		}
		
		function loadInvoice(){
			$http.get(invoiceId).success(function(invoice){
				vm.invoice = invoice;
			});
		}
		
		function loadNextInvoice(){
			$http.get("../invoice/" + invoiceId + "/next").success(function(nextInvoiceId){
				if(nextInvoiceId == "") customerCreditId = null;
				vm.nextInvoice = nextInvoiceId;
			});
		}
		
		function loadPreviousInvoice(){
			$http.get("../invoice/" + invoiceId + "/previous").success(function(previousInvoiceId){
				if(previousInvoiceId == "") customerCreditId = null;
				vm.previousInvoice = previousInvoiceId;
			});
		}
		
		function openCustomerCard($event, customer){
			$event.stopPropagation();
			
			$scope.$broadcast("openCustomerCard", customer);
		}
		
		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}

		function openReceipt(){
			window.open(vm.invoice.id + "/receipt.pdf", "Ticket de caisse", "pdf");
		}
		
		function printReceipt(){
			$http.post("../invoice/" + vm.invoice.id + "/receipt?print");
		}
		
		function refund(){
			vm.invoice.status = {type : "REFUND"};
			
			$http.post("../invoice", vm.invoice).success(function(invoice){
				vm.invoice = invoice;
			});
		};
	}
})();