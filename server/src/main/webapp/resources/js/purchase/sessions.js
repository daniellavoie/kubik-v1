var app = angular.module("sessions", []);

app.controller("SessionController", function($scope, $http){
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadSessions();
	}
	
	$scope.loadSessions = function(){
		var params = {	status : $scope.user.preferences.purchaseSession.status, 
						page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};

		$http.get("purchaseSession?" + $.param(params)).success(function(sessionsPage){
			$scope.sessionsPage = sessionsPage;
		});
	};
	
	$scope.loadUserAndSessions = function(){
		$http.get("user").success(function(user){
			$scope.user = user;
			
			$scope.loadSessions();
		})
	}
	
	$scope.newSession = function(){
		$http.post("purchaseSession", {}).success(function(session){
			$scope.openSession(session.id);
		});
	};
	
	$scope.openSession = function(id){
		window.location.href = "purchaseSession/" + id;
	};
	
	$scope.updateStatus = function(status){		
		var statusIndex = $scope.user.preferences.purchaseSession.status.indexOf(status);
		if(statusIndex != -1){
			$scope.user.preferences.purchaseSession.status.splice(statusIndex, 1);
		}else{
			$scope.user.preferences.purchaseSession.status.push(status);
		}
		
		$http.post("user", $scope.user);
		
		$scope.loadSessions();
	};
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "openDate";
	$scope.direction = "DESC";
	
	$scope.loadUserAndSessions();
});