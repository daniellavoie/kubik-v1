(function(){
	angular
		.module("Kubik", [])
		.controller("MenuCtrl", MenuCtrl);
	
	function MenuCtrl($scope, $http){
		var vm = this;
		
		$http.get("/notification/NEW/count").success(function(notificationCount){
			vm.notificationCount = notificationCount;
		});
		
		$http.get("/product?category").success(function(productsWithoutCategory){
			vm.productsWithoutCategory = productsWithoutCategory;
		});
		
		$http.get("/product?nonValidatedProductImages&count").success(function(nonValidatedImages){
			vm.nonValidatedImages = nonValidatedImages;
		});
		
		$http.get("/restock/OPEN/count").success(function(restockCount){
			vm.restockCount = restockCount;
		});
	}
})();
