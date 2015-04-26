var app = angular.module("KubikMenu", []);

app.controller("KubikMenuController", function($scope, $http){
	$http.get("/notification/NEW/count").success(function(notificationCount){
		$scope.notificationCount = notificationCount;
	});
});

angular.bootstrap($("nav.navbar")[0],['KubikMenu']);