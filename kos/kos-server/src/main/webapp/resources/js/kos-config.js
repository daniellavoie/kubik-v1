(function(){
	angular
		.module("kos")
		.config(Config);
	
	function Config($locationProvider){
		$locationProvider.html5Mode({
			enabled : true,
			requireBase: false
		});
	}
})();