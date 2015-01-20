window.KubikProductSearch = function(options){
	if(options.modal == undefined){
		options.modal = false;
	}
	
	if(options.resultPerPage == undefined){
		options.resultPerPage = 50;		
	}
	
	if(options.defaultSortBy == undefined){
		options.defaultSortBy = "extendedLabel";		
	}
	
	if(options.defaultSortDirection == undefined){
		options.defaultSortDirection = "ASC";		
	}
	
	if(options.productCreationAllowed == undefined){
		options.productCreationAllowed = true;
	}
	
	this.app = options.app;
	this.modal = options.modal;
	this.$container = options.$container;
	this.productCreationAllowed = options.productCreationAllowed;
	
	this.productUrl = options.productUrl;
	this.resultPerPage = options.resultPerPage;
	this.defaultSortBy = options.defaultSortBy;
	this.defaultSortDirection = options.defaultSortDirection;
	this.productSelected = options.productSelected;
	
	this.init();
};

window.KubikProductSearch.prototype.init = function(){
	var kubikProductSearch = this;
	
	if(this.app == undefined){
		this.app = angular.module("KubikProductSearch", []); 
	}

	this.app.controller("KubikProductSearchController", function($scope, $http, $timeout){
		$scope.$on("search", function(event){
			$scope.search();
		});
		
		$scope.changePage = function(page){
			$scope.page = page;
			
			$scope.search();
		}
		
		$scope.productSelected = function(product){
			if(kubikProductSearch.productSelected != undefined){
				kubikProductSearch.productSelected(product);
			}
		};
		
		$scope.newProduct = function(){
			$scope.$emit("openProductCard", {});
		};
		
		$scope.openCard = function($event, product){
			$scope.$emit("openProductCard", product);
			
			event.stopPropagation()
		};
		
		$scope.sort = function(sortBy, direction){
			$scope.sortBy = sortBy;
			$scope.direction = direction;
			
			$scope.search();
		}
		
		$scope.search = function(){
			var params = {
				search : "",
				query : $scope.query,
				page : $scope.page,
				resultPerPage : $scope.resultPerPage,
				sortBy : $scope.sortBy,
				direction : $scope.direction 
			}
			
			$http.get(kubikProductSearch.productUrl + "?" + $.param(params)).success(function(searchResult){
				$scope.searchResult = searchResult;

				$timeout(function(){
					for(var productIndex in searchResult.content){
						var product = searchResult.content[productIndex];
						
						$("#product-image-" + product.id).attr(
							"src", 
							"http://images1.centprod.com/" + $scope.company.ean13 + "/" + product.imageEncryptedKey + "-cover-thumb.jpg"
						);
					}					
				})
				
				if(kubikProductSearch.modal){
					$timeout(function(){
						kubikProductSearch.$container.find(".modal-backdrop")
					      .css('height', 0)
					      .css('height', kubikProductSearch.$container[0].scrollHeight);
					});
				}
			});
		};
		
		$scope.query = "";
		$scope.page = 0;
		$scope.resultPerPage = kubikProductSearch.resultPerPage
		$scope.sortBy = kubikProductSearch.defaultSortBy;
		$scope.direction = kubikProductSearch.defaultSortDirection;
		$scope.productCreationAllowed = kubikProductSearch.productCreationAllowed;

		$.get("/company").success(function(company){
			$scope.company = company;
			
			$scope.search();
		});
		
	});
};

window.KubikProductSearch.prototype.closeSearchModal = function(){
	this.modal.modal("hide");
};

window.KubikProductSearch.prototype.openSearchModal = function(){
	this.modal = this.$container.modal({
		backdrop : "static",
		keyboard : false
	}).on("shown.bs.modal", function(){
		$("#search-product-query").focus();
	});
	
};