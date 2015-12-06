(function(){
	var CATEGORIES = ["BD", "Produits dérivés", "Romans"];
	
	angular
		.module("kos")
		.controller("MenuCtrl", MenuCtrl);
	
	function MenuCtrl($scope, $timeout, categoryService){
		var vm = this;
		
		loadMenus();
		
		vm.mouseEntersCategoryMenu = mouseEntersCategoryMenu;
		vm.mouseEntersCategorySubmenu = mouseEntersCategorySubmenu;
		vm.mouseLeavesCategoryMenu = mouseLeavesCategoryMenu;
		vm.mouseLeavesCategorySubmenu = mouseLeavesCategorySubmenu;
		
		$scope.$on("setActiveTab", setActiveTabEvent);
		
		function loadMenus(){
			angular.forEach(CATEGORIES, onCategory);
			
			function onCategory(categoryName, index){
				vm.categories = [];
				
				categoryService.findByName(categoryName).then(findByNameSuccess);
				
				function findByNameSuccess(category){
					vm.categories.push(category);
					
					if(vm.categories.length == CATEGORIES.length){
						$scope.$emit("menu-loaded");
					}
				}
			}
		}
		
		function mouseEntersCategoryMenu($event, category){
			vm.category = category;
			vm.hoveredTab = category.name;
			
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
					vm.hoveredTab = null;
				});
			}
		}
		
		function mouseLeavesCategorySubmenu(){
			vm.category = null;
			vm.hoveredTab = null;
		}
		
		function setActiveTabEvent($event, activeTab){
			vm.activeTab = activeTab;
		}
	}
})();