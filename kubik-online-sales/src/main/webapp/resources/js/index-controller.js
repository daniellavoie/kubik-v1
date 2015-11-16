(function(){
	angular
		.module("kos")
		.controller("IndexCtrl", IndexCtrl);
	
	function IndexCtrl($scope){
		$scope.$emit("updateTitle", "Accueil");
	}
})();