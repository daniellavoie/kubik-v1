(function(){
	angular
		.module("kos")
		.controller("CreateAccountCtrl", CreateAccountCtrl);
	
	function CreateAccountCtrl($location, accountService){
		vm = this;
		
		var sourceLocation = $location.search().sourceLocation;
		
		vm.submited = false;
		vm.createAccount = createAccount;
		vm.hideError = hideError;
		
		function createAccount(){
			vm.hideError();
			vm.loading = true;
			var valid = true;
			if(vm.account.password != vm.passwordConfirmation){
				valid = false;
				vm.loading = false;
				vm.error = 'invalidPasswordConfirmation';
			}
			
			if(valid){
				accountService
					.createAccount(vm.account.username, vm.account.password)
					.then(createAccountSuccess)
					.catch(createAccountError)
					.finally(createAccountCompleted);
			}
			
			function createAccountCompleted(){
				vm.loading = false;
			}
			
			function createAccountSuccess(account){
				vm.submited = true;
				vm.success = true;
			}
			
			function createAccountError(message){
				vm.error = message.data.message;
				vm.submited = false;
			}
		}
		
		function hideError(){
			vm.error = null;
		}
	}
})();