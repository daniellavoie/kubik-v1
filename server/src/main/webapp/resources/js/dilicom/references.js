window.KubikReferenceSearch = function(options){
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
	
	if(options.referenceCreationAllowed == undefined){
		options.referenceCreationAllowed = true;
	}
	
	this.app = options.app;
	this.modal = options.modal;
	this.$container = options.$container;
	this.referenceCreationAllowed = options.referenceCreationAllowed;
	
	this.referenceUrl = options.referenceUrl;
	this.resultPerPage = options.resultPerPage;
	this.defaultSortBy = options.defaultSortBy;
	this.defaultSortDirection = options.defaultSortDirection;
	this.referenceSelected = options.referenceSelected;
	
	this.init();
};

window.KubikReferenceSearch.prototype.init = function(){
	var kubikReferenceSearch = this;
	
	if(this.app == undefined){
		this.app = angular.module("KubikReferenceSearch", []); 
	}

	this.app.controller("KubikReferenceSearchController", function($scope, $http, $timeout){
		$scope.$on("search", function(event){
			$scope.search();
		});
		
		$scope.changePage = function(page){
			$scope.page = page;
			
			$scope.search();
		}
		
		$scope.createProduct = function($event, reference){
			try{
				reference.loading = true;
				$http.post("/reference/" + reference.id + "?createProduct").success(function(product){
					reference.importedInKubik = true;
				}).error(function(data, status, headers, config){
					reference.error = true;
				}).finally(function(){
					reference.loading = false;
				});
			}finally{
				$event.stopPropagation();
			}
		}
		
		$scope.referenceSelected = function(reference){
			if(kubikReferenceSearch.referenceSelected != undefined){
				kubikReferenceSearch.referenceSelected(reference);
			}
		};
		
		$scope.openCard = function($event, reference){
			try{
				$scope.$emit("openReferenceCard", reference);
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
			};
			
			$http.get(kubikReferenceSearch.referenceUrl + "?" + $.param(params)).success(function(searchResult){
				$scope.searchResult = searchResult;

				$timeout(function(){
					for(var referenceIndex in searchResult.content){
						var reference = searchResult.content[referenceIndex];
						
						$("#reference-image-" + reference.id).attr(
							"src", 
							"http://images1.centprod.com/" + $scope.company.ean13 + "/" + reference.imageEncryptedKey + "-cover-thumb.jpg"
						);
					}					
				});
			});
		};
		
		$scope.query = "";
		$scope.page = 0;
		$scope.resultPerPage = kubikReferenceSearch.resultPerPage
		$scope.sortBy = kubikReferenceSearch.defaultSortBy;
		$scope.direction = kubikReferenceSearch.defaultSortDirection;
		$scope.referenceCreationAllowed = kubikReferenceSearch.referenceCreationAllowed;

		$.get("/company").success(function(company){
			$scope.company = company;
			
			$scope.search();
		});
		
		kubikReferenceSearch.$scope = $scope;
	});
};

window.KubikReferenceSearch.prototype.closeSearchModal = function(){
	this.modal.modal("hide");
};

window.KubikReferenceSearch.prototype.openSearchModal = function(searchQuery){
	var KubikReferenceSearch = this;
	this.modal.modal({
		backdrop : "static",
		keyboard : false
	}).on("shown.bs.modal", function(){
		$("#search-reference-query").focus();
		
		if(searchQuery != undefined){
			KubikReferenceSearch.$scope.query = searchQuery;
			KubikReferenceSearch.$scope.search();	
		}
	});
	
};