(function(){
	var CREATE_ACCOUNT_URL = "/creer-un-compte";
	
	angular
		.module("kos")
		.controller("SignInCtrl", SignInCtrl);
	
	function SignInCtrl(accountService){
		var vm = this;
		
		vm.loading = true;
		
		loadAccount();
		
		function loadAccount(){
			accountService.loadAccount().then(loadAccountSuccess);
			
			function loadAccountSuccess(account){
				if(account != ""){
					vm.account = account;
				}
				
				vm.loading = false;
			}
		}
	}
})();