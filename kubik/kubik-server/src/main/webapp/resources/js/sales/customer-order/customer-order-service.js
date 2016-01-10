(function(){
	var CUSTOMER_ORDER_URL = "/customer-order";
	
	angular
		.module("Kubik")
		.factory("customerOrderService", CustomerOrderService);
	
	function CustomerOrderService($http){
		return {
			loadCustomerOrder : loadCustomerOrder,
			process : process,
			save : save,
			search : search
		};
		
		function loadCustomerOrder(customerOrderId){
			return $http
				.get(CUSTOMER_ORDER_URL + "/" + customerOrderId)
				.then(loadCustomerOrderSuccess);
			
			function loadCustomerOrderSuccess(response){
				return response.data;
			}
		}
		
		function process(customerOrder){
			customerOrder.status = "PROCESSED";
			
			return save(customerOrder);
		}
		
		function save(customerOrder){
			return $http
				.post(CUSTOMER_ORDER_URL, customerOrder)
				.then(saveSuccess);
			
			function saveSuccess(response){
				return response.data;
			}
		}
		
		function search(params){
			params.search = true;
			
			return $http
				.get(CUSTOMER_ORDER_URL + "?" + $.param(params))
				.then(searchSuccess);
			
			function searchSuccess(response){
				return response.data;
			}
		}
	}
})();