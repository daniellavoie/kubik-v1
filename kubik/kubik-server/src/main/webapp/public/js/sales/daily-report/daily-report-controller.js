(function(){
	var dailyReportId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("DailyReportDetailsCtrl", DailyReportDetailsCtrl);
	
	function DailyReportDetailsCtrl(dailyReportService){
		var vm = this;

		dailyReportService
            .findOne(dailyReportId)
            .then(findOneSuccess);

        function findOneSuccess(dailyReport){
            vm.dailyReport = dailyReport;
        }
	}
})();