(function(){
	var HOME_URL = "/";
	
	angular
		.module("kos")
		.controller("HeaderCtrl", HeaderCtrl);
	
	function HeaderCtrl(){
		var vm = this;
		
		vm.redirectToHome = redirectToHome;
		
		function redirectToHome(){
			location.href = HOME_URL;
		}
	}
})();