var app = angular.module("sessions", []);

app.controller("SessionController", function($scope, $http){
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadSessions();
	}
	
	$scope.loadSessions = function(){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};

		$http.get("purchaseSession?" + $.param(params)).success(function(sessionsPage){
			$scope.sessionsPage = sessionsPage;
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
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "openDate";
	$scope.direction = "DESC";
	
	$scope.loadSessions();
});