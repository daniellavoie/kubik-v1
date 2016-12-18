(function(){
	var CATEGORY_URL = "/category";
	
	angular
		.module("Kubik")
		.factory("categoryService", CategoryService);

	angular
		.module("KubikProductVehicule")
		.factory("categoryService", CategoryService);
	
	function CategoryService($http){
		return {
			deleteCategory : deleteCategory,
			deleteProductCategories : deleteProductCategories,
			findAll : findAll,
			findOne : findOne,
			newCategory : newCategory,
			save : save
		};
		
		function deleteCategory(categoryId){
			return $http.delete(CATEGORIES_URL + "/" + categoryId);
		}
		
		function deleteProductCategories(categoryId){
			return $http
				delete(CATEGORY_URL + "/" + categoryId + "/product");
		}
		
		function findAll(){
			return $http
				.get(CATEGORY_URL)
				.then(getSuccess);
			
			function getSuccess(response){
				return response.data;
			}
		}
		
		function findOne(id){
			return $http
				.get(CATEGORY_URL + "/" + id)
				.then(getSuccess);
			
			function getSuccess(response){
				return response.data;
			}
		}
		
		function newCategory(){
			return $http
				.get(CATEGORY_URL + "?newName")
				.then(getSuccess);
			
			function getSuccess(response){
				return response.data;
			}
		}
		
		function save(category){
			return $http
				.post(CATEGORY_URL, category)
				.then(postSuccess);
			
			function postSuccess(response){
				return response.data;
			}
		}

	}
})();