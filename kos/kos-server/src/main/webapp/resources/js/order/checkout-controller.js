(function(){
	var CONTAINER_ID = "payment-form";
	
	angular
		.module("kos")
		.controller("CheckoutCtrl", CheckoutCtrl);
	
	function CheckoutCtrl(accountService, braintreeService, customerOrderService, $scope){
		var vm = this;

		vm.workflowStep = "shipping";
		vm.confirmCheckoutActivated = false;
		
		loadCart();
		
		$scope.$on("addressSaved", addressSavedEvent);
		
		vm.confirmShippingMethod = confirmShippingMethod;
		vm.editAddress = editAddress;
		vm.saveCustomerOrder = saveCustomerOrder;
		vm.updateShippingAddressPreferedForBilling = updateShippingAddressPreferedForBilling;
		
		function addressSavedEvent($event, address){
			if(address.type == 'BILLING')
				vm.customerOrder.billingAddress = address;
			else
				vm.customerOrder.shippingAddress = address;
			
			saveCustomerOrder()
				.then(loadCart);
		}
		
		function checkCheckoutActivationState(){
			if(	vm.customerOrder.account.shippingAddress != null && (
					vm.customerOrder.account.shippingAddressPreferedForBilling || 
					vm.customerOrder.account.shippingAddress != null)){
				vm.confirmCheckoutActivated = true;
			}
		}
		
		function confirmShippingMethod(){
			braintreeService
				.setup(CONTAINER_ID)
				.then(showPaymentOptions);
			
			function showPaymentOptions(){
				vm.workflowStep = "payment";
			}
		}
		
		function loadCart(){
			customerOrderService
				.loadCart()
				.then(loadCartSuccess);
			
			function loadCartSuccess(customerOrder){
				vm.customerOrder = customerOrder;
				
				if(vm.customerOrder.shippingAddress == null && vm.customerOrder.account.shippingAddress != null)
					vm.customerOrder.shippingAddress = vm.customerOrder.account.shippingAddress;
					
				
				checkCheckoutActivationState();
			}
		}
		
		function editAddress(address){
			$scope.$broadcast("editAddress", address);
		}
		
		function saveCustomerOrder(){
			vm.loading = true;
			
			return customerOrderService
				.saveCustomerOrder(vm.customerOrder)
				.then(saveCustomerOrderSuccess)
				.finally(saveCustomerOrderCompleted);
			
			function saveCustomerOrderCompleted(){
				vm.loading = false;
			}
			
			function saveCustomerOrderSuccess(customerOrder){
				vm.customerOrder = customerOrder;
				
				return customerOrder;
			}
		}
		
		function updateShippingAddressPreferedForBilling(){
			accountService
				.updateShippingAddressPreferedForBilling(vm.customerOrder.account.shippingAddressPreferedForBilling)
				.then(accountUpdated);
				
			function accountUpdated(account){
				if(account.shippingAddressPreferedForBilling)
					vm.customerOrder.billingAddress = null;
				else{
					vm.customerOrder.billingAddress = vm.customerOrder.account.billingAddress;
				}
				
				return saveCustomerOrder();
			}
		}
	}
})();