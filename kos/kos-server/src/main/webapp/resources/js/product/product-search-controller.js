(function(){
	var PRODUCT_URL = "/product";

	var SEARCH_PARAMS = [	"page", "size", "direction", "title", "author", 
	                     	"manufacturer", "publishFrom", "publishUntil", 
	                     	"priceFrom", "priceTo", "orderBy","query"];
	
	var $affix = $('[data-smart-affix]');
	var $body = $("body");
	var $header = $("header");
	var $menu = $(".menu");
	
	var bodyPadding = parseInt($("body").css("padding-top").replace("px", ""));
	
	$(window).on("resize", setAffixOffset);
	
	$('[data-spy]').affix({
	    offset: {
	        top: $('[data-spy]').offset().top
	    }
	});
		
	angular
		.module("kos")
		.controller("ProductSearchCtrl", ProductSearchCtrl);
	
	function ProductSearchCtrl($scope, $location, $timeout, categoryService, productService){
		var vm = this;
		
		vm.categoriesChanged = categoriesChanged;
		vm.changePage = changePage;
		vm.openProductPage = openProductPage;
		vm.triggerSearchEvent = triggerSearchEvent;

		loadCategories()
			.then(loadCategoriesSuccess);

		$scope.$on("menu-loaded", onMenuLoaded);
		
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
				var categoriesMap = {};
				vm.categories = [];
				
				angular.forEach(rootCategories, onCategory);
				
				parseLocationParameters();

				$timeout(function(){
					$(".categories-select").select2({
						templateResult : templateResult
					});
					
					function templateResult(state){
						if (!state.id)
							return state.text;
						
						var category = categoriesMap[state.text];
						return $("<span class=\"level-" + category.level + "\">" + category.name + "</span>");
					}
				});
				
				function onCategory(rootCategory, index){
					addCategoryToList(rootCategory, 1);
				}
				
				function addCategoryToList(category, level){
					category.level = level;
					vm.categories.push(category);
					categoriesMap[category.name] = category;
					
					angular.forEach(category.childCategories, onChildCategory);
					
					function onChildCategory(childCategory, index){
						addCategoryToList(childCategory, level + 1);
					}
				}
			}
		}
		
		function loadCategoriesSuccess(){
			search(true);
		}
		
		function onMenuLoaded(){
			setAffixOffset();
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
				
				function onCategoryParam(categoryName, paramIndex){					
					if(category.name == categoryName){
						vm.searchParams.categories[paramIndex] = category.name;
						
						if(category.rootCategory){
							$scope.$broadcast("setActiveTab", category.name);
						}
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
	
	function setAffixOffset(){
		$affix.each(function(){
			$(this).data('bs.affix').options.offset = bodyPadding + $header.outerHeight() + $menu.outerHeight(); 
		});
	}
})();