(function(){
	var PRODUCT_URL = "/product";

	angular
		.module("Kubik")
		.controller("ProductImagesCtrl", ProductImagesCtrl);
	
	angular
		.module("KubikProductVehicule")
		.controller("ProductImagesCtrl", ProductImagesCtrl);
	
	function ProductImagesCtrl(productImageService, $scope, $http, $timeout){
		var vm = this;

		vm.downloadingImages = false;
		vm.downloadImageFromAmazon = downloadImageFromAmazon;
		vm.downloadImageFromDilicom = downloadImageFromDilicom;
		vm.downloadImageFromUrl = downloadImageFromUrl;
		vm.uploadCustomImage = uploadCustomImage;
		
		$scope.$on("productImages-setProductEan13", setProductEan13Event);
		$scope.$on("productImages-validate", validateImageEvent)
		
		function setProductEan13Event($event, ean13){
			vm.url = "";
			vm.preview = false;
			vm.ean13 = ean13;
			
			vm.cacheKey = Math.random();
		}
		
		function downloadImage(provider, remoteUrl){
			vm.downloadingImages = true;
			
			productImageService.downloadImage(vm.ean13, provider, remoteUrl)
				.then(downloadImageSuccess)
				.finally(downloadImageCompleted);
			
			function downloadImageCompleted(){
				vm.preview = true;
				vm.cacheKey = Math.random();
				
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
			downloadImage("url", vm.url);
		}
		
		function uploadCustomImage(){
			vm.downloadingImages = true;
			
			productImageService.uploadCustomImage(vm.ean13, vm.image, uploadCustomImageCompleted);
			
			function uploadCustomImageCompleted(){
				vm.preview = true;
				
				vm.downloadingImages = false;
				
				vm.cacheKey = Math.random();
			}
		}
		
		function validateImageEvent($event){
			productImageService.validateImage(vm.ean13).then(validateImageSuccess);
			
			function validateImageSuccess(){
				$scope.$emit("productImages-image-validated");
			}
		}
	}
})();