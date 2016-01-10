(function(){
	var customerOrderId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("CustomerOrderDetailsCtrl", CustomerOrderDetailsCtrl);
	
	function CustomerOrderDetailsCtrl(customerOrderService, transactionService){
		vm = this;
		
		loadCustomerOrder();
		
		vm.hideError = hideError;
		vm.loadCustomerOrder = loadCustomerOrder;
		vm.loadTransaction = loadTransaction;
		vm.processCustomerOrder = processCustomerOrder;
		vm.saveCustomerOrder = saveCustomerOrder;
		
		function hideError(){
			vm.error = undefined;
		}
		
		function loadCustomerOrder(){
			customerOrderService
				.loadCustomerOrder(customerOrderId)
				.then(loadCustomerOrderSuccess);
			
			function loadCustomerOrderSuccess(customerOrder){
				vm.customerOrder = customerOrder;
				
				loadTransaction();
			}
		}
		
		function loadTransaction(){
			vm.transaction = undefined;
			return transactionService
				.loadTransaction(vm.customerOrder.transactionId)
				.then(loadTransactionSuccess);
			
			function loadTransactionSuccess(transaction){
				vm.transaction = transaction;
			}
		}
		
		function processCustomerOrder(){
			loadTransaction()
				.then(loadTransactionSuccess);
			
			function loadTransactionSuccess(){
				// Asserts that customer order total amount is the same as the transaction.
//				if(vm.customerOrder.totalAmount == vm.transaction.totalAmount){
					customerOrderService
						.process(vm.customerOrder)
						.then(processCustomerOrderSuccess)
						.catch(processCustomerOrderFail);
					
					function processCustomerOrderSuccess(){
						vm.customerOrder = customerOrder;
					}
					
					function processCustomerOrderFail(response){
						vm.error = response.headers("reason");
						
						loadCustomerOrder();
					}
//				}else{
//					vm.error = "La commande ne peut etre traité tant que le montant total ne correspond pas à celui du règlement.";
//				}
			}
		}
		
		function saveCustomerOrder(){
			customerOrderService
				.save(vm.customerOrder)
				.then(saveSuccess)
				.catch(saveFailed);
			
			function saveSuccess(customerOrder){
				vm.customerOrder = customerOrder;
			}
			
			function saveFailed(response){
				vm.error = response.headers("reason");
				
				loadCustomerOrder();
			}
		}
	}
})();