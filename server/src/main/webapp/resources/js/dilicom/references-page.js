var app = angular.module("KubikReferencesPage", []);
var kubikReferenceSearch = new KubikReferenceSearch({
	app : app,
	referenceUrl : "reference"
});

app.controller("KubikReferencesPageController", function($scope, $http, $timeout){
	$scope.$on("openReferenceCard", function(event, reference){
		$scope.kubikReferenceCard.openCard(reference);
	})
	
	$scope.kubikReferenceSearch = kubikReferenceSearch;
	
	$scope.kubikReferenceCard = new KubikReferenceCard({referenceUrl : "reference", referenceSaved : function(){
		$scope.$broadcast("search");
	}});
});