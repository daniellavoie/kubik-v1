(function(){
	angular
		.module("kos")
		.controller("IndexCtrl", IndexCtrl);
	
	function IndexCtrl($scope){
		$scope.$emit("updateTitle", "La Librairie");
		$scope.$broacast("setActiveTab", "librairy");
	}
})();