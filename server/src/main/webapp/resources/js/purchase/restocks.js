(function(){
	angular
		.module("Kubik")
		.controller("RestocksCtrl", RestocksCtrl);
	
	function RestocksCtrl($scope, $http){
		var vm = this;
		
		vm.page = 0;
		vm.resultPerPage = 50;
		vm.sortBy = "openDate";
		vm.direction = "DESC";
		
		vm.changePage = changePage;
		vm.loadRestocks = loadRestocks;
		vm.openProductCard = openProductCard;
		vm.openRestock = openRestock;
		vm.openSupplierCard = openSupplierCard;
		vm.validateRestock = validateRestock;
		
		loadRestocks();
		
		function changePage(page){
			vm.page = page;
			
			vm.loadRestocks();
		}
		
		function loadRestocks(successCallback){
			var params = {	page : vm.page,
							resultPerPage : vm.resultPerPage,
							sortBy : vm.sortBy,
							direction : vm.direction};
			
			$http.get("restock?" + $.param(params)).success(restocksLoadSuccess);
			
			function restocksLoadSuccess(restocksPage){
				vm.restocksPage = restocksPage;
				
				if(successCallback != undefined){
					successCallback();
				}
			}
		}
		
		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
			
		function openRestock(restock){
			vm.restock = restock;

			var url = "product/" + restock.product.id + "/productStats";
			
			$http.get(url).success(productStatsLoadSuccess);
			
			function productStatsLoadSuccess(productStats){
				vm.productStats = productStats;
				
				$(".restock-modal").modal();
			}
		}
		
		function openSupplierCard($event, supplier){
			$event.stopPropagation();
			
			$scope.$broadcast("openSupplierCard", supplier);
		}
		
		function validateRestock(status, openNextRestock){
			vm.restock.status = status;
			
			vm.restock.product.category = null;
			
			$http.post("restock/", vm.restock).success(saveRestockSucccess);
			
			function saveRestockSucccess(){
				vm.loadRestocks(loadRestocksSuccess);
				
				function loadRestocksSuccess(){
					if(openNextRestock != undefined && openNextRestock && vm.restocksPage.content.length > 0){
						vm.openRestock(vm.restocksPage.content[0]);					
					}else{
						$(".restock-modal").modal("hide");
					}
				}
			}
		}
	}
})();