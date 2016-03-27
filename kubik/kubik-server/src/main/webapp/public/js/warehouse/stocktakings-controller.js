(function(){
	angular
		.module("Kubik")
		.controller("StocktakingsCtrl", StocktakingsCtrl);
	
	function StocktakingsCtrl(stocktakingService){
		var vm = this;
		
		loadStocktakings();
		
		vm.newStocktaking = newStocktaking;
		vm.openStocktaking = openStocktaking;
		
		function loadStocktakings(){
			return stocktakingService
				.findAll()
				.then(findAllSuccess);
			
			function findAllSuccess(stocktakings){
				vm.stocktakings = stocktakings;
			}
		}
		
		function newStocktaking(){
			stocktakingService
				.save({})
				.then(saveSuccess);
			
			function saveSuccess(stocktaking){
				location.href = "/stocktaking/" + stocktaking.id;
			}
		}
		
		function openStocktaking(stocktaking){
			location.href = "/stocktaking/" + stocktaking.id;
		}
	}	
})();