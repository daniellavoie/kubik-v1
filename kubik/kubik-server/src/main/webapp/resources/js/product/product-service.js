(function(){
	var PRODUCT_URL = "/product";
	
	angular
		.module("Kubik")
		.factory("productService", ProductService);
	
	function ProductService($http){
		return {
			findByEan13 : findByEan13
		};
		
		function findByEan13(ean13){
			return $http
				.get(PRODUCT_URL + "?ean13=" + ean13)
				.then(findByEan13Success);
			
			function findByEan13Success(response){
				return response.data;
			}
		}
	}
})();