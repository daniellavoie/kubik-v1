(function(){
	var ean13 = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("ProductDoublesCtrl", ProductDoublesCtrl);
	
	function ProductDoublesCtrl(productDoublesService, productService, $scope){
		var vm = this;
		
		vm.mergeProducts = mergeProducts;
		vm.openProductCard = openProductCard;
		
		loadProductDoubles();
		
		function loadProductDoubles(){
			productDoublesService
				.findOne(ean13)
				.then(findOneSuccess);
			
			function findOneSuccess(productDoubles){
				vm.productDoubles = productDoubles;
			}
		}
		
		function mergeProducts(){
			var targetProduct = Stream(vm.productDoubles.products)
				.filter({id : vm.selectedProductId})
				.findAny()
				.get();
			
			Stream(vm.productDoubles.products)
				.filter(onFilter)
				.forEach(onProductToMerge);
			
			location.href = "/product-doubles";
			
			function onFilter(product){
				return product.id != vm.selectedProductId;
			}
			
			function onProductToMerge(product){
				productService
					.mergeProducts(product, targetProduct);
			}
		}
		
		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
	}
})();