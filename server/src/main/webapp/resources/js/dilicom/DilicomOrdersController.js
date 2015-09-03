angular
	.module("KubikDilicomOrders")
	.controller("DilicomOrdersController", DilicomOrdersController);

function DilicomOrdersController($scope, $http){
	var vm = this;
	
	vm.direction = "DESC";
	vm.page = 0;
	vm.resultPerPage = 50;
	vm.sortBy = "creationDate";
	
	vm.changePage = changePage;
	vm.loadDilicomOrders = loadDilicomOrders
	
	loadDilicomOrders();
	
	function changePage(page){
		vm.page = page;
		
		vm.loadDilicomOrders();
	}
	
	function loadDilicomOrders(successCallback){
		var params = {	page : vm.page,
						resultPerPage : vm.resultPerPage,
						sortBy : vm.sortBy,
						direction : vm.direction};
		
		$http.get("dilicomOrder?" + $.param(params)).success(ordersPageLoaded);
		
		function ordersPageLoaded(dilicomOrdersPage){
			vm.dilicomOrdersPage = dilicomOrdersPage;
			
			if(successCallback != undefined){
				successCallback();
			}
		}
	}
};