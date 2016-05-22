(function(){
	angular
		.module("Kubik")
		.controller("CompanyCtrl", CompanyCtrl);
	
	function CompanyCtrl(companyService, uploadFileService, userService, $scope){
		var vm = this;
		
		vm.save = save;
		vm.updatePassword = updatePassword;
		vm.uploadLogo = uploadLogo;
		
		$scope.$on("setUserExists", setUserExists);

		function loadCompanySuccess(company){
			vm.company = company;
		}
		
		function loadCompany(){
			return companyService.loadCompany();
		}
		
		function save(){
            vm.loading = true;

			if(!vm.userExists)
				userService.createUser(vm.user).then(saveCompany);
			else
				saveCompany();
			
			function saveCompany(){
                companyService
                    .save(vm.company)
                    .then(saveSuccess)
                    .catch(saveError);

                function saveError(){
                    vm.error = true;
                }
                
                function saveSuccess(company){
                    vm.success = true;

                    $scope.$emit("companySaved", company);
                }
			}
		}
		
		function setUserExists($event, userExists){
			vm.userExists = userExists;
			
			if(userExists)
				loadCompany()
					.then(loadCompanySuccess);
		}
		
		function updatePassword(){
			return userService.updatePassword(vm.user.password);
		}
		
		function uploadLogo(){
			vm.loading = true;
			
			uploadFileService.uploadFile(vm.logoFile, "/logo", null, null, uploadCompleted);
			
			function uploadCompleted(){
				vm.loading = false;
				
				$scope.$apply();
			}
		}
	}
})();