var app = angular.module("KubikReceptions", []);

app.controller("KubikReceptionsController", function($scope, $http){
	$scope.loadReceptions = function(){
		$http.get("reception").success(function(receptions){
			$scope.receptions = receptions;
		});
	};
	
	$scope.openReception = function(id){
		window.location.href = "reception/" + id;
	};
	
	$scope.loadReceptions();
});