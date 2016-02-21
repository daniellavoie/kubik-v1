(function(){
	var $modifyAddressModal = $(".modify-address");
	
	angular
		.module("kos")
		.controller("EditAddressCtrl", EditAddressCtrl);
	
	function EditAddressCtrl(addressService, $scope, $timeout){
		var vm = this;
		
		$scope.$on("editAddress", editAddressEvent);
		
		vm.save = save;
		
		function save(){
			addressService
				.saveAddress(vm.address)
				.then(saveAddressSuccess);
			
			function saveAddressSuccess(address){
				vm.address = address;
				
				$modifyAddressModal.modal("hide");
				
				$scope.$emit("addressSaved", address);				
			}
		}
		
		function editAddressEvent($event, address){
			vm.address = address;
			
			$timeout(timeoutClosure);
			
			function timeoutClosure(){
				$modifyAddressModal.modal();
			}
		}
	}
})();