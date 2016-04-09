(function(){
	var stocktakingId = location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("StocktakingDiffCtrl", StocktakingDiffCtrl);
	
	function StocktakingDiffCtrl(stocktakingDiffService, stocktakingService, $scope){
		var vm = this;
		
		loadStocktaking();
		
		vm.hideError = hideError;
		vm.openProductCard = openProductCard;
		vm.updateCountedQuantity = updateCountedQuantity;
		vm.updateInventory = updateInventory;
		vm.updateValidated = updateValidated;
		
		function calculateDiffsValidated(){
			vm.diffsValidated = !Stream(vm.stocktaking.diffs).filter({validated : false}).findFirst().isPresent();
		}
		
		function hideError(){
			vm.error = false;
		}
		
		function loadStocktaking(){
			stocktakingService
				.findOne(stocktakingId)
				.then(findOneSuccess);
				
			function findOneSuccess(stocktaking){
				vm.stocktaking = stocktaking;
				
				calculateDiffsValidated();
			}
		}

		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
		
		function updateCountedQuantity(diff){
			vm.loading = true;
			
			return stocktakingDiffService
				.updateCountedQuantity(diff.id, diff.countedQuantity)
				.then(success, error)
				.finally(completed);
			
			function completed(){
				vm.loading = false;
			}
			
			function error(diff){
				vm.error = true;
			}
			
			function success(newDiff){
				diff.adjustmentQuantity = newDiff.adjustmentQuantity;
			}
		}
		
		function updateInventory(){
			vm.updating = true;
			
			return stocktakingService
				.updateInventory(vm.stocktaking.id)
				.then(success, error)
				.finally(completed);
			
			function completed(){
				vm.updating = false;
			}
			
			function error(error){
				vm.error = true;
			}
			
			function success(stocktaking){
				vm.stocktaking = stocktaking;
			}
		}
		
		function updateValidated(diff){
			calculateDiffsValidated();
			vm.loading = true;
			
			return stocktakingDiffService
				.updateValidated(diff.id, diff.validated)
				.then(null, error)
				.finally(completed);
			
			function completed(){
				vm.loading = false;
			}
			
			function error(diff){
				vm.error = true;
			}
		}
	}
})();