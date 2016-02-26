(function(){
	var INVOICE_URL = "/invoice";
	
	angular
		.module("Kubik")
		.factory("invoiceService", InvoiceService);
	
	function InvoiceService($http){
		return {
			cancelInvoice : cancelInvoice,
			findByNumber : findByNumber,
			findNext : findNext,
			findOne : findOne,
			findPrevious : findPrevious,
			generateNewOrder : generateNewOrder,
			printReceipt : printReceipt,
			removeInvoiceDetail : removeInvoiceDetail,
			save : save,
			search : search
		};
		
		function cancelInvoice(invoice){
			invoice.status.type = 'CANCELED';
			
			return save(invoice);
		}
		
		function findByNumber(number){
			return $http
				.get("/invoice?number=" + number)
				.then(findByNumberSuccess);
			
			function findByNumberSuccess(response){
				return response.data;
			}
		}
		
		function findNext(id){
			return $http.get(INVOICE_URL + "/" + id + "/next").then(findNextSuccess);
			
			function findNextSuccess(response){
				return response.data;
			}
		}
		
		function findPrevious(id){
			return $http.get(INVOICE_URL + "/" + id + "/previous").then(findPreviousSuccess);
			
			function findPreviousSuccess(response){
				return response.data;
			}
		}
		
		function findOne(id){
			return $http
				.get(INVOICE_URL + "/" + id)
				.then(findOneSuccess);
			
			function findOneSuccess(response){
				return response.data;
			};
		}
		
		function generateNewOrder(customerId){
			return $http
				.get(INVOICE_URL + "?new&customerId=" + customerId)
				.then(generateNewOrderSuccess);
			
			function generateNewOrderSuccess(response){
				return response.data;
			}
		}
		
		function printReceipt(invoiceId){
			return $http.post("../invoice/" + invoiceId + "/receipt?print");
		}
		
		function removeInvoiceDetail(invoice, invoiceDetail){
			if(invoice != undefined && invoiceDetail != undefined){
				for(var detailIndex in invoice.details){
					if(invoice.details[detailIndex].id == invoiceDetail.id){
						invoice.details.splice(detailIndex, 1);
						
						break;
					}
				}
			}
		}
		
		function save(invoice){
			if(invoice.status.type == "ORDER_CONFIRMED")
				invoice.confirmedDate = null;
			
			return $http
				.post(INVOICE_URL, invoice)
				.then(saveSuccess);
			
			function saveSuccess(response){
				return response.data;
			}
		}
		
		function search(params){
			return $http
				.get("invoice?" + $.param(params).replace(/\%5B\%5D/g, ''))
				.then(searchSuccess);
			
			function searchSuccess(response){
				return response.data;
			}
		}
	}
})();