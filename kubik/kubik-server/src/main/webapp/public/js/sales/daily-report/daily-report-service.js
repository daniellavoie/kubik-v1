(function(){
    var DAILY_REPORT_URL = "/daily-report";
    angular
        .module("Kubik")
        .factory("dailyReportService", DailyReportService);

    function DailyReportService($http){
        return {
            findAll : findAll,
            findOne : findOne,
            reload : reload
        }

        function findAll(params){
            return $http
                .get(DAILY_REPORT_URL + "?" + $.param(params))
                .then(getSuccess);

            function getSuccess(response){
                return response.data;
            }
        }

        function findOne(id){
            return $http
                .get(DAILY_REPORT_URL + "/" + id)
                .then(getSuccess);

            function getSuccess(response){
                return response.data;
            }
        }

        function reload(dailyReportId){
            return $http
                .post(DAILY_REPORT_URL + "/" + dailyReportId)
                .then(postSuccess);

            function postSuccess(response){
                return response.data;
            }
        }
    }
})();