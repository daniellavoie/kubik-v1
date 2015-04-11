window.KubikReferenceCard = function(options){
	if(options == undefined){
		options = {};
	}
	if(options.$modalContainer == undefined){
		options.$modalContainer = $(".reference-card");
	}
	
	this.$modalContainer = options.$modalContainer;
	this.referenceUrl = options.referenceUrl;
	this.referenceSaved = options.referenceSaved;
	
	this.init();
};

window.KubikReferenceCard.prototype.init = function(){
	var kubikReferenceCard = this;
	
	this.app = angular.module("KubikReferenceCard", []);
	
	this.app.controller("KubikReferenceCardController", function($scope, $http, $timeout){
		kubikReferenceCard.$modalScope = $scope;
		
		$scope.getSupplier = function(id){
			for(var supplierIndex in $scope.suppliers){
				var supplier = $scope.suppliers[supplierIndex];
				if(supplier.id == id){
					return supplier;
				}
			}
			
			return null;
		}		
		
		$scope.openCard = function(reference){
			$scope.reference = reference;

			$timeout(function(){
				kubikReferenceCard.$modal = kubikReferenceCard.$modalContainer.find(".modal").modal({
					backdrop : "static",
					keyboard : false
				});
			});
		}
		
		$scope.refreshModalBackdrop = function(){
			$timeout(function(){
				kubikReferenceCard.$modalContainer.find(".modal-backdrop")
			      .css('height', 0)
			      .css('height', kubikReferenceCard.$modal[0].scrollHeight);
			});
		};
		
		$scope.showTab = function(tabClass){
			$(".kubikReferenceCard .nav li").removeClass("active");
			$(this).addClass("active");
			
			$(".kubikReferenceCard .tab").addClass("hidden");
			
			$(".tab." + tabClass).removeClass("hidden")
			
			$timeout(function(){
				kubikReferenceCard.$modal.find(".modal-backdrop")
			      .css('height', 0)
			      .css('height', kubikReferenceCard.$modal[0].scrollHeight);
			});
		};
		
		$.get("/company").success(function(company){
			$scope.company = company;
		});

		$http.get("/supplier").success(function(suppliers){
			$scope.suppliers = suppliers;
		});
	});
	
	$.get(
		"/reference?card", 
		function(referenceCardHtml) {
			kubikReferenceCard.$modalContainer.html(referenceCardHtml);
			
		    angular.bootstrap($(".kubikReferenceCard")[0],['KubikReferenceCard']);
		}, 
		"html"
	);

};

window.KubikReferenceCard.prototype.openCard = function(reference){
	this.$modalScope.openCard(reference);
}