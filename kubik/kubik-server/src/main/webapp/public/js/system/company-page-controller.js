(function(){
    angular
        .module("Kubik")
        .controller("CompanyPageCtrl", CompanyPageCtrl);

    function CompanyPageCtrl($scope, $timeout){
        var vm = this;
        
        $timeout(initializeCompanyCtrl);

        function initializeCompanyCtrl(){
            $scope.$broadcast("setUserExists", true);
        }
    }
})();