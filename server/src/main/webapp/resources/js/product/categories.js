var app = angular.module("KubikProductCategories", []);

app.controller("KubikProductCategoriesController", function($scope, $http){
	$scope.addCategory = function(){
		$http.post("/category", {}).success(function(category){
			$scope.openCategory(category);
		});
	};

	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadCategories();
	};
	
	$scope.loadCategories = function(successCallback){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};
		
		$http.get("category?" + $.param(params)).success(function(categoriesPage){
			$scope.categoriesPage = categoriesPage;
			
			if(successCallback != undefined){
				successCallback();
			}
		});
	};
	
	$scope.openCategory = function(category){
		window.location.href = "/category/" + category.id;
	}
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "name";
	$scope.direction = "ASC";
	
	$scope.loadCategories();
});