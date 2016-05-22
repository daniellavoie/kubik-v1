(function(){
	var PRODUCT_URL = "/product";
	
	angular
		.module	("Kubik")
		.factory("uploadFileService", UploadFileService);
	
	function UploadFileService($http, $timeout){
		return {
			uploadFile : uploadFile 
		};
		
		function uploadFile(file, url, success, error, complete){
			var fd = new FormData();
	        fd.append('file', file);
	        
	        var request = new XMLHttpRequest();
	        
	        request.onreadystatechange = onReadyStateChange;
	        
	        try {
		        request.open('POST', url);
		        request.send(fd);
	        } catch (ex){
            	if(error != undefined)
            		$timeout(error);
	        } 
	        
	        function onReadyStateChange(){
	        	if(request.readyState == 4){
	                try {	                	
	                	if(request.status == 204 || request.status == 200)
		                	if(success != undefined)
		                		$timeout(success);
	                	else
		                	if(error != undefined)
		                		$timeout(error);
	                	
	                	if(complete != undefined)
	                		$timeout(complete);
	                } catch (ex){
	                	if(error != undefined)
	                		$timeout(error);
	                } 
	            }
	        }
		}
	}
})();