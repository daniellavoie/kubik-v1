(function(){
	var ERROR_TRACE_URL = "/error"
	angular
		.module("Kubik")
		.factory("errorTraceService", ErrorTraceService);
	
	function ErrorTraceService($http){
		return {
			postError : postError
		};
		
		function postError(error){
			return $http
				.post(ERROR_TRACE_URL, error)
				.then(postErrorSuccess);
			
			function postErrorSuccess(response){
				return response.data;
			};
		}
	}
})();