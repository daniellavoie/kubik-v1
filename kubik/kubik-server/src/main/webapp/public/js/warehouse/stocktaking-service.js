(function(){
	var STOCKTAKING_URL = "/stocktaking";
	angular
		.module("Kubik")
		.factory("stocktakingService", StocktakingService);
	
	function StocktakingService($http){
		return {
			findAll : findAll,
			findOne : findOne,
			generateDiff : generateDiff, 
			save : save,
			updateInventory : updateInventory
		};
		
		function findAll(){
			return $http
				.get(STOCKTAKING_URL)
				.then(findAllSuccess);
			
			function findAllSuccess(response){
				return response.data; 
			}
		}
		
		function findOne(id){
			return $http
				.get(STOCKTAKING_URL + "/" + id)
				.then(findOneSuccess);
			
			function findOneSuccess(response){
				return response.data;
			}
		}
		
		function generateDiff(id){
			return $http
				.post(STOCKTAKING_URL + "/" + id + "?generateDiffs");
		}
		
		function save(stocktaking){
			return $http
				.post(STOCKTAKING_URL, stocktaking)
				.then(saveSuccess);
			
			function saveSuccess(response){
				return response.data;
			}
		}
		
		function updateInventory(stocktakingId){
			return $http
				.post(STOCKTAKING_URL + "/" + stocktakingId + "?updateInventory")
				.then(postSuccess);
			
			function postSuccess(response){
				return response.data;
			}
		}
	}
})();