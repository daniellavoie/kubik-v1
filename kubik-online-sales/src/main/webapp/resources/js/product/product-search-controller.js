(function(){
	var PRODUCT_URL = "/product";

	var SEARCH_PARAMS = [	"page", "size", "direction", "title", "author", 
	                     	"manufacturer", "publishFrom", "publishUntil", 
	                     	"priceFrom", "priceTo", "orderBy","query"];
	
	angular
		.module("kos")
		.config(Config)
		.controller("ProductSearchCtrl", ProductSearchCtrl);
		
	function Config($locationProvider){
		$locationProvider.html5Mode(true);
	}
	
	function ProductSearchCtrl($scope, $location, $timeout, categoryService, productService){
		var vm = this;
		
		vm.categoriesChanged = categoriesChanged;
		vm.changePage = changePage;
		vm.openProductPage = openProductPage;
		vm.triggerSearchEvent = triggerSearchEvent;

		loadCategories()
			.then(loadCategoriesSuccess);
		
		function categoriesChanged(){
			vm.searchParams.page = 0;
			search();
		}
		
		function changePage(page, skipScroll){
			vm.searchParams.page = page;
			
			search(skipScroll);
		}
		
		function loadCategories(){
			return categoryService.findRootCategories().then(findRootCategorySuccess);
			
			function findRootCategorySuccess(rootCategories){
				vm.categories = [];
				
				angular.forEach(rootCategories, onCategory);
				
				parseLocationParameters();

				$timeout(function(){
					$(".categories-select").select2();					
				});
				
				function onCategory(rootCategory, index){
					addCategoryToList(rootCategory, 1);
				}
				
				function addCategoryToList(category, level){
					category.level = level;
					vm.categories.push(category);
					
					angular.forEach(category.childCategories, onChildCategory);
					
					function onChildCategory(childCategory, index){
						addCategoryToList(childCategory, index + 1);
					}
				}
			}
		}
		
		function loadCategoriesSuccess(){
			search(true);
		}
		
		function openProductPage(product){
			location.href = "/product/" + product.id;
		}
		
		function parseLocationParameters(){
			vm.searchParams = $location.search();
			
			if(vm.searchParams.categories != null){
				if(vm.searchParams.categories.constructor !== Array)
					vm.searchParams.categories = [vm.searchParams.categories];
				
				angular.forEach(vm.categories, onCategory);
			}
			
			function onCategory(category, categoryIndex){
				angular.forEach(vm.searchParams.categories, onCategoryParam);
				
				function onCategoryParam(categoryId, paramIndex){
					if(category.id == categoryId){
						vm.searchParams.categories[paramIndex] = category.id;
					}

					return;
				}
			}
		}
		
		function search(skipScroll){
			vm.loading = true;
			
			if(!skipScroll){
				$('html, body').animate({
			        scrollTop: $(".pager-top").offset().top
			    }, 1000);
			}
			var parameters = {};
			angular.forEach(SEARCH_PARAMS, onParam);
			
			if(vm.searchParams.categories != undefined){
				parameters.categories = [];
				
				angular.forEach(vm.searchParams.categories, onCategory);
				
				$location.search("categories", parameters.categories);
			}
			
			productService.search(parameters).then(searchSuccess).finally(searchCompleted);
			
			function computeParameter(name){
				var parameter = vm.searchParams[name];
				if(parameter != undefined){
					parameters[name] = parameter;
					$location.search(name, parameter);
				}
			}
			
			function onCategory(category, index){
				parameters.categories.push(category);
			}
			
			function onParam(param, index){
				computeParameter(param);
			}
			
			function searchCompleted(){
				vm.loading = false;
			}
			
			function searchSuccess(productPage){
				vm.productPage = productPage;

				$scope.$emit("updateTitle", productPage.totalElements + " produit(s) retrouvé(s)");
			}
		}
		
		function triggerSearchEvent($event){
			search();
		}
	}
})();