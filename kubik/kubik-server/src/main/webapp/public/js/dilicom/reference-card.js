(function(){
	var $modal = $(".kubikReferenceCard");
	
	angular
		.module("Kubik")
		.controller("ReferenceCardCtrl", ReferenceCardCtrl);
	
	function ReferenceCardCtrl($scope, $http, $timeout){	
		var vm = this;
				
		loadCompany();
		loadSuppliers();
		
		vm.getSupplier = getSupplier;
		vm.loadCompany = loadCompany;
		vm.loadSuppliers = loadSuppliers;
		vm.openCard = openCard;
		vm.showTab = showTab;
		
		$scope.$on("openReferenceCard", function($event, reference){
			vm.openCard(reference);
		});
		
		function getSupplier(id){
			for(var supplierIndex in vm.suppliers){
				var supplier = vm.suppliers[supplierIndex];
				if(supplier.id == id){
					return supplier;
				}
			}
			
			return null;
		}
		
		function loadCompany(){
			$.get("/company").success(function(company){
				vm.company = company;
			});
		}

		function loadSuppliers() {
			$http.get("/supplier").success(function(suppliers){
				vm.suppliers = suppliers;
			});
		}
		
		function openCard(reference){
			$modal.on("shown.bs.modal", function(){
				$timeout(function(){
					vm.reference = reference;					
				})
			}).modal({
				backdrop : "static",
				keyboard : false
			});
		}
		
		function showTab(tabClass){
			$(".kubikReferenceCard .nav li").removeClass("active");
			$(this).addClass("active");
			
			$(".kubikReferenceCard .tab").addClass("hidden");
			
			$(".tab." + tabClass).removeClass("hidden");
		};
	}
})();