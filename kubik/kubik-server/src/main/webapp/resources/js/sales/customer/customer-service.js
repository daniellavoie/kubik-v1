(function(){
	var CUSTOMER_URL = "/customer";
	
	angular
		.module("Kubik")
		.factory("customerService", CustomerService);
	
	function CustomerService($http){
		return {
			loadCustomerCreditAmount : loadCustomerCreditAmount
		};
		
		function loadCustomerCreditAmount(customerId){
			return $http
				.get(CUSTOMER_URL + "/" + customerId + "/customerCreditAmount")
				.then(loadCustomerCreditAmountSuccess);
			
			function loadCustomerCreditAmountSuccess(response){
				return response.data;
			}
		}
	}
})();