(function(){
	angular
		.module("kos")
		.controller("KosCtrl", KosCtrl);
	
	function KosCtrl($scope){
		var vm = this;
		
		$scope.$on("updateTitle", updateTitleEvent);
		
		function updateTitleEvent($event, title){
			vm.title = title;
		}
	}
})();