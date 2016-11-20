(function(){
	var ACCOUNT_URL = "/account";
	var AUTHENTICATE = "/authenticate";
	
	angular
		.module("kos")
		.factory("accountService", AccountService);
	
	function AccountService($http){
		return {
			createAccount : createAccount,
			loadAccount : loadAccount,
			saveAddress : saveAddress,
			signIn : signIn,
			updateShippingAddressPreferedForBilling : updateShippingAddressPreferedForBilling
		};
		
		function createAccount(username, password){
			return $http
				.post(ACCOUNT_URL + "?username=" + username + "&password=" + password)
				.then(createAccountSuccess);
			
			function createAccountSuccess(response){
				return response.data;
			}
		}
		
		function loadAccount(){
			return $http
				.get(ACCOUNT_URL)
				.then(loadAccountSuccess);
			
			function loadAccountSuccess(response){
				return response.data.account;
			}
		}
		
		function saveAddress(address){
			return $http
				.post(ACCOUNT_URL + "/address", address)
				.then(saveAddressSuccess);
			
			function saveAddressSuccess(response){
				return response.data;
			}
		}
		
		function signIn(username, password){
			return $http
				.post(AUTHENTICATE + "?username=" + username + "&password=" + password);
		}
		
		function updateShippingAddressPreferedForBilling(shippingAddressPreferedForBilling){
			return $http
				.post(ACCOUNT_URL + "?shippingAddressPreferedForBilling=" + shippingAddressPreferedForBilling)
				.then(updateSuccess);
				
			function updateSuccess(response){
				return response.data;
			}
		}
	}
})();