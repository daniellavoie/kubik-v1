var app = angular.module("KubikInvoiceDetails", []);
var kubikProductCard = new KubikProductCard({
	app : app,
	productUrl : "../product"
});

var invoiceId = window.location.pathname.split("/")[2];

app.controller("KubikInvoiceDetailsController", function($scope, $http, $timeout){
	$scope.changeInvoice = function(invoiceId){
		location.href = invoiceId;
	};
	
	$scope.changePaymentMethod = function(payment, paymentMethodType){
		payment.paymentMethod.type = paymentMethodType;
		
		$http.post("/invoice", $scope.invoice).success(changePaymentMethodSuccess);
		
		function changePaymentMethodSuccess(invoice){
			$scope.invoice = invoice;
		}
	}
	
	$scope.confirmRefund = function(){
		$(".refund-modal").modal();
	};
	
	$scope.loadInvoice = function(){
		$http.get(invoiceId).success(function(invoice){
			$scope.invoice = invoice;
		});
	};
	
	$scope.openReceipt = function(){
		window.open($scope.invoice.id + "/receipt", "Ticket de caisse", "pdf");
	}
	
	$scope.kubikCustomerCard = new KubikCustomerCard({customerUrl : "../customer", customerSaved : function(){
		$scope.loadInvoice();
	}});
	
	$scope.printReceipt = function(){
		$http.post("../invoice/" + $scope.invoice.id + "/receipt?print");
	};
	
	$scope.refund = function(){
		$scope.invoice.status = {type : "REFUND"};
		
		$http.post("../invoice", $scope.invoice).success(function(invoice){
			$scope.invoice = invoice;
		});
	};
	
	$http.get("../invoice/" + invoiceId + "/next").success(function(invoiceId){
		if(invoiceId == "") customerCreditId = null;
		$scope.nextInvoice = invoiceId;
	});
	
	$http.get("../invoice/" + invoiceId + "/previous").success(function(invoiceId){
		if(invoiceId == "") customerCreditId = null;
		$scope.previousInvoice = invoiceId;
	});
	
	$scope.kubikProductCard = kubikProductCard;
	
	$scope.loadInvoice();
});