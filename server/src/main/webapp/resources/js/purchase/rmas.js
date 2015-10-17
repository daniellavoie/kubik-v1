(function(){
	angular
		.module("Kubik")
		.controller("RmasCtrl", RmasCtrl);
	
	function RmasCtrl($scope, $http){
		var vm = this;
		
		vm.page = 0;
		vm.resultPerPage = 50;
		vm.sortBy = "openDate";
		vm.direction = "DESC";
		
		vm.rma = {supplier : {}, status : "OPEN"};
		vm.error = {};
		
		vm.calculateRmaQuantity = calculateRmaQuantity;
		vm.changePage = changePage;
		vm.createRma = createRma;
		vm.loadRmas = loadRmas;
		vm.newRma = newRma;
		vm.openOrder = openOrder;
		vm.supplierEan13KeyUp = supplierEan13KeyUp;
		vm.validateSupplierEan13 = validateSupplierEan13;
		vm.openSupplierCard = openSupplierCard;

		loadRmas();
		
		$scope.$on("supplierSaved", function($event, supplier){
			vm.loadRmas();
		});
		
		function calculateRmaQuantity(rma){
			var quantity = 0;
			for(var detailIndex in rma.details){
				var detail = rma.details[detailIndex];
				
				quantity += detail.quantity;
			}
			
			return quantity;
		}
		
		function changePage(page){
			vm.page = page;
			
			vm.loadRmas();
		}

		function createRma(){
			$http.post("rma", vm.rma).success(rmaCreated);
			
			function rmaCreated(rma){
				location.href = "rma/" + rma.id;
			}
		}
		
		function loadRmas(){
			var params = {	page : vm.page,
							resultPerPage : vm.resultPerPage,
							sortBy : vm.sortBy,
							direction : vm.direction};
			
			$http.get("rma?" + $.param(params)).success(rmasLoaded);
			
			function rmasLoaded(rmasPage){
				vm.rmasPage = rmasPage;
			}
		}
		
		function newRma(){
			$(".new-rma-modal").on("shown.bs.modal", focus).modal({
				backdrop : "static",
				keyboard : "false"
			});
			
			function focus(){
				$(".supplier-ean13").focus();
			}
		}

		function openOrder(id){
			window.location.href = "rma/" + id;
		}
		
		function openSupplierCard($event, supplier){
			$event.stopPropagation();
			
			$scope.$broadcast("openSupplierCard", supplier);
		}
		
		function supplierEan13KeyUp($event){
			if($event.keyCode == 13){
				vm.validateSupplierEan13();
			}
		}
		
		function validateSupplierEan13(){
			vm.error.supplierNotFound = false;
			
			$http.get("supplier?ean13=" + vm.rma.supplier.ean13).success(supplierLoaded);
			
			function supplierLoaded(supplier){
				if(supplier != ""){
					vm.rma.supplier = supplier;
					
					vm.createRma();
				}else{
					vm.error.supplierNotFound = true;
				}
			}
		}
	}
})();