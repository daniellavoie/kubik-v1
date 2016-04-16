(function(){
	var PRODUCT_DOUBLES_URL = "/product-doubles";
	
	angular
		.module("Kubik")
		.factory("productDoublesService", ProductDoublesServices);
	
	function ProductDoublesServices($http){
		return {
			findAll : findAll,
			findOne : findOne
		};
		
		function findAll(){
			return $http
				.get(PRODUCT_DOUBLES_URL)
				.then(getSuccess);
			
			function getSuccess(response){
				return response.data;
			}
		}
		
		function findOne(ean13){
			return $http
				.get(PRODUCT_DOUBLES_URL + "/" + ean13)
				.then(getSuccess);
				
			function getSuccess(response){
				return response.data;
			}
		}
	}
})();