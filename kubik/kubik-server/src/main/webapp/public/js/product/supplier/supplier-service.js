(function(){
	var SUPPLIER_URL = "/supplier";
	angular
		.module("Kubik")
		.factory("supplierService", SupplierService);
	
	function SupplierService($http){
		return {
			findByEan13 : findByEan13
		}
		
		function findByEan13(ean13){
			return $http
				.get(SUPPLIER_URL + "?ean13=" + ean13)
				.then(getSuccess);
			
			function getSuccess(response){
				return response.data;
			}
		}
	}
})();