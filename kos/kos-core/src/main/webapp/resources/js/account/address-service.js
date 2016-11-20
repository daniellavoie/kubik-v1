(function(){
	var ADDRESS_URL = "/account/address";
	angular
		.module("kos")
		.factory("addressService", AddressService);
	
	function AddressService($http){
		return {
			saveAddress : saveAddress
		};
		
		function saveAddress(address){
			return $http
				.post(ADDRESS_URL, address)
				.then(saveAddressSuccess);
			
			function saveAddressSuccess(response){
				return response.data;
			}
		}
	}
})();