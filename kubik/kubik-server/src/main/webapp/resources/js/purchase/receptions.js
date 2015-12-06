(function(){
	angular
		.module("Kubik")
		.controller("ReceptionsCtrl", ReceptionsCtrl);
	
	function ReceptionsCtrl($scope, $http){
		var vm = this;

		vm.page = 0;
		vm.resultPerPage = 50;
		vm.sortBy = "deliveryDate";
		vm.direction = "DESC";
		
		vm.calculateReceptionQuantity = calculateReceptionQuantity;
		vm.changePage = changePage;
		vm.loadReceptions = loadReceptions;
		vm.openReception = openReception;
		vm.openSupplierCard = openSupplierCard;
		
		loadReceptions();
		
		$scope.$on("supplierSaved", function($event, supplier){
			vm.loadReceptions();
		});

		function calculateReceptionQuantity(reception){
			var quantity = 0;
			if(reception != undefined){
				for(var detailIndex in reception.details){
					var detail = reception.details[detailIndex];
					
					quantity += detail.quantityToReceive;
				}
			}
			
			return quantity;
		}
		
		function changePage(page){
			vm.page = page;
			
			vm.loadReceptions();
		}
		
		function loadReceptions(){
			var params = {
					page : vm.page,
					resultPerPage : vm.resultPerPage,
					sortBy : vm.sortBy,
					direction : vm.direction};

			$http.get("reception?" + $.param(params)).success(receptionsLoaded);
			
			function receptionsLoaded(receptionsPage){
				vm.receptionsPage = receptionsPage;
			}
		}

		function openReception(id){
			window.location.href = "reception/" + id;
		}
		
		function openSupplierCard($event, supplier){
			$event.stopPropagation();

			$scope.$broadcast("openSupplierCard", supplier);
		}
	}
})();