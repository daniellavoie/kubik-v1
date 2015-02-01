var app = angular.module("KubikDailyReportDetails", []);
var dailyReportId = window.location.pathname.split("/")[2];

app.controller("KubikDailyReportDetailsController", function($scope, $http, $timeout){	
	$scope.loadDailyReport = function(){
		$http.get(dailyReportId).success(function(dailyReport){
			$scope.dailyReport = dailyReport;
		});
	};
	
	$scope.loadDailyReport();
});