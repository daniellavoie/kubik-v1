(function(){
	angular
		.module("Kubik")
		.config(Config);
	
	function Config($locationProvider){
		$locationProvider.html5Mode({
			enabled : true,
			requireBase: false
		});
	}
})();