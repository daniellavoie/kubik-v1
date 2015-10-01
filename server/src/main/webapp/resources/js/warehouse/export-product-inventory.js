(function() {
	var PRODUCT_INVENTORY_URL = "/productInventory";
	
	angular
		.module("Kubik", [])
		.controller("ExportProductInventoryCtrl", ExportProductInventoryCtrl);

	function ExportProductInventoryCtrl($scope, $http, $timeout) {
		var vm = this;

		vm.closeSeparatorAlert = closeSeparatorAlert;
		vm.executeExport = executeExport;

		vm.export = {
			separator : ";",
			decimalSeparator : ","
		};
		
		function closeSeparatorAlert() {
			vm.invalidSeparator = false;
		}

		function executeExport() {
			vm.invalidSeparator = false;

			if ((vm.export.separator == undefined || vm.export.separator == "")
					&& (vm.export.decimalSeparator == undefined || vm.export.decimalSeparator) == "") {
				vm.invalidSeparator = true;
				
				return;
			}

			location.href = PRODUCT_INVENTORY_URL + "?"
					+ $.param(vm.export);
		}
	}
})();