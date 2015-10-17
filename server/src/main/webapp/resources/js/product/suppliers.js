(function(){
	angular
		.module("Kubik")
		.controller("SuppliersCtrl", SuppliersCtrl);
	
	function SuppliersCtrl($scope, $http, $timeout){
		var vm = this;

		loadSuppliers();
		
		vm.loadSuppliers = loadSuppliers;
		vm.newSupplier = newSupplier;
		vm.openSupplierCard = openSupplierCard;
		
		$scope.$on("supplierSaved", function($event, supplier){
			vm.loadSuppliers();
		});
		
		function loadSuppliers(){
			$http.get("supplier").success(loadSuppliersSuccess);
			
			function loadSuppliersSuccess(suppliers){
				vm.suppliers = suppliers;
			}
		}
		
		function newSupplier(){
			vm.openSupplierCard({});
		}
		
		function openSupplierCard(supplier){
			$scope.$broadcast("openSupplierCard", supplier)
		}
	}
})();