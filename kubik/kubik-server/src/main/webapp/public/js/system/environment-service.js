(function(){
	var ENV_URL = "/environment	";

	angular
	    .module("Kubik")
	    .factory("envService", EnvService);

	angular
	    .module("KubikProductVehicule")
	    .factory("envService", EnvService);
	
	function EnvService($http){
		return {
			getEnvProperty : getEnvProperty
		};
		
		function getEnvProperty(property){
			return $http
				.post(ENV_URL, property, {headers: {'Content-Type': 'application/json'}})
			    .then(getSuccess);
			
			function getSuccess(response){				
				return response.data;
			}
		}
	}
})();