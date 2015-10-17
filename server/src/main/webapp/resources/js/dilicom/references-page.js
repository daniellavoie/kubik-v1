(function(){
	var kubik = angular.module("Kubik");
	
	var referenceCardController = kubik.controller("KubikReferenceCardController");
	
	kubik.controller("KubikReferencesPageController", KubikReferencesPageController);
	
	function KubikReferencesPageController($scope, $http, $timeout){
		$scope.$on("openReferenceCard", openReferenceCard);
		
		function openReferenceCard(event, reference){
			referenceCardController.openCard(reference);
		}
	}
})();