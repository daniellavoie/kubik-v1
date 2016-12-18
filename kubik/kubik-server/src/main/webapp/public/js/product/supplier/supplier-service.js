(function(){
	var SUPPLIER_URL = "/supplier";
	
	angular
		.module("Kubik")
		.factory("supplierService", SupplierService);
	
	angular
		.module("KubikProductVehicule")
		.factory("supplierService", SupplierService);
	
	function SupplierService($http){
		return {
			findAll : findAll,
			findByEan13 : findByEan13
		}
		
		function findAll(){
			return $http
			    .get(SUPPLIER_URL)
			    .then(getSuccess);
			
			function getSuccess(response){
				return response.data;
			}
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