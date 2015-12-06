(function(){
	var CUSTOMER_ORDER_URL = "/customer-order"
	angular
		.module("kos")
		.factory("customerOrderService", CustomerOrderService);
	
	function CustomerOrderService($http){
		return {
			addProductToCustomerOrder : addProductToCustomerOrder,
			calculateTotalQuantity : calculateTotalQuantity,
			loadOpenCustomerOrder : loadOpenCustomerOrder,
			saveCustomerOrder : saveCustomerOrder
		};
		
		function addProductToCustomerOrder(product, quantity){
			return $http.put(CUSTOMER_ORDER_URL + "/detail", {product : {id : product.id}, quantity : quantity});
		}
		
		function calculateTotalQuantity(customerOrder){
			var totalQuantity = 0;
			
			angular.forEach(customerOrder.customerOrderDetails, onDetail);
			
			function onDetail(detail, index){
				totalQuantity += detail.quantity;
			}
			
			return totalQuantity;
		}
		
		function loadOpenCustomerOrder(){
			return $http.get(CUSTOMER_ORDER_URL)
				.then(loadOpenCustomerOrderSuccess);
			
			function loadOpenCustomerOrderSuccess(response){
				return response.data;
			}
		}
		
		function saveCustomerOrder(customerOrder){
			return $http.post(CUSTOMER_ORDER_URL, customerOrder)
				.then(saveCustomerOrderSuccess);
			
			function saveCustomerOrderSuccess(response){
				return response.data;
			}
		}
	}
})();