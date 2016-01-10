(function(){
	var CUSTOMER_ORDER_URL = "/customer-order";
	var CART_URL = "/cart";
	
	angular
		.module("kos")
		.factory("customerOrderService", CustomerOrderService);
	
	function CustomerOrderService($http){
		return {
			addProductToCustomerOrder : addProductToCustomerOrder,
			calculateTotalQuantity : calculateTotalQuantity,
			loadCart : loadCart,
			loadCustomerOrder : loadCustomerOrder,
			loadCustomerOrdersHistory : loadCustomerOrdersHistory,
			saveCustomerOrder : saveCustomerOrder
		};
		
		function addProductToCustomerOrder(product, quantity){
			return $http.put(CART_URL + "/detail", {product : {id : product.id}, quantityOrdered : quantity});
		}
		
		function calculateTotalQuantity(customerOrder){
			var totalQuantity = 0;
			
			angular.forEach(customerOrder.customerOrderDetails, onDetail);
			
			function onDetail(detail, index){
				totalQuantity += detail.quantityOrdered;
			}
			
			return totalQuantity;
		}
		
		function loadCustomerOrder(customerOrderId){
			return $http
				.get(CUSTOMER_ORDER_URL + "/" + customerOrderId)
				.then(loadCustomerOrderSuccess);
			
			function loadCustomerOrderSuccess(response){
				return response.data;
			}
		}
		
		function loadCustomerOrdersHistory(params){
			return $http
				.get(CUSTOMER_ORDER_URL + "?" + $.param(params))
				.then(loadCustomerOrdersHistorySuccess);
			
			function loadCustomerOrdersHistorySuccess(response){
				return response.data;
			}
		}
		
		function loadCart(){
			return $http.get(CART_URL)
				.then(loadCartSuccess);
			
			function loadCartSuccess(response){
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