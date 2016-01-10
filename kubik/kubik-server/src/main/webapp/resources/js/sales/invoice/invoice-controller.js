(function(){
	var invoiceId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("InvoiceDetailsCtrl", InvoiceDetailsCtrl);
	
	function InvoiceDetailsCtrl(invoiceService, productService, $scope){
		var vm = this;

		vm.cancelInvoice = cancelInvoice;
		vm.changeInvoice = changeInvoice;
		vm.changePaymentMethod = changePaymentMethod;
		vm.checkoutInvoice = checkoutInvoice
		vm.confirmOrder = confirmOrder;
		vm.confirmRefund = confirmRefund;
		vm.loadInvoice = loadInvoice;
		vm.loadNextInvoice = loadNextInvoice;
		vm.loadPreviousInvoice = loadPreviousInvoice;
		vm.openCustomerCard = openCustomerCard;
		vm.openProductCard = openProductCard;
		vm.openProductSearch = openProductSearch;
		vm.openReceipt = openReceipt;
		vm.printReceipt = printReceipt;
		vm.refund = refund;
		vm.removeDetail = removeDetail;
		vm.save = save;
		vm.searchProductKeyUp = searchProductKeyUp;
		
		loadInvoice();
		loadNextInvoice();
		loadPreviousInvoice();		
		
		$scope.$on("customerSaved", function($event, customer){
			loadInvoice();
		});
		
		$scope.$on("productSelected", function($event, product){
			addProductToInvoice(product);
		});
		
		$scope.$on("saleOver", saleOverEvent);
		
		function addProductToInvoice(product){
			var detail = getDetail(product);
			if(detail != null)
				detail.quantity += 1;
			else{
				product.category = null;
				
				vm.invoice.details.push({
					invoice : {id : vm.invoice.id},
					product : product,
					quantity : 1
				});
			}
			
			save();
			
			$("html, body").animate({ scrollTop: $(document).height() }, 500);
		}
		
		function cancelInvoice(){
			invoiceService
				.cancelInvoice(vm.invoice)
				.then(cancelInvoiceSuccess);
			
			function cancelInvoiceSuccess(invoice){
				vm.invoice = invoice;
			}
		}
		
		function changeInvoice(invoiceId){
			location.href = invoiceId;
		}
		
		function changePaymentMethod(payment, paymentMethodType){
			payment.paymentMethod.type = paymentMethodType;
			
			invoiceService
				.save(vm.invoice)
				.then(saveSuccess);
			
			function saveSuccess(invoice){
				vm.invoice = invoice;
			}
		}
		
		function checkoutInvoice(){
			$scope.$broadcast("checkoutInvoice", vm.invoice);
		}
		
		function confirmOrder(){
			vm.invoice.status = {type : 'ORDER_CONFIRMED'};
			
			save();
		}
		
		function confirmRefund(){
			$(".refund-modal").modal();
		}
		
		function getDetail(product){
			for(var detailIndex in vm.invoice.details){
				if(vm.invoice.details[detailIndex].product.id == product.id){
					return vm.invoice.details[detailIndex];
				}
			}
			
			return null;	
		}
		
		function loadInvoice(){
			invoiceService
				.findOne(invoiceId)
				.then(findOneSuccess);
			
			function findOneSuccess(invoice){
				vm.invoice = invoice;
			}
		}
		
		function loadNextInvoice(){
			invoiceService
				.findNext(invoiceId)
				.then(findNextSuccess);
			
			function findNextSuccess(nextInvoiceId){
				if(nextInvoiceId == "") 
						customerCreditId = null;
				
				vm.nextInvoice = nextInvoiceId;
			}
		}
		
		function loadPreviousInvoice(){
			invoiceService
				.findPrevious(invoiceId)
				.then(findPreviousSuccess);
			
			function findPreviousSuccess(previousInvoiceId){
				if(previousInvoiceId == "") 
					customerCreditId = null;
				
				vm.previousInvoice = previousInvoiceId;
			}
		}
		
		function openCustomerCard($event, customer){
			$event.stopPropagation();
			
			$scope.$broadcast("openCustomerCard", customer);
		}
		
		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
		
		function openProductSearch(options){
			$scope.$broadcast("openProductSearchModal", options);
		}

		function openReceipt(){
			window.open(vm.invoice.id + "/receipt.pdf", "Ticket de caisse", "pdf");
		}
		
		function printReceipt(){
			invoiceService
				.printReceipt();
		}
		
		function refund(){
			vm.invoice.status = {type : "REFUND"};
			
			save();
		};

		function removeDetail(detail){
			invoiceService
				.removeInvoiceDetail(vm.invoice, detail);
			
			save();
		}
		
		
		function saleOverEvent(){
			loadInvoice();
		}
		
		function save(){
			invoiceService
				.save(vm.invoice)
				.then(saveSuccess);
			
			function saveSuccess(invoice){
				vm.invoice = invoice;
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
			}
			
			function findByEan13Completed(){
				vm.searchInProgress = false;
			}
			
			function findByEan13Success(products){
				// Check if a product was found
				if(products.length == 0){
					// Show a warning explaining that the product does not exists.
					$(".product-not-found").modal();
				}else {
					$(".product-not-found").modal("hide");
					
					if (products.length > 1){
						openProductSearch({query : vm.search.typedEan13});
					}else{
						addProductToInvoice(products[0]);
					}
				}
			}
		}
		
		function searchProductKeyUp($event){
			if($event.keyCode == 13){
				vm.ean13 = this.ean13;
				
				searchProduct();
			}
		}
	}
})();