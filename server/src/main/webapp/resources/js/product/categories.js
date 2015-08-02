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
	
	$scope.confirmDeleteCategory = function(category, $event){
		$event.stopPropagation();
		
		$http.get("/product?category=" + category.id).success(function(productCount){
			$scope.category = category;
			$scope.productCount = productCount;
			
			$(".confirm-delete-category-modal").modal();
			
		});
	};
	
	$scope.deleteCategory = function(category){
		$scope.loading = true;
		
		$http.delete("/category/" + category.id).success(function(){
			$(".confirm-delete-category-modal").modal("hide");
			
			$scope.loadCategories();
		}).finally(function(){
			$scope.loading = false;
		});
	}
	
	$scope.loadCategories = function(successCallback){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};
		
		$scope.loading = true;
		$http.get("category?" + $.param(params)).success(function(categoriesPage){
			$scope.categoriesPage = categoriesPage;
			
			if(successCallback != undefined){
				successCallback();
			}
		}).finally(function(){
			$scope.loading = false;
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