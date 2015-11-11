(function(){
	var PRODUCT_URL = "/product";
	
	angular
		.module("kos")
		.factory("productService", ProductService);
	
	function ProductService($http){
		return {
			loadProduct : loadProduct
		};
		
		function loadProduct(id){
			return $http.get(PRODUCT_URL + "/" + id).then(loadProductSuccess);
			
			function loadProductSuccess(response){
				return response.data;
			}
		}
	}
})();