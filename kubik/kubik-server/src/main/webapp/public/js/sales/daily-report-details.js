(function(){
	var dailyReportId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("DailyReportDetailsCtrl", DailyReportDetailsCtrl);
	
	function DailyReportDetailsCtrl($scope, $http){
		var vm = this;
		
		vm.loadDailyReport = loadDailyReport;
		
		loadDailyReport();
		
		function loadDailyReport(){
			$http.get(dailyReportId).success(loadDailyReportSuccess);
			
			function loadDailyReportSuccess(dailyReport){
				vm.dailyReport = dailyReport;
			}
		}
	}
})();