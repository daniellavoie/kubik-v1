(function(){
	angular
		.module("Kubik")
		.controller("ProductDoublesListCtrl", ProductDoublesListCtrl);
	
	function ProductDoublesListCtrl(productDoublesService){
		var vm = this;
		
		vm.openProductDoubles = openProductDoubles;
		
		loadProductDoubles();
		
		function loadProductDoubles(){
			return productDoublesService
				.findAll().then(findAllSuccess);
			
			function findAllSuccess(productDoublesList){
				vm.productDoublesList = productDoublesList;
			}
		}
		
		function openProductDoubles(productDoubles){
			location.href = "/product-doubles/" + productDoubles.ean13;
		}
	}
})();