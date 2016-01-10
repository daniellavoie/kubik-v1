(function(){
	var TRANSACTION_URL = "/payment/transaction";
	
	angular
		.module("Kubik")
		.factory("transactionService", TransactionService);

	function TransactionService($http){
		return {
			loadTransaction : loadTransaction
		};
		
		function loadTransaction(id){
			return $http
				.get(TRANSACTION_URL + "/" + id)
				.then(loadTransactionSuccess);
			
			function loadTransactionSuccess(response){
				return response.data;
			}
		}
	}
})();