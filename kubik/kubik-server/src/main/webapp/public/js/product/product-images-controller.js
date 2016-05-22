(function(){
	var PRODUCT_URL = "/product";
	
	angular
		.module("Kubik")
		.controller("ProductImagesCtrl", ProductImagesCtrl);
	
	function ProductImagesCtrl(uploadFileService, $scope, $http, $timeout){
		var vm = this;

		vm.downloadingImages = false;
		vm.downloadImageFromAmazon = downloadImageFromAmazon;
		vm.downloadImageFromDilicom = downloadImageFromDilicom;
		vm.downloadImageFromUrl = downloadImageFromUrl;
		vm.uploadCustomImage = uploadCustomImage;
		
		$scope.$on("productImages-setProduct", setProductEvent);
		
		function setProductEvent($event, product){
			vm.url = "";
			vm.product = product;
			
			vm.cacheKey = Math.random();
		}
		
		function downloadImage(provider){
			vm.downloadingImages = true;
			
			var url = PRODUCT_URL + "/" + vm.product.id + "/image/" + provider;
			if(provider == "url"){
				url += "?url=" + vm.url; 
			}
			
			$http
				.get(url)
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
		
		function downloadImageFromUrl(){
			downloadImage("url");
		}
		
		function uploadCustomImage(){
			vm.downloadingImages = true;
			
			var url = PRODUCT_URL + "/" + vm.product.id + "/image/custom", uploadCustomImageSuccess;
			
			uploadFileService.uploadFile(vm.image, url, null, null, uploadCustomImageCompleted);
			
			function uploadCustomImageCompleted(){
				vm.downloadingImages = false;
				
				vm.cacheKey = Math.random();
			}
		}
	}
})();