(function(){
	var CHECKOUT_URL = "/checkout";
	
	angular
		.module("kos")
		.factory("braintreeService", BraintreeService);
	
	function BraintreeService($http){
		return {
			setup : setup
		}
		
		function setup(containerId){
			return retreiveClientToken()
				.then(initializeBraintreeContainer);
			
			function initializeBraintreeContainer(clientToken){
				braintree.setup(
					clientToken,
					"dropin", 
					{container: containerId}
				);
			}
			
			function retreiveClientToken(){
				return $http
					.get(CHECKOUT_URL + "/token")
					.then(retreiveClientTokenSuccess);
				
				function retreiveClientTokenSuccess(response){
					return response.data;
				}
			}
		}
	}
})();