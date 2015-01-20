var app = angular.module("KubikInvoiceDetails", []);
var invoiceId = window.location.pathname.split("/")[2];

app.controller("KubikInvoiceDetailsController", function($scope, $http, $timeout){	
	$scope.loadInvoice = function(){
		$http.get(invoiceId).success(function(invoice){
			$scope.invoice = invoice;
		});
	};
	
	$scope.openReceipt = function(){
		window.open($scope.invoice.id + "/receipt", "Ticket de caisse", "height=600,width=479");
	}
	
	$scope.kubikCustomerCard = new KubikCustomerCard({customerUrl : "customer", customerSaved : function(){
		$scope.loadInvoice();
	}});

	$scope.kubikProductCard = new KubikProductCard();
	
	$scope.loadInvoice();
});