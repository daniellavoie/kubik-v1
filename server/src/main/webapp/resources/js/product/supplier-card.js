window.KubikSupplierCard = function(options){
	if(options == undefined){
		options = {};
	}
	if(options.$modalContainer == undefined){
		options.$modalContainer = $(".supplier-card");
	}
	
	this.$modalContainer = options.$modalContainer;
	this.supplierUrl = options.supplierUrl;
	this.supplierSaved = options.supplierSaved;
	
	this.init();
};

window.KubikSupplierCard.prototype.init = function(){
	var kubikSupplierCard = this;
	
	this.app = angular.module("KubikSupplierCard", []);
	
	this.app.controller("KubikSupplierCardController", function($scope, $http, $timeout){
		kubikSupplierCard.$modalScope = $scope;
		$scope.editMode = false;
		
		$scope.cancelModify = function(){
			$scope.supplier = $scope.originalSupplier;
			
			$scope.endEditMode();
		}
		
		$scope.closeCard = function(){
			kubikSupplierCard.$modalContainer.find(".modal").modal("hide");
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
			
			$scope.originalSupplier = $.extend(true, {}, $scope.supplier);
			
			$scope.refreshModalBackdrop();
		}
		
		$scope.openCard = function(supplier){
			$scope.supplier = supplier;
			
			$timeout(function(){
				kubikSupplierCard.$modal = kubikSupplierCard.$modalContainer.find(".modal").modal({
					backdrop : "static",
					keyboard : false
				});
				
				if(supplier.id == undefined){
					$scope.modify();
				}
			});
		};
		
		$scope.refreshModalBackdrop = function(){
			$timeout(function(){
				kubikSupplierCard.$modalContainer.find(".modal-backdrop")
			      .css('height', 0)
			      .css('height', kubikSupplierCard.$modal[0].scrollHeight);
			});
		};
		
		$scope.save = function(){
			var supplier = $scope.supplier;
			
			if(supplier.ean13.trim() != "" && supplier.name.trim() != ""){
				$http.post(kubikSupplierCard.supplierUrl, supplier).success(function(supplier){
					$scope.endEditMode();
					if(kubikSupplierCard.supplierSaved != undefined){
						kubikSupplierCard.supplierSaved(supplier);
					}
				});
			}
		};

		$scope.$saveBtn = kubikSupplierCard.$modalContainer.find(".save")
		$scope.$modifyBtn = kubikSupplierCard.$modalContainer.find(".modify")
		$scope.$closeBtn = kubikSupplierCard.$modalContainer.find(".closeModal")
		$scope.$cancelBtn = kubikSupplierCard.$modalContainer.find(".cancel")
	});
	
	$.get(
		this.supplierUrl + "?card", 
		function(supplierCardHtml) {
			kubikSupplierCard.$modalContainer.html(supplierCardHtml);
			
		    angular.bootstrap($(".supplier-card")[0],['KubikSupplierCard']);
		}, 
		"html"
	);

};

window.KubikSupplierCard.prototype.closeCard = function(){
	this.$modalScope.closeCard();
}

window.KubikSupplierCard.prototype.openCard = function(supplier){
	this.$modalScope.openCard(supplier);
};