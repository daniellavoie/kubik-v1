(function(){
	angular
		.module("Kubik")
		.controller("ProductCategoriesCtrl", ProductCategoriesCtrl);
	
	function ProductCategoriesCtrl($scope, $http, $timeout){
		var CATEGORIES_URL = "/category";
		var $modal = $(".categories-modal");
		
		var vm = this;
		
		vm.childParentCategoriesMap = {};
		vm.categoriesTree = {};
		vm.categories = [];
		
		loadCategories();
		$scope.$emit("categoriesLoaded", vm);
		
		vm.addCategory = addCategory;
		vm.addRootCategory = addRootCategory;
		vm.cancelEditCategory = cancelEditCategory;
		vm.categoryHovered = categoryHovered;
		vm.categorySelected = categorySelected;
		vm.closeModal = closeModal;
		vm.confirmDeleteCategory = confirmDeleteCategory;
		vm.deleteCategory = deleteCategory;
		vm.editCategory = editCategory;
		vm.hideError = hideError;
		vm.loadCategories = loadCategories;
		vm.openModal = openModal;
		vm.saveCategory = saveCategory;
		
		$scope.$on("closeProductCategoriesModal", function(event){
			vm.closeModal();
		});
		
		$scope.$on("openProductCategories", function(event, options){
			vm.openModal(options);
		});
		
		$scope.$on("updateCategorySelectedCallback", function($event, categorySelected){
//			$timeout(function(){
				vm.categorySelectedCallback = categorySelected;
//			});
		});
		
		function addCategory(parentCategory){
			if(parentCategory.childCategories == undefined){
				parentCategory.childCategories = [];
			}
			
			$http.get(CATEGORIES_URL + "?newName").success(newCategoryNameLoaded);
			
			function newCategoryNameLoaded(name){
				parentCategory.childCategories.push({name : name, rootCategory : false});
				
				$http.post(CATEGORIES_URL, parentCategory).success(vm.loadCategories);
			}
		}
		
		function addRootCategory(){
			$http.get(CATEGORIES_URL + "?newName").success(newCategoryNameLoaded);
			
			function newCategoryNameLoaded(name){
				$http.post(CATEGORIES_URL, {name : name, rootCategory : true}).success(vm.loadCategories());
			}
		}
		
		function cancelEditCategory(){
			vm.loadCategories();
			vm.error = null;
	
			$(".edit-category-modal").modal("hide");
		}
		
		function categoryHovered(category, $event){
			$event.stopPropagation();
			
			vm.hoveredCategory = category;			
		}
		
		function categorySelected(category, $event){
			$event.stopPropagation();
			
			$scope.$emit("categorySelectedCallback", category);
			
			if(vm.categorySelectedCallback != undefined){
				vm.categorySelectedCallback(category);
			}
		}
		
		
		function closeModal(){
			$modal.modal("hide");
		}
		
		function confirmDeleteCategory(category, $event){
			$event.stopPropagation();
			
			$http.get("/product?category=" + category.id).success(function(productCount){
				vm.category = category;
				vm.productCount = productCount;
				
				$(".confirm-delete-category-modal").modal();
				
			});
		}
		
		function deleteCategory(category){
			vm.loading = true;
			
			var parentCategory = vm.childParentCategoriesMap[category.id];
			
			$http.delete(CATEGORIES_URL + "/" + category.id + "/product").success(productCategoriesDeleted);
			
			function productCategoriesDeleted(){
				if(category.rootCategory){
					$http.delete(CATEGORIES_URL + "/" + category.id).error(handleError).finally(deleteCompleted);
				}else{
					parentCategory.childCategories.splice(parentCategory.childCategories.indexOf(category), 1);
					
					$http.post(CATEGORIES_URL, parentCategory).error(handleError).finally(deleteCompleted);
				}
				
				function handleError(data){
					vm.error = data.message;
				}
				
				function deleteCompleted(){
					$(".confirm-delete-category-modal").modal("hide");
					
					vm.loading = false;
					
					vm.loadCategories();
				}
			}
		}
		
		function editCategory(category){
			vm.category = category;
	
			$(".edit-category-modal").modal();
		}
		
		function hideError(){
			vm.error = null;
		}
		
		function loadCategories(successCallback){
			vm.loading = true;
			
			$http.get(CATEGORIES_URL).success(categoriesLoaded).finally(function(){
				vm.loading = false;
			});
			
			function categoriesLoaded(categories){
				vm.categories = categories;
	
				var calculateLevel = function(category, level){
					category.level = level;
	
					angular.forEach(category.childCategories, function(childCategory, key){
						vm.childParentCategoriesMap[childCategory.id] = category;
						calculateLevel(childCategory, level + 1);
					});
				};
			
				angular.forEach(categories, function(category, key ){
					category.level = 1;
	
					angular.forEach(category.childCategories, function(childCategory, key){
						vm.childParentCategoriesMap[childCategory.id] = category;
						calculateLevel(childCategory, 2);
					});
				});
							
				if(successCallback != undefined){
					successCallback();
				}
			}
		};
		
		function openModal(options){
			$modal.on("show.bs.modal", function(e){
				$timeout(function(){
					if(options != undefined) vm.categorySelectedCallback = options.categorySelected;
				});
			}).modal({
				backdrop : "static",
				keyboard : false
			});
		}
		
		function saveCategory(category){
			vm.hideError();
			
			$http.post(CATEGORIES_URL, vm.category).error(handleError).finally(saveCompleted);
			
			function handleError(data){
				vm.error = data.message;
			}
			
			function saveCompleted(){
				$(".edit-category-modal").modal("hide");
				
				vm.loadCategories();
			}
		}
	}
})();