(function(){
	var STOCKTAKING_DIFF_URL = "/stocktaking-diff";
	
	angular
		.module("Kubik")
		.factory("stocktakingDiffService", StocktakingDiffService);
	
	function StocktakingDiffService($http){
		return {
			updateAdjustmentQuantity : updateAdjustmentQuantity,
			updateValidated : updateValidated
		};
		
		function updateAdjustmentQuantity(id, adjustmentQuantity){
			return $http
				.post(STOCKTAKING_DIFF_URL + "/" + id + "?adjustmentQuantity=" + adjustmentQuantity)
				.then(postSuccess);
			
			function postSuccess(response){
				return response.data;
			}
		}
		
		function updateValidated(id, validated){
			return $http
				.post(STOCKTAKING_DIFF_URL + "/" + id + "?validated=" + validated)
				.then(postSuccess);
			
			function postSuccess(response){
				return response.data;
			}
		}
	}
})();