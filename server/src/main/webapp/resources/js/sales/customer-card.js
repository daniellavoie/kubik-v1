window.KubikCustomerCard = function(options){
	if(options == undefined){
		options = {};
	}
	if(options.$modalContainer == undefined){
		options.$modalContainer = $(".customer-card");
	}
	
	this.$modalContainer = options.$modalContainer;
	this.customerUrl = options.customerUrl;
	this.customerSaved = options.customerSaved;
	
	this.init();
};

window.KubikCustomerCard.prototype.init = function(){
	var kubikCustomerCard = this;
	
	this.app = angular.module("KubikCustomerCard", []);
	
	this.app.controller("KubikCustomerCardController", function($scope, $http, $timeout){
		kubikCustomerCard.$modalScope = $scope;
		$scope.editMode = false;
		
		$scope.cancelModify = function(){
			$scope.customer = $scope.originalCustomer;
			
			$scope.endEditMode();
		}
		
		$scope.endEditMode = function(){
			$scope.editMode = false;
			
			$scope.$saveBtn.addClass("hidden");
			$scope.$cancelBtn.addClass("hidden");
			$scope.$modifyBtn.removeClass("hidden");
			$scope.$closeBtn.removeClass("hidden");
			
			$scope.refreshModalBackdrop();
		};
		
		$scope.modify = function(){
			$scope.editMode = true;
			
			$scope.$saveBtn.removeClass("hidden");
			$scope.$cancelBtn.removeClass("hidden");
			$scope.$modifyBtn.addClass("hidden");
			$scope.$closeBtn.addClass("hidden");
			
			$scope.originalCustomer = $.extend(true, {}, $scope.customer);
			
			$scope.refreshModalBackdrop();
		}
		
		$scope.openCard = function(customer){
			$scope.customer = customer;
			
			$timeout(function(){
				kubikCustomerCard.$modal = kubikCustomerCard.$modalContainer.find(".modal").modal({
					backdrop : "static",
					keyboard : false
				});
				
				if(customer.id == undefined){
					$scope.modify();
				}
			});
		};
		
		$scope.refreshModalBackdrop = function(){
			$timeout(function(){
				kubikCustomerCard.$modalContainer.find(".modal-backdrop")
			      .css('height', 0)
			      .css('height', kubikCustomerCard.$modal[0].scrollHeight);
			});
		};
		
		$scope.save = function(){
			$http.post(kubikCustomerCard.customerUrl, $scope.customer).success(function(customer){
				$scope.endEditMode();
				if(kubikCustomerCard.customerSaved != undefined){
					kubikCustomerCard.customerSaved(customer);
				}
			})
		};

		$scope.$saveBtn = kubikCustomerCard.$modalContainer.find(".save")
		$scope.$modifyBtn = kubikCustomerCard.$modalContainer.find(".modify")
		$scope.$closeBtn = kubikCustomerCard.$modalContainer.find(".closeModal")
		$scope.$cancelBtn = kubikCustomerCard.$modalContainer.find(".cancel")
	});
	
	$.get(
		"/customer?card", 
		function(customerCardHtml) {
			kubikCustomerCard.$modalContainer.html(customerCardHtml);
			
		    angular.bootstrap($(".customer-card")[0],['KubikCustomerCard']);
		}, 
		"html"
	);

};

window.KubikCustomerCard.prototype.openCard = function(productId){
	this.$modalScope.openCard(productId);
}