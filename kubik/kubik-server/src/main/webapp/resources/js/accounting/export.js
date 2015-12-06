(function(){
	angular
		.module("Kubik")
		.controller("AccountingExportsCtrl", AccountingExportsCtrl);
	
	function AccountingExportsCtrl($scope, $http, $timeout){
		var vm = this;

		vm.export = {separator : ";", decimalSeparator : ","};

		$(".date").datepicker({format : 'dd/mm/yyyy'});
		
		vm.changeExportType = changeExportType;
		vm.closeInvalidDatesAlert = closeInvalidDatesAlert;
		vm.closeInvalidTypeAlert = closeInvalidTypeAlert;
		vm.closeSeparatorAlert = closeSeparatorAlert;
		vm.executeExport = executeExport;
		
		function changeExportType(exportType){
			vm.export.type = exportType;
		}
		
		function closeInvalidDatesAlert(){
			vm.invalidDates = false;
		}
		
		function closeInvalidTypeAlert(){
			vm.invalidType = false;
		}
		
		function closeSeparatorAlert(){
			vm.invalidSeparator = false;
		}
		
		function executeExport(){
			vm.invalidDates = false;
			vm.invalidType = false;
			vm.invalidSeparator = false;
			
			if(vm.export.type != "accounts" && (vm.export.startDate == undefined || vm.export.endDate == undefined)){
				vm.invalidDates = true;
				return;
			}
			
			if(vm.export.type == undefined){
				vm.invalidType = true;
				return;
			}
			
			if(vm.export.separator == undefined || vm.export.separator == ""){
				vm.invalidSeparator = true;
				return;
			}
			
			location.href = "entry/" + vm.export.type + "?" + $.param(vm.export);
		}
	}
})();