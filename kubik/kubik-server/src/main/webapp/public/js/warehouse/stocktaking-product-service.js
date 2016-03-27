(function() {
	var STOCKTAKING_PRODUCT_URL = "/stocktaking-product"
	var STOCKTAKING_URL = "/stocktaking";

	angular
		.module("Kubik")
		.factory("stocktakingProductService", StocktakingProductService);

	function StocktakingProductService($http) {
		return {
			addProductToCategory : addProductToCategory,
			deleteStocktakingProduct : deleteStocktakingProduct,
			updateQuantity : updateQuantity
		};
		
		function addProductToCategory(productId, stocktakingCategoryId, stocktakingId){
			return $http
				.post(
						STOCKTAKING_URL + "/" + stocktakingId + "/stocktakingCategory/" + 
						stocktakingCategoryId + "/stocktakingProduct?productId=" + productId)
				.then(postSuccess);
			
			function postSuccess(response){
				return response.data;
			}
		}
		
		function deleteStocktakingProduct(stocktakingProduct){
			return $http
				.delete(STOCKTAKING_PRODUCT_URL + "/" + stocktakingProduct.id);
		}
		
		function updateQuantity(stocktakingProductId, quantity){
			return $http
				.post(STOCKTAKING_PRODUCT_URL + "/" + stocktakingProductId + "?quantity=" + quantity)
				.then(postSuccess);
			
			function postSuccess(response){
				return response.data;
			}
		}
	}
})();