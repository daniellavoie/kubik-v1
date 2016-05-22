(function(){
	angular
		.module("Kubik")
		.controller("InitializationCtrl", InitializationCtrl);
	
	function InitializationCtrl(initializationService, $scope, $timeout){
		vm = this;
		
		$timeout(initializeCompanyCtrl);
		
		$scope.$on("companySaved", companySaved);
		
		function companySaved($event, company){
			initialize();
		}
		
		function initialize(){
			vm.loading = true;
			
			initializationService
				.initialize()
				.success(initializeSuccess)
				.error(initializationError)
				.finally(initializeCompleted);
			
			function initializeSuccess(){
				location.href = "/";
			}
			
			function initializationError(){
				vm.error = true;
			}
			
			function initializeCompleted(){
				vm.loading = false;
			}
		}
		
		function initializeCompanyCtrl(){
			$scope.$broadcast("setUserExists", false);
		}
	}
})();