(function(){
	angular
		.module("Kubik")
		.controller("DailyReportPageCtrl", DailyReportPageCtrl);
	
	function DailyReportPageCtrl($scope, $http){
		var vm = this;
		
		vm.page = 0;
		vm.resultPerPage = 20;
		vm.sortBy = "date";
		vm.direction = "DESC";
		
		loadDailyReports();
		
		vm.changePage = changePage;
		vm.loadDailyReports = loadDailyReports;
		vm.openDailyReport = openDailyReport;
		vm.reload = reload;
		
		function changePage(page){
			vm.page = page;
			
			vm.loadDailyReports();
		}
		
		function loadDailyReports(){
			var params = {	page : vm.page,
							resultPerPage : vm.resultPerPage,
							sortBy : vm.sortBy,
							direction : vm.direction};
			
			$http.get("dailyReport?" + $.param(params)).success(dailyReportLoadSuccess);
			
			function dailyReportLoadSuccess(dailyReportPage){
				vm.dailyReportPage = dailyReportPage;
			}
		}
		
		function openDailyReport(dailyReport){
			location.href = "dailyReport/" + dailyReport.id;
		}
		
		function reload(dailyReport, $event){
			$event.stopPropagation();
			
			var $btn = $("#reload-daily-report-" + dailyReport.id + "-btn");
			var $loading = $("#reload-daily-report-" + dailyReport.id + "-loading");
			
			$btn.addClass("hidden");
			$loading.removeClass("hidden");
			
			$http.post("dailyReport/" + dailyReport.id).success(vm.loadDailyReports).finally(dailyReportReloaded);
			
			function dailyReportReloaded(){
				$btn.removeClass("hidden");
				$loading.addClass("hidden");				
			}
		}
	}
})();