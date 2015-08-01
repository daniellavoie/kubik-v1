var app = angular.module("KubikProductCategory", []);
var categoryId = window.location.pathname.split("/")[2];

app.controller("KubikProductCategoryController", function($scope, $http){
	$scope.addSubCategory = function(){
		if($scope.category.subCategories == undefined){
			$scope.category.subCategories = [];
		}
		
		var subCategory = { category : {id : $scope.category.id}};
		$scope.category.subCategories.push(subCategory);
		
		$scope.editSubCategory(subCategory);
	};
	
	$scope.confirmDeleteSubCategory = function(subCategory, $event){
		$event.stopPropagation();
		
		$scope.subCategory = subCategory;
		
		$(".confirm-delete-sub-category-modal").modal();
	};
	
	$scope.deleteSubCategory = function(subCategory){
		$scope.category.subCategories.splice($scope.category.subCategories.indexOf(subCategory), 1);
		
		$(".confirm-delete-sub-category-modal").modal("hide");
	};
	
	$scope.editSubCategory = function(subCategory){		
		$scope.subCategory = subCategory;
		
		$(".edit-sub-category-modal").modal();
	};
	
	$scope.hideAlerts = function(){
		$scope.error = false;
		$scope.completed = false;
	};
	
	$scope.loadCategory = function(){
		$http.get("/category/" + categoryId).success(function(category){
			$scope.category = category;
		});
	};
	
	$scope.save = function(){
		$scope.hideAlerts();
		
		$scope.saving = true;
		
		$http.post("/category", $scope.category).success(function(category){
			$scope.category = category;
			
			$scope.completed = true;			
		}).error(function(){
			$scope.error = true;
		}).finally(function(){
			$scope.saving = false;
		});
	}
	
	$scope.loadCategory();
});