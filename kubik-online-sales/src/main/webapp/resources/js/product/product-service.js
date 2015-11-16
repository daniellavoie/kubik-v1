(function(){
	var PRODUCT_URL = "/product";
	
	angular
		.module("kos")
		.factory("productService", ProductService);
	
	function ProductService($http){
		return {
			loadProduct : loadProduct,
			search : search
		};
		
		function loadProduct(id){
			return $http.get(PRODUCT_URL + "/" + id).then(loadProductSuccess);
			
			function loadProductSuccess(response){
				return response.data;
			}
		}
		
		function search(searchParams){
			var url = PRODUCT_URL;
			if(searchParams != undefined){
				url += "?" + $.param(searchParams);
			}
			
			return $http.get(url).then(searchSuccess);
			
			function searchSuccess(response){
				return response.data;
			}
		}
	}
})();