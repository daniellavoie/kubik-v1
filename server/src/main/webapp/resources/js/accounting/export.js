var app = angular.module("KubikAccountingExports", []);

app.controller("KubikAccountingExportsController", function($scope, $http, $timeout){
	$scope.changeExportType = function(exportType){
		$scope.export.type = exportType;
	};
	
	$scope.closeInvalidDatesAlert = function(){
		$scope.invalidDates = false;
	};
	
	$scope.closeInvalidTypeAlert = function(){
		$scope.invalidType = false;
	};
	
	$scope.closeSeparatorAlert = function(){
		$scope.invalidSeparator = false;
	};
	
	$scope.executeExport = function(){
		$scope.invalidDates = false;
		$scope.invalidType = false;
		$scope.invalidSeparator = false;
		
		if($scope.export.type != "accounts" && ($scope.export.startDate == undefined || $scope.export.endDate == undefined)){
			$scope.invalidDates = true;
			return;
		}
		
		if($scope.export.type == undefined){
			$scope.invalidType = true;
			return;
		}
		
		if($scope.export.separator == undefined || $scope.export.separator == ""){
			$scope.invalidSeparator = true;
			return;
		}
		
		location.href = "entry/" + $scope.export.type + "?" + $.param($scope.export);
	};
	
	$scope.export = {separator : ";", decimalSeparator : ","};

	$(".date").datepicker({format : 'dd/mm/yyyy'});
});