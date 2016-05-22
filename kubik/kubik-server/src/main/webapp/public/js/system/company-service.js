(function(){
	var COMPANY_URL = "/company";
	
	angular
		.module("Kubik")
		.factory("companyService", CompanyService);
	
	function CompanyService($http){
		return {
			loadCompany : loadCompany,
			save : save
		}

		function loadCompany(){
			return $http
				.get(COMPANY_URL)
				.then(getSuccess);

			function getSuccess(response){
				return response.data;
			}
		}

		function save(company){
			return $http
				.post(COMPANY_URL + (company.id == undefined ? "/new" : ""), company)
				.then(postSuccess);
			
			function postSuccess(response){
				return response.data;
			}
		}
	}
})();