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
		
		$scope.categoryChanged = function(){
			$scope.subCategories = $scope.category.subCategories;
			
			if($scope.product.subCategory == undefined){
				$scope.product.subCategory = $scope.subCategories[0];
			}
		};
		
		$scope.changeInventoryTab = function(tabClassName){
			if($scope.inventoryTab != tabClassName){
				$scope.inventoryTab = tabClassName;
			}
		};
		
		$scope.changePage = function(tabName, page){
			$scope.searchParams.page = page;
			
			$scope.loadInventoryTab(tabName);
		}
		
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
		};
		
		$scope.loadCategory = function(categoryId){
			$http.get("/category/" + categoryId).success(function(category){
				$scope.category = category;
				
				$scope.subCategories = category.subCategories;
			});
		}
		
		$scope.loadInventoryTabs = function(){
			for(var inventoryTabIndex in $scope.inventoryTabs){
				var searchParams = $scope.inventoryTabs[inventoryTabIndex].searchParams;
				
				$scope.loadInventoryTab(inventoryTabIndex, searchParams.page, searchParams.resultPerPage, searchParams.sortBy, searchParams.direction);
			}
		}
		
		$scope.loadSubCategory = function(id, success){
			$http.get("/subCategory/" + id).success(function(subCategory){
				$scope.subCategory = subCategory;
				
				if(success != undefined){
					success();
				}
			});
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
				
				$scope.$apply();
			});
			
			$scope.refreshModalBackdrop();
		}
		
		$scope.openCard = function(product){
			$scope.product = product;
			$scope.productTab = "product";
			$scope.inventoryTab = "reception";
			
			if($scope.product.subCategory != undefined){
				$scope.loadSubCategory($scope.product.subCategory.id != undefined ? $scope.product.subCategory.id : $scope.product.subCategory, function(){
					$scope.loadCategory($scope.subCategory.category.id);
				});
			}

			$timeout(function(){
				kubikProductCard.$modal = kubikProductCard.$modalContainer.find(".modal").modal({
					backdrop : "static",
					keyboard : false
				});
				
				if(product.id == undefined){
					$scope.modify();
				}else{
					$scope.loadInventoryTabs();
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
			
			if($scope.subCategory != null){
				$scope.product.subCategory = {id : $scope.subCategory.id};
			}
			
			$http.post(kubikProductCard.productUrl, $scope.product).success(function(product){
				$scope.product = product;
				$scope.endEditMode();
				if(kubikProductCard.productSaved != undefined){
					kubikProductCard.productSaved(product);
				}
			})
		};
		
		$scope.subCategoryChanged = function(){
			$scope.loadSubCategory($scope.subCategory.id);
		};
		
		$.get("/company").success(function(company){
			$scope.company = company;
		});

		$scope.$saveBtn = kubikProductCard.$modalContainer.find(".save")
		$scope.$modifyBtn = kubikProductCard.$modalContainer.find(".modify")
		$scope.$closeBtn = kubikProductCard.$modalContainer.find(".closeModal")
		$scope.$cancelBtn = kubikProductCard.$modalContainer.find(".cancel")
		
		$scope.inventoryTabs = {	customerCredit : {	searchParams : {
											page : 0,
											resultPerPage : 20,
											sortBy : "customerCredit.completeDate",
											direction : "DESC" }
									}, 
									invoice : {	searchParams : {
										page : 0,
										resultPerPage : 20,
										sortBy : "invoice.paidDate",
										direction : "DESC"  }
									}, 
									reception : {	searchParams : {
										page : 0,
										resultPerPage : 20,
										sortBy : "reception.dateReceived",
										direction : "DESC"  }
									}, 
									rma : {	searchParams : {
										page : 0,
										resultPerPage : 20,
										sortBy : "rma.shippedDate",
										direction : "DESC"  }
									}};
		
		$scope.loadInventoryTab = function(tabName, page, resultPerPage, orderBy, direction){
			$scope.inventoryTabs[tabName].searchParams = {	page : page,
															resultPerPage : resultPerPage,
															sortBy : orderBy,
															direction : direction};
			
			$http.get(kubikProductCard.productUrl + "/" + $scope.product.id + "/" + tabName + "?" + $.param($scope.inventoryTabs[tabName].searchParams)).success(function(searchResult){
				$scope.inventoryTabs[tabName].result = searchResult;
			});
		}
		

		$http.get("/category").success(function(categories){
			$scope.categories = categories;
		});
		
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