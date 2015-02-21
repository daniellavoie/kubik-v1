window.KubikCustomerSearch = function(options){
	if(options.modal == undefined){
		options.modal = false;
	}
	
	if(options.resultPerPage == undefined){
		options.resultPerPage = 50;		
	}
	
	if(options.defaultSortBy == undefined){
		options.defaultSortBy = "lastName";		
	}
	
	if(options.defaultSortDirection == undefined){
		options.defaultSortDirection = "ASC";		
	}
	
	if(options.customerCreationAllowed == undefined){
		options.customerCreationAllowed = true;
	}
	
	this.app = options.app;
	this.modal = options.modal;
	this.$container = options.$container;
	this.customerCreationAllowed = options.customerCreationAllowed;
	
	this.customerUrl = options.customerUrl;
	this.resultPerPage = options.resultPerPage;
	this.defaultSortBy = options.defaultSortBy;
	this.defaultSortDirection = options.defaultSortDirection;
	this.customerSelected = options.customerSelected;
	
	this.init();
};

window.KubikCustomerSearch.prototype.init = function(){
	var kubikCustomerSearch = this;
	
	if(this.app == undefined){
		this.app = angular.module("KubikCustomerSearch", []); 
	}

	this.app.controller("KubikCustomerSearchController", function($scope, $http, $timeout){
		$scope.$on("search", function(event){
			$scope.search();
		});
		
		$scope.changePage = function(page){
			$scope.page = page;
			
			$scope.search();
		}
		
		$scope.customerSelected = function(customer){
			if(kubikCustomerSearch.customerSelected != undefined){
				kubikCustomerSearch.customerSelected(customer);
			}
		};
		
		$scope.newCustomer = function(){
			$scope.$emit("openCustomerCard", {});
		};
		
		$scope.openCard = function($event, customer){
			try{
				$scope.$emit("openCustomerCard", customer);
			}finally{
				$event.stopPropagation();
			}
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
			
			$http.get(kubikCustomerSearch.customerUrl + "?" + $.param(params)).success(function(searchResult){
				$scope.searchResult = searchResult;

				if(kubikCustomerSearch.modal){
					$timeout(function(){
						kubikCustomerSearch.$container.find(".modal-backdrop")
					      .css('height', 0)
					      .css('height', kubikCustomerSearch.$container[0].scrollHeight);
					});
				}
			});
		};
		
		$scope.query = "";
		$scope.page = 0;
		$scope.resultPerPage = kubikCustomerSearch.resultPerPage
		$scope.sortBy = kubikCustomerSearch.defaultSortBy;
		$scope.direction = kubikCustomerSearch.defaultSortDirection;
		$scope.customerCreationAllowed = kubikCustomerSearch.customerCreationAllowed;
		
		$scope.search();
	});
};

window.KubikCustomerSearch.prototype.closeSearchModal = function(){
	this.modal.modal("hide");
};

window.KubikCustomerSearch.prototype.openSearchModal = function(){
	this.modal = this.$container.modal({
		backdrop : "static",
		keyboard : false
	}).on("shown.bs.modal", function(){
		$("#search-customer-query").focus();
	});
	
};