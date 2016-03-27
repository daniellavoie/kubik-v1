(function(){
	var MODAL_CLASS = ".new-category";
	
	var stocktakingId = location.pathname.split("/")[2];
	
	
	angular
		.module("Kubik")
		.controller("StocktakingCtrl", StocktakingCtrl);
	
	function StocktakingCtrl(stocktakingService, stocktakingCategoryService){
		var vm = this;
		
		loadStocktaking();
		
		vm.generateDiff = generateDiff;
		vm.openCategory = openCategory;
		vm.openDiff = openDiff;
		vm.openNewCategoryDialog = openNewCategoryDialog;
		vm.saveNewCategory = saveNewCategory;
		
		function generateDiff(){
			vm.loading = true;
			
			stocktakingService
				.generateDiff(vm.stocktaking.id)
				.then(generateDiffSuccess)
				.finally(generateDiffCompleted);
			
			function generateDiffCompleted(){
				vm.loading = false;
			}
			
			function generateDiffSuccess(){
				openDiff();
			}
		}
		
		function loadStocktaking(){
			return stocktakingService
				.findOne(stocktakingId)
				.then(findOneSuccess);
			
			function findOneSuccess(stocktaking){
				vm.stocktaking = stocktaking;
			}
		}
		
		function openCategory(category){
			location.href = "/stocktaking/" + vm.stocktaking.id + "/stocktaking-category/" + category.id; 
		}
		
		function openDiff(){
			location.href = "/stocktaking/" + vm.stocktaking.id + "/stocktaking-diffs";
		}
		
		function openNewCategoryDialog(){
			vm.newCategory = {stocktaking : {id : vm.stocktaking.id}};
			
			$(MODAL_CLASS).modal();
		}
		
		function saveNewCategory(){
			vm.nameAlreadyExists = false;
			
			loadStocktaking()
				.then(loadStocktakingSuccess);
			
			function loadStocktakingSuccess(){
				angular.forEach(vm.stocktaking.categories, onCategory);
				
				if(!vm.nameAlreadyExists)
					vm.loading = true;
				
					stocktakingCategoryService
						.save(vm.newCategory)
						.then(saveSuccess)
						.finally(saveCompleted);
				
				function onCategory(category, index){
					if(category.name == vm.newCategory.name)
						vm.nameAlreadyExists = true;
				}
				
				function saveCompleted(){
					vm.loading = false;
				}
				
				function saveSuccess(category){
					window.location = "/stocktaking/" + vm.stocktaking.id + "/stocktaking-category/" + category.id;
				}
			}
		}
	}
})();