var app = angular.module("KubikNonCategorizedProduct", []);

app.controller("KubikNonCategorizedProductController", function($scope, $http){
	$scope.categoryChanged = function(){
		$scope.subCategories = $scope.category.subCategories;
		
		$scope.product.subCategory = $scope.subCategories[0];
	};
	
	$scope.loadRandomProduct = function(){
		$http.get("/product?subCategory&random").success(function(product){			
			$scope.product = product;

			$scope.category = $scope.categories[0];
			$scope.subCategories = $scope.category.subCategories;
			$scope.product.subCategory = $scope.subCategories[0];
		});
	};
	
	$scope.loadCategories = function(success){
		$http.get("/category").success(function(categories){
			$scope.categories = categories;
			
			$scope.category = $scope.categories[0];
			
			if(success != undefined){
				success();
			}
		});
	};
	
	$scope.pass = function(){
		$scope.loadRandomProduct();
	};
	
	$scope.save = function(){
		$scope.product.subCategory.category = null;
		$http.post("/product", $scope.product).success(function(product){
			$scope.loadRandomProduct();
		})
	}
	
	$scope.loadCategories(function(){
		$scope.loadRandomProduct();
	});
	
});