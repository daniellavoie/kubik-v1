(function(){
	var DILCIOM_ORDER_URL = "/dilicomOrder";
	angular
	    .module("Kubik")
	    .factory("dilicomOrderService", DilicomOrderService);
	
	function DilicomOrderService($http){
		return {
			resendOrder : resendOrder,
			save : save
		}
		
		function resendOrder(dilicomOrder){
			dilicomOrder.status = 'PENDING';
			
			return save(dilicomOrder);
		}
		
		function save(dilicomOrder){
			return $http.post(DILCIOM_ORDER_URL, dilicomOrder)
			    .then(success, error, complete);
			
			function success(response){
				return response.data;
			}
			
			function error(error){
				return error;
			}
			
			function complete(){
				
			}
		}
	}
})();