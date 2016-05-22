(function(){
	angular
		.module("Kubik")
		.controller("DailyReportPageCtrl", DailyReportPageCtrl);
	
	function DailyReportPageCtrl(dailyReportService){
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

			dailyReportService
				.findAll(params)
				.then(dailyReportLoadSuccess);

			function dailyReportLoadSuccess(dailyReportPage){
				vm.dailyReportPage = dailyReportPage;
			}
		}
		
		function openDailyReport(dailyReport){
			location.href = "/daily-report/" + dailyReport.id;
		}
		
		function reload(dailyReport, $event){
			$event.stopPropagation();
			
			var $btn = $("#reload-daily-report-" + dailyReport.id + "-btn");
			var $loading = $("#reload-daily-report-" + dailyReport.id + "-loading");
			
			$btn.addClass("hidden");
			$loading.removeClass("hidden");

			dailyReportService
				.reload(dailyReport.id)
				.then(vm.loadDailyReports)
				.finaly(dailyReportReloaded);

			function dailyReportReloaded(){
				$btn.removeClass("hidden");
				$loading.addClass("hidden");				
			}
		}
	}
})();