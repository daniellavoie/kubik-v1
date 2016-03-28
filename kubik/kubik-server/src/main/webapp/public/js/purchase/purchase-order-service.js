(function(){
	var PURCHASE_ORDER_URL = "/purchaseOrder"
	angular
		.module("Kubik")
		.factory("purchaseOrderService", PurchaseOrderService);
	
	function PurchaseOrderService($http){
		return {
			findOne : findOne
		};
		
		function findOne(purchaseOrderId){
			return $http
				.get(PURCHASE_ORDER_URL + "/" + purchaseOrderId)
				.then(findOneSuccess);
			
			function findOneSuccess(response){
				return response.data;
			}			
		}
	}
})();