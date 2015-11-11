(function(){
	var COMPANY_URL = "/company";
	
	angular
		.module("kos")
		.factory("companyService", CompanyService);
	
	function CompanyService($http){
		return {
			getEan13 : getEan13
		};
		
		function getEan13(){
			return $http.get(COMPANY_URL + "/ean13").then(getEan13Success);
			
			function getEan13Success(ean13){
				return ean13;
			}
		}
	}
})();