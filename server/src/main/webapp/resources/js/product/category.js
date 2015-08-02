var app = angular.module("KubikProductCategory", []);
var categoryId = window.location.pathname.split("/")[2];

app.controller("KubikProductCategoryController", function($scope, $http){
	$scope.addSubCategory = function(){
		if($scope.category.subCategories == undefined){
			$scope.category.subCategories = [];
		}
		
		var subCategory = { category : $scope.category.id};
		$scope.category.subCategories.push(subCategory);
		
		$scope.editSubCategory(subCategory);
	};

	$scope.confirmDeleteSubCategory = function(subCategory, $event){
		$event.stopPropagation();
		
		$http.get("/product?subCategory=" + subCategory.id).success(function(productCount){
			$scope.subCategory = subCategory;
			$scope.productCount = productCount;
			
			$(".confirm-delete-sub-category-modal").modal();
			
		});
	};
	
	$scope.deleteSubCategory = function(subCategory){
		$scope.loading = true;
		
		$scope.category.subCategories.splice($scope.category.subCategories.indexOf(subCategory), 1);
		
		$scope.save(function(){
			$http.delete("/subCategory/" + subCategory.id).success(function(){
				$(".confirm-delete-sub-category-modal").modal("hide");
				
				$scope.loadCategory();
			}).finally(function(){
				$scope.loading = false;
			});
		});
	}
	
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
	
	$scope.save = function(success){
		$scope.hideAlerts();
		
		$scope.saving = true;
		
		$http.post("/category", $scope.category).success(function(category){
			$scope.category = category;
			
			$scope.completed = true;
			
			if(success != undefined){
				success();
			}
		}).error(function(){
			$scope.error = true;
		}).finally(function(){
			$scope.saving = false;
		});
	}
	
	$scope.loadCategory();
});