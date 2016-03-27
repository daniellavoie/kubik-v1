(function(){
	var PRODUCT_INVENTORY = "/productInventory";
	
	angular
		.module("Kubik")
		.factory("productInventoryService", ProductInventoryService);
	
	function ProductInventoryService($http){
		return {
			updateProductInventory : updateProductInventory
		};
		
		function updateProductInventory(ean13){
			return $http
				.get(PRODUCT_INVENTORY + "/product/" + ean13 + "?updateInventory")
				.then(updateProductInventorySuccess);
			
			function updateProductInventorySuccess(response){
				return response.data;
			}
		}
	}
})();