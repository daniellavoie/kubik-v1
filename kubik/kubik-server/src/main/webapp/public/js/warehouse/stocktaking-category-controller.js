(function(){
	var pathParts = location.pathname.split("/");
	var stocktakingId = pathParts[2];
	var categoryId = parseInt(pathParts[4]);
	
	angular
		.module("Kubik")
		.controller("StocktakingCategoryCtrl", StocktakingCategoryCtrl);
	
	function StocktakingCategoryCtrl(productService, stocktakingProductService, stocktakingService, $scope, $timeout){
		var vm = this;
		
		vm.search = {};
		
		loadStocktaking();
		selectInput();
		
		vm.deleteStocktakingProduct = deleteStocktakingProduct;
		vm.hideError = hideError;
		vm.inputKeyup = inputKeyup;
		vm.openProductCard = openProductCard;
		vm.selectInput = selectInput;
		vm.updateQuantity = updateQuantity;
		
		function deleteStocktakingProduct(stocktakingProduct){
			vm.loading = true;
			
			stocktakingProductService
				.deleteStocktakingProduct(stocktakingProduct)
				.then(deleteSuccess, deleteError)
				.finally(deleteCompleted);
			
			function deleteCompleted(){
				vm.loading = false;
			}
			
			function deleteError(){
				vm.error = true;
			}
			
			function deleteSuccess(){
				vm.category.products.splice(vm.category.products.indexOf(stocktakingProduct), 1);
			}
		}
		
		function hideError(){
			vm.notFound = false;
			vm.error = false;
		}
		
		function inputKeyup($event){
			if($event.keyCode == 13){
				searchProduct();
			}
		}
		
		function loadStocktaking(){
			return stocktakingService
				.findOne(stocktakingId)
				.then(findOneSuccess);
			
			function findOneSuccess(stocktaking){
				vm.stocktaking = stocktaking;
				
				vm.category = Stream(stocktaking.categories).filter({id : categoryId}).findFirst().get();
				
				return stocktaking;
			}
		}

		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
		
		function searchProduct(){
			vm.loading = true;
			hideError();
			
			return productService
				.findByEan13(vm.search.query)
				.then(findByEan13Success, findByEan13Error);
			
			function findByEan13Error(){
				vm.notFound = true;
				vm.loading = false;
				
				selectInput();
			}
			
			function findByEan13Success(product){
				if(product != ""){
					stocktakingProductService
						.addProductToCategory(product.id, vm.category.id, vm.stocktaking.id)
						.then(addProductSuccess, addProductError)
						.finally(addProductCompleted);
					
					function addProductCompleted(){
						vm.loading = false;
						selectInput();
					}
					
					function addProductError(error){
						vm.error = true;
					}
					
					function addProductSuccess(stocktakingProduct){
						vm.search.query = "";
						
						var optional = Stream(vm.category.products).filter(onElement).findFirst();
						if(optional.isPresent())
							optional.get().quantity = stocktakingProduct.quantity;
						else
							vm.category.products.splice(0, 0, stocktakingProduct);
						
						function onElement(element){
							return element.product.id == stocktakingProduct.product.id;
						}
						
						return stocktakingProduct;
					}
				}else
					findByEan13Error();
			}
		}
		
		function selectInput(){
			$timeout(function(){$(".query").select();});
		}
		
		function updateQuantity(stocktakingProduct){
			vm.loading = true;
			
			return stocktakingProductService
				.updateQuantity(stocktakingProduct.id, stocktakingProduct.quantity)
				.then(null, saveError)
				.finally(saveCompleted);
			
			function saveCompleted(){
				vm.loading = false;
			}
			
			function saveError(){
				vm.error = true;
			}
		}
	}
})();