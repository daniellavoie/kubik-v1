window.KubikProductCard = function(options){
	if(options == undefined){
		options = {};
	}
	if(options.$modalContainer == undefined){
		options.$modalContainer = $(".product-card");
	}
	
	this.$modalContainer = options.$modalContainer;
	this.productUrl = options.productUrl;
	this.productSaved = options.productSaved;
	
	this.init();
};

window.KubikProductCard.prototype.init = function(){
	var kubikProductCard = this;
	
	this.app = angular.module("KubikProductCard", []);
	
	this.app.controller("KubikProductCardController", function($scope, $http, $timeout){
		kubikProductCard.$modalScope = $scope;
		$scope.editMode = false;
		
		$scope.cancelModify = function(){
			$scope.product = $scope.originalProduct;
			
			$scope.endEditMode();
		};
		
		$scope.endEditMode = function(){
			$scope.editMode = false;
			
			$scope.$saveBtn.addClass("hidden");
			$scope.$cancelBtn.addClass("hidden");
			$scope.$modifyBtn.removeClass("hidden");
			$scope.$closeBtn.removeClass("hidden");
			
			$scope.refreshModalBackdrop();
		};
		
		$scope.getSupplier = function(id){
			for(var supplierIndex in $scope.suppliers){
				var supplier = $scope.suppliers[supplierIndex];
				if(supplier.id == id){
					return supplier;
				}
			}
			
			return null;
		}
		
		$scope.modify = function(){
			$scope.editMode = true;
			
			$scope.$saveBtn.removeClass("hidden");
			$scope.$cancelBtn.removeClass("hidden");
			$scope.$modifyBtn.addClass("hidden");
			$scope.$closeBtn.addClass("hidden");
			
			$scope.originalProduct = $.extend(true, {}, $scope.product);
			
			$timeout(function(){
				if($scope.editMode){
					$(".date-published, .publish-end-date").each(function(index, element){
						var $element = $(element);
						
						if($element.val() != ""){
							$element.val(moment(parseInt($element.val())).format("DD/MM/YYYY"))							
						}
						
						$element.datepicker({format : 'dd/mm/yyyy'});
					});
				}
				
				var supplier = null;
				if($scope.product.supplier != null) {
					supplier = $scope.getSupplier($scope.product.supplier.id);
				}
				if(supplier == null){
					supplier = $scope.suppliers[0];
				}
				
				$("#product-supplier-" + supplier.id).attr("SELECTED", "SELECTED");
			});
			
			$scope.refreshModalBackdrop();
		}
		
		$scope.openCard = function(product){
			$scope.product = product;

			$timeout(function(){
				kubikProductCard.$modal = kubikProductCard.$modalContainer.find(".modal").modal({
					backdrop : "static",
					keyboard : false
				});
				
				if(product.id == undefined){
					$scope.modify();
				}
			});
		}
		
		$scope.refreshModalBackdrop = function(){
			$timeout(function(){
				kubikProductCard.$modalContainer.find(".modal-backdrop")
			      .css('height', 0)
			      .css('height', kubikProductCard.$modal[0].scrollHeight);
			});
		};
		
		$scope.save = function(){
			$scope.product.datePublished = $(".date-published").datepicker("getDate");
			$scope.product.publishEndDate = $(".publish-end-date").datepicker("getDate");
			
			if(!$scope.product.dilicomReference){
				$scope.product.supplier = $scope.getSupplier($("select.product-supplier").val());
			}
			
			$http.post(kubikProductCard.productUrl, $scope.product).success(function(product){
				$scope.product = product;
				$scope.endEditMode();
				if(kubikProductCard.productSaved != undefined){
					kubikProductCard.productSaved(product);
				}
			})
		};
		
		$scope.showTab = function(tabClass){
			$(".kubikProductCard .nav li").removeClass("active");
			$(this).addClass("active");
			
			$(".kubikProductCard .tab").addClass("hidden");
			
			$(".tab." + tabClass).removeClass("hidden")
			
			$timeout(function(){
				kubikProductCard.$modal.find(".modal-backdrop")
			      .css('height', 0)
			      .css('height', kubikProductCard.$modal[0].scrollHeight);
			});
		};
		
		$.get("/company").success(function(company){
			$scope.company = company;
		});

		$scope.$saveBtn = kubikProductCard.$modalContainer.find(".save")
		$scope.$modifyBtn = kubikProductCard.$modalContainer.find(".modify")
		$scope.$closeBtn = kubikProductCard.$modalContainer.find(".closeModal")
		$scope.$cancelBtn = kubikProductCard.$modalContainer.find(".cancel")

		$http.get("/supplier").success(function(suppliers){
			$scope.suppliers = suppliers;
		});
	});
	$.get(
		"/product?card", 
		function(productCardHtml) {
			kubikProductCard.$modalContainer.html(productCardHtml);
			
		    angular.bootstrap($(".kubikProductCard")[0],['KubikProductCard']);
		}, 
		"html"
	);

};

window.KubikProductCard.prototype.openCard = function(product){
	this.$modalScope.openCard(product);
}