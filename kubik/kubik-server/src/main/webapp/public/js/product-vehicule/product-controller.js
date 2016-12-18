(function(){
	angular
	    .module("KubikProductVehicule")
	    .controller("ProductsPageCtrl", ProductsPageCtrl);
	
	function ProductsPageCtrl(productService, $scope){
		var vm = this;
		
		vm.newProduct = newProduct;
		vm.openProductCard = openProductCard;
		vm.query = query;
		
		vm.params = {};
		
		query();
		
		$scope.$on("productSaved", query);
		
		function newProduct(){
			$scope.$broadcast("openProductCard", {});
		}
		
		function openProductCard($event, product){
			$scope.$broadcast("openProductCard", product);
		}
		
		function query(){
			return productService
			    .query(vm.params)
			    .then(querySuccess);
			
			function querySuccess(productsPage){
				vm.productsPage = productsPage;
			}
		}
	}
})();