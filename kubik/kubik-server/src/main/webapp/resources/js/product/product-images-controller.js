(function(){
	var PRODUCT_URL = "/product";
	
	angular
		.module("Kubik")
		.controller("ProductImagesCtrl", ProductImagesCtrl);
	
	function ProductImagesCtrl(uploadProductImageService, $scope, $http, $timeout){
		var vm = this;

		vm.downloadingImages = false;
		vm.downloadImageFromAmazon = downloadImageFromAmazon;
		vm.downloadImageFromDilicom = downloadImageFromDilicom;
		vm.uploadCustomImage = uploadCustomImage;
		
		$scope.$on("productImages-setProduct", setProductEvent);
		
		function setProductEvent($event, product){
			vm.product = product;
		}
		
		function downloadImage(provider){
			vm.downloadingImages = true;
			
			$http
				.get(PRODUCT_URL + "/" + vm.product.id + "/image/" + provider)
				.then(downloadImageSuccess)
				.finally(downloadImageCompleted);
			
			function downloadImageCompleted(){
				vm.downloadingImages = false;
			}
			
			function downloadImageSuccess(response){
				vm.cacheKey = Math.random();
			}
		}
		
		function downloadImageFromAmazon(){
			downloadImage("amazon");
		}
		
		function downloadImageFromDilicom(){
			downloadImage("dilicom");
		}
		
		function uploadCustomImage(){
			vm.downloadingImages = true;
			
			uploadProductImageService
				.uploadProductImage(vm.image, vm.product.id, uploadCustomImageSuccess, null, uploadCustomImageCompleted);
			
			function uploadCustomImageCompleted(){
				vm.downloadingImages = false;
			}
			
			function uploadCustomImageSuccess(){
				vm.cacheKey = Math.random();
			}
		}
	}
})();