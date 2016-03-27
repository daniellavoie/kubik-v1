(function(){
	angular
		.module("Kubik")
		.controller("NonCategorizedProductCtrl", NonCategorizedProductCtrl);
	
	function NonCategorizedProductCtrl($scope, $http, $timeout){
		var vm = this;
		
		loadRandomProduct();
		
		vm.updateProductCategory = updateProductCategory;
		
		$scope.$on("categoriesLoaded", function($event, categoriesVm){
			$timeout(function(){
				categoriesVm.categorySelectedCallback = vm.updateProductCategory;				
			});
		});
		
		vm.loadRandomProduct = loadRandomProduct;
		vm.pass = pass;
		
		function loadRandomProduct(){
			$http.get("/product?category&random").success(loadRandomProductSuccess);
			
			function loadRandomProductSuccess(product){
				vm.product = product;
			}
		}
		
		function pass(){
			vm.loadRandomProduct();
		}
		
		function updateProductCategory(category){
			vm.product.category = category;
			
			$http.post("/product", vm.product).success(loadRandomProduct);
		}
	}
})();