(function(){
	var CONTAINER_ID = "payment-form";
	var $modifyAddressModal = $(".modify-address");
	
	angular
		.module("kos")
		.controller("CheckoutCtrl", CheckoutCtrl);
	
	function CheckoutCtrl(accountService, braintreeService, customerOrderService){
		var vm = this;

		vm.workflowStep = "shipping";
		
		loadCart();
		
		vm.confirmShippingMethod = confirmShippingMethod;
		vm.modifyAddress = modifyAddress;
		vm.saveCustomerOrder = saveCustomerOrder;
		
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
			}
		}
		
		function modifyAddress(address, shippingAddress){
			vm.address = address;
			vm.shippingAddress = shippingAddress;
			
			$modifyAddressModal.modal();
		}	
		
		function saveAddress(){
//			accountService.saveAddress(vm.address, vm.account.);
		}
		
		function saveCustomerOrder(){
			vm.loading = true;
			
			customerOrderService
				.saveCustomerOrder(vm.customerOrder)
				.then(saveCustomerOrderSuccess)
				.finally(saveCustomerOrderCompleted);
			
			function saveCustomerOrderCompleted(){
				vm.loading = false;
			}
			
			function saveCustomerOrderSuccess(customerOrder){
				vm.customerOrder = customerOrder;
			}
		}
	}
})();