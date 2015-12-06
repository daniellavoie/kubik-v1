(function(){
	angular
		.module("kos")
		.controller("LibraryCtrl", LibraryCtrl);
	
	function LibraryCtrl($scope, $timeout){
		$scope.$emit("updateTitle", "La Librairie");
		
		$timeout(function(){
			$scope.$broadcast("setActiveTab", "the-library");
		});
	}
})();