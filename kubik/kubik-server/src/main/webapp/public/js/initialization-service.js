(function(){
	angular
		.module("Kubik")
		.factory("initializationService", InitializationService);
	
	function InitializationService($http){
		return {
			initialize : initialize,
			loadLogo : loadLogo
		};
		
		function initialize(){
			return $http.post("/initialization");
		}
		
		function loadLogo(){
			return $http.get("/logo").then(getSuccess);
			
			function getSuccess(response){
				return response.data;
			}
		}
	}
})();