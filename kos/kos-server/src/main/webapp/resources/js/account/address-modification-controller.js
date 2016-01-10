(function(){
	angular
		.module("kos")
		.controller("AddressModificationCtrl", AddressModificationCtrl);
	
	function AddressModificationCtrl($scope){
		var vm = this;
		
		$scope.$on("setAddressToModify", setAddressToModifyEvent);
		
		vm.saveAddress = saveAddress;
		
		function saveAddress(){
			$scope.$emit("saveAddress", vm.address);
		}
		
		function setAddressToModifyEvent($event, address){
			vm.address = address;
		}
	}
})();