(function(){
	var orderId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("ProductsStatsCtrl", ProductsStatsCtrl);
	
	function ProductsStatsCtrl($scope, $http, $timeout){
		var vm = this;
		vm.loading = true;
		
		vm.page = 0;
		vm.resultPerPage = 200;
		vm.direction = "DESC";
		vm.orderBy = "sold";
		vm.withoutInventory = false;
		
		$(".date").each(function(index, element){
			var $element = $(element);
			
			$element.val(moment(parseInt($element.val())).format("DD/MM/YYYY")).datepicker({format : 'dd/mm/yyyy'});
		});
		
		loadProductsStats();
		
		vm.orderAttributes = [	{key : 'inventory', label : 'Inventaire'},
		                      	{key : 'onHold', label : 'Mises de coté'},
		                      	{key : 'ordered', label : 'Commandés'},
		                      	{key : 'received', label : 'Réceptionnés'},
		                      	{key : 'sold', label : 'Vendus'},
		                      	{key : 'returned', label : 'Retournés'},
		                      	{key : 'refunded', label : 'Remboursés'},
		                      	{key : 'counted', label : 'Décomptés'}];
		
		vm.changePage = changePage;
		vm.loadProductsStats = loadProductsStats;
		vm.changeOrderBy = changeOrderBy;
		vm.openProductCard = openProductCard;
		vm.toggleWithoutInventory = toggleWithoutInventory;
		
		function changePage(page){
			vm.page = page;
			
			vm.loadProductsStats();
		}
		
		function loadProductsStats(){
			vm.loading = true;
			var params = {	page : vm.page,
							resultPerPage : vm.resultPerPage,
							orderBy : vm.orderBy,
							direction : vm.direction,
							withoutInventory : vm.withoutInventory};
			
			if(vm.startDate != undefined) params.startDate = moment(vm.startDate, "DD-MM-YYYY").toDate();
			if(vm.endDate != undefined) params.endDate = moment(vm.endDate, "DD-MM-YYYY").toDate();
			
			$http.get("productStats?" + $.param(params))
				.success(loadProductsStatsSuccess)
				.finally(loadProductsStatsCompleted);
			
			function loadProductsStatsSuccess(productsStatsPage){
				vm.productsStatsPage = productsStatsPage;
			}
			
			function loadProductsStatsCompleted(){
				vm.loading = false;
			}
		}
		
		function changeOrderBy(column){
			if(vm.orderBy == column){
				if (vm.direction == "ASC"){
					vm.direction = "DESC";
				} else {
					vm.direction = "ASC";
				}
			}else{
				vm.orderBy = column;
				vm.direction = "DESC";
			}
			
			vm.loadProductsStats();
		}
		
		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
		
		function toggleWithoutInventory(){
			vm.withoutInventory = !vm.withoutInventory;
			
			vm.loadProductsStats();
		}
	}
})();