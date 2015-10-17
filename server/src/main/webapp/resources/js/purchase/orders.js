(function(){
	angular
		.module("Kubik")
		.controller("PurchaseOrdersCtrl", PurchaseOrdersCtrl);
	
	function PurchaseOrdersCtrl($scope, $http){
		var vm = this;
		
		vm.page = 0;
		vm.resultPerPage = 50;
		vm.sortBy = "date";
		vm.direction = "DESC";
		
		vm.calculateOrderQuantity = calculateOrderQuantity;
		vm.changePage = changePage;
		vm.loadOrders = loadOrders;
		vm.openOrder = openOrder;
		vm.openSupplierCard = openSupplierCard;
		
		loadOrders();
		
		function calculateOrderQuantity(order){
			var quantity = 0;
			for(var detailIndex in order.details){
				var detail = order.details[detailIndex];
				
				quantity += detail.quantity;
			}
			
			return quantity;
		}
		
		function changePage(page){
			vm.page = page;
			
			vm.loadOrders();
		}
		
		function loadOrders(){
			var params = {	page : vm.page,
							resultPerPage : vm.resultPerPage,
							sortBy : vm.sortBy,
							direction : vm.direction};
			
			$http.get("purchaseOrder?" + $.param(params)).success(ordersLoaded);
			
			function ordersLoaded(ordersPage){
				vm.ordersPage = ordersPage;
			}
		}
		
		function openOrder(id){
			window.location.href = "purchaseOrder/" + id;
		}
		
		function openSupplierCard($event, supplier){
			$event.stopPropagation();
			
			$scope.$broadcast("openSupplierCard", supplier);
		}
	}
})();