(function(){
	angular
		.module("Kubik")
		.controller("ProductCategoriesCtrl", ProductCategoriesCtrl);
	
	angular
		.module("KubikProductVehicule")
		.controller("ProductCategoriesCtrl", ProductCategoriesCtrl);
	
	function ProductCategoriesCtrl(categoryService, $scope, $http, $timeout){
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
				vm.categorySelectedCallback = categorySelected;
		});
		
		function addCategory(parentCategory){
			if(parentCategory.childCategories == undefined)
				parentCategory.childCategories = [];
			
			categoryService
				.newCategory()
				.then(newCategoryNameLoaded);
			
			function newCategoryNameLoaded(name){
				parentCategory.childCategories.push({name : name, rootCategory : false});
				
				return categoryService
					.save(parentCategory)
					.then(vm.loadCategories);
			}
		}
		
		function addRootCategory(){
			categoryService
				.newCategory()
				.then(newCategoryNameLoaded);
			
			function newCategoryNameLoaded(name){
				categoryService
					.save({name : name, rootCategory : true})
					.then(vm.loadCategories());
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
			
			categoryService
				.deleteProductCategories(category.id)
				.then(productCategoriesDeleted);
			
			function productCategoriesDeleted(){
				if(category.rootCategory){
					return categoryService
						.deleteCategory(category.id)
						.error(handleError)
						.finally(deleteCompleted);
				}else{
					parentCategory.childCategories.splice(parentCategory.childCategories.indexOf(category), 1);
					
					return categoryService
						.save(parentCategory)
						.error(handleError).finally(deleteCompleted);
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
			
			categoryService
				.findAll()
				.then(findAllSuccess)
				.finally(findAllCompleted);
			
			function findAllCompleted(){
				vm.loading = false;
			}
			
			function findAllSuccess(categories){
				vm.categories = categories;
			
				angular.forEach(categories, onEach);
							
				if(successCallback != undefined)
					successCallback();
				
				function calculateLevel(category, level){
					category.level = level;
	
					angular.forEach(category.childCategories, onEach);
					
					function onEach(childCategory, key){
						vm.childParentCategoriesMap[childCategory.id] = category;
						childCategory.parentCategory = { id : category.id};
						calculateLevel(childCategory, level + 1);
					}
				}
				
				function onEach(category, key){
					category.level = 1;
	
					angular.forEach(category.childCategories, onEach);
					
					function onEach(childCategory, key){
						vm.childParentCategoriesMap[childCategory.id] = category;
						childCategory.parentCategory = { id : category.id};
						calculateLevel(childCategory, 2);
					}
				}
			}
		};
		
		function openModal(options){
			$modal
				.on("show.bs.modal", onShow)
				.modal({backdrop : "static", keyboard : false});
			
			function onShow(event){
				$timeout(onTimeout);
				
				function onTimeout(){
					if(options != undefined) 
						vm.categorySelectedCallback = options.categorySelected;
				}
			}
		}
		
		function saveCategory(){
			vm.hideError();
			
			categoryService
				.save(vm.category)
				.error(saveError)
				.finally(saveCompleted);
			
			function saveError(data){
				vm.error = data.message;
			}
			
			function saveCompleted(){
				$(".edit-category-modal").modal("hide");
				
				vm.loadCategories();
			}
		}
	}
})();