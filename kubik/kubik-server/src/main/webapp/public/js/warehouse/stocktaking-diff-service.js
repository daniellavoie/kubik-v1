(function(){
	var STOCKTAKING_DIFF_URL = "/stocktaking-diff";
	
	angular
		.module("Kubik")
		.factory("stocktakingDiffService", StocktakingDiffService);
	
	function StocktakingDiffService($http){
		return {
			updateCountedQuantity : updateCountedQuantity,
			updateValidated : updateValidated
		};
		
		function updateCountedQuantity(id, countedQuantity){
			return $http
				.post(STOCKTAKING_DIFF_URL + "/" + id + "?countedQuantity=" + countedQuantity)
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