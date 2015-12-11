(function(){
	angular
		.module("kos")
		.controller("ContactCtrl", ContactCtrl);
	
	function ContactCtrl(contactService){
		var vm = this;
		
		vm.contactUs = contactUs;
		
		function contactUs(){
			contactService
				.getContactUsEmail()
				.then(getContactUsEmailSuccess);
			
			function getContactUsEmailSuccess(email){
				window.location.href = "mailto:" + email;
			}
		}
	}
})();