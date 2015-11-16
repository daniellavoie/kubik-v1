(function(){
	var CATEGORIES = ["BD", "Produits dérivés", "Romans"];
	
	angular
		.module("kos")
		.controller("MenuCtrl", MenuCtrl);
	
	function MenuCtrl($timeout, categoryService){
		var vm = this;
		
		loadMenus();
		
		vm.navigate = navigate;
		vm.mouseEntersCategoryMenu = mouseEntersCategoryMenu;
		vm.mouseEntersCategorySubmenu = mouseEntersCategorySubmenu;
		vm.mouseLeavesCategoryMenu = mouseLeavesCategoryMenu;
		vm.openCategory = openCategory;
		
		function loadMenus(){
			angular.forEach(CATEGORIES, onCategory);
			
			function onCategory(categoryName, index){
				vm.categories = [];
				
				categoryService.findByName(categoryName).then(findByNameSuccess);
				
				function findByNameSuccess(category){
					vm.categories.push(category);
					
					if(vm.categories.length == CATEGORIES.length){
//						$('.grid').isotope({
//						  itemSelector: '.grid-item',
//						  layoutMode: 'fitRows'
//						});
					}
				}
			}
		}
		
		function mouseEntersCategoryMenu($event, category){
			vm.category = category;
			
			if(vm.hideCategorySubmenuTimer != null){
				clearTimeout(vm.hideCategorySubmenuTimer);
			}
		}
		
		function mouseEntersCategorySubmenu($event){
			if(vm.hideCategorySubmenuTimer != null){
				clearTimeout(vm.hideCategorySubmenuTimer);
			}
		}
		
		function mouseLeavesCategoryMenu($event){
			vm.hideCategorySubmenuTimer = setTimeout(hideCategorySubmenu, 200);
			
			function hideCategorySubmenu(){
				$timeout(function(){
					vm.category = null;
				});
			}
		}
		
		function navigate(url){
			location.href = url;
		}
		
		function openCategory(category){
			navigate("/product?categories=" + category.id);
		}
	}
})();