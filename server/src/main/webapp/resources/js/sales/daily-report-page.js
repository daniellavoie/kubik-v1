var app = angular.module("KubikDailyReportPage", []);

app.controller("KubikDailyReportPageController", function($scope, $http, $timeout){
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadDailyReports();
	}
	
	$scope.loadDailyReports = function(){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};
		
		$http.get("dailyReport?" + $.param(params)).success(function(dailyReportPage){
			$scope.dailyReportPage = dailyReportPage;
		});
	};
	
	$scope.openDailyReport = function(dailyReport){
		location.href = "dailyReport/" + dailyReport.id;
	}
	
	$scope.page = 0;
	$scope.resultPerPage = 20;
	$scope.sortBy = "date";
	$scope.direction = "DESC";
	
	$scope.loadDailyReports();
});