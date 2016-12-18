(function(){
	angular
	    .module("KubikProductVehicule")
	    .factory("productService", ProductService);
	
	function ProductService(envService, $http){
		return {
			findOne : findOne,
			query : query,
			save : save
		};
		
		function findOne(ean13){
			return envService
			    .getEnvProperty("kubik.product.vehicule.url")
			    .then(getSuccess);
			
			function getSuccess(url){
				return $http
					.get(url + "/product/" + ean13)
					.then(getSuccess);
				
				function getSuccess(response){
					return response.data
				}
			}
		}
		
		function query(params){
			return envService
			    .getEnvProperty("kubik.product.vehicule.url")
			    .then(getSuccess);
			
			function getSuccess(url){
				return $http
				    .get(url + "/product?" + $.param(params))
				    .then(getSuccess);
				
				function getSuccess(response){
					return response.data;
				}
			}
		}
		
		function save(product){
			return envService
		    	.getEnvProperty("kubik.product.vehicule.url")
		    	.then(getSuccess, getFailure);
			
			function getFailure(error){
				console.log(error);
			}
			
			function getSuccess(url){
				return $http
				    .post(url + "/product", product)
				    .then(postSuccess);
				
				function postSuccess(response){
					return response.data;
				}
			}
		}
	}
})();