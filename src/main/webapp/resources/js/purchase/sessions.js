var app = angular.module("sessions", []);

app.controller("SessionController", function($scope, $http){
	$scope.loadSessions = function(){
		$http.get("purchaseSession").success(function(sessions){
			$scope.sessions = sessions;
		});
	};
	
	$scope.newSession = function(){
		$http.post("purchaseSession", {}).success(function(session){
			$scope.openSession(session.id);
		});
	};
	
	$scope.openSession = function(id){
		window.location.href = "purchaseSession/" + id;
	};
	
	$scope.loadSessions();
});