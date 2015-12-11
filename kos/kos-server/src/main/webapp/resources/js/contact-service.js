(function(){
	var CONTACT_US_URL = "/contact-us";
	
	angular
		.module("kos")
		.factory("contactService", ContactService);
	
	function ContactService($http){
		return {
			getContactUsEmail : getContactUsEmail
		};
		
		function getContactUsEmail(product, quantity){
			return $http
				.get(CONTACT_US_URL)
				.then(getContactUsEmailSuccess);
			
			function getContactUsEmailSuccess(response){
				return response.data;
			}
		}
	}
})();