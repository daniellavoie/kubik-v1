(function(){
	angular
		.module("Kubik")
		.controller("NonValidatedProductImagesCtrl", NonValidatedProductImagesCtrl);
	
	function NonValidatedProductImagesCtrl($scope, $http, $timeout){
		var vm = this;
		
		loadRandomProduct();

		vm.pass = pass;
		vm.validate = validate;
		
		function loadRandomProduct(){
			$http.get("/product?nonValidatedProductImages&random").success(loadRandomProductSuccess);
			
			function loadRandomProductSuccess(product){
				vm.product = product;
				
				$scope.$broadcast("productImages-setProduct", product);
			}
		}
		
		function pass(){
			loadRandomProduct();
		}
		
		function validate(){
			vm.product.imagesValidated = true;
			
			$http.post("/product", vm.product).success(loadRandomProduct);
		}
	}
})();