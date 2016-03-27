(function() {
	var STOCKTAKING_CATEGORY_URL = "/stocktaking-category";

	angular
		.module("Kubik")
		.factory("stocktakingCategoryService",
			StocktakingCategoryService);

	function StocktakingCategoryService($http) {
		return {
			save : save
		};

		function save(stocktakingCategory) {
			return $http.post(STOCKTAKING_CATEGORY_URL, stocktakingCategory)
					.then(saveSuccess);

			function saveSuccess(response) {
				return response.data;
			}
		}
	}
})();