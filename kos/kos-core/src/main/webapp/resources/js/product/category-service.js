(function(){
	var CATEGORY_URL = "/category";
	
	angular
		.module("kos")
		.factory("categoryService", CategoryService);
	
	function CategoryService($http){
		return {
			findByName : findByName,
			findRootCategories : findRootCategories
		};
		
		function findByName(name){
			return $http
				.get(CATEGORY_URL + "?name=" + name)
				.then(findByNameSuccess);
			
			function findByNameSuccess(response){
				return response.data;
			}
		}
		
		function findRootCategories(){
			return $http
				.get(CATEGORY_URL + "?rootCategory=true")
				.then(findRootCategoriesSuccess);
			
			function findRootCategoriesSuccess(response){
				return response.data;
			}
		}
	}
})();