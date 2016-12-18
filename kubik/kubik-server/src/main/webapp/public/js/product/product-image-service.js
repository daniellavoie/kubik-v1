(function(){
	var PRODUCT_URL = "/product";
	
	angular
        .module("Kubik")
        .factory("productImageService", ProductImageService);

	angular
	    .module("KubikProductVehicule")
	    .factory("productImageService", ProductImageService);
	
	function ProductImageService(uploadFileService, $http){
		return {
			downloadImage : downloadImage,
			uploadCustomImage : uploadCustomImage,
			validateImage : validateImage
		};
		
		function downloadImage(productId, provider, remoteUrl){
			var url = PRODUCT_URL + "/" + productId + "/image/" + provider;
			if(provider == "url"){
				url += "?url=" + remoteUrl; 
			}
			
			return $http.get(url);
		}
		
		function uploadCustomImage(ean13, image, completeCallback){
			var url = PRODUCT_URL + "/ean13/" + ean13 + "/image/custom";
			
			uploadFileService.uploadFile(image, url, null, null, completeCallback);
		}
		
		function validateImage(ean13){
			return $http.post(PRODUCT_URL + "/ean13/" + ean13 + "/image?validate");
		}
	}
})();