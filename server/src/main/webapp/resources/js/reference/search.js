window.KubikReferenceSearch = function(options){
	if(options.resultPerPage == undefined){
		options.resultPerPage = 50;		
	}
	
	if(options.defaultSortBy == undefined){
		options.defaultSortBy = "extendedLabel";		
	}
	
	if(options.defaultSortDirection == undefined){
		options.defaultSortDirection = "ASC";		
	}
	
	this.searchUrl = options.searchUrl;
	this.searchResultUrl = options.searchResultUrl;
	this.productUrl = options.productUrl;
	this.$modalContainer = options.$modalContainer
	this.importedInKubik = options.importedInKubik;
	this.resultPerPage = options.resultPerPage;
	this.defaultSortBy = options.defaultSortBy;
	this.defaultSortDirection = options.defaultSortDirection;
	this.referenceClicked = options.referenceClicked;
	this.productUrl = options.productUrl;
	this.productSelected = options.productSelected;
	
	this.init();
};

window.KubikReferenceSearch.prototype.init = function(){
	var kubikReferenceSearch = this;
	$.get(this.searchResultUrl, function(searchResultHtml) {
		kubikReferenceSearch.$modalContainer.html(searchResultHtml);
		
		var kubikReferenceSearchApp = angular.module("KubikReferenceSearch", []);

		kubikReferenceSearchApp.controller("ProductSearchController", function($scope, $http, $timeout){
			$scope.changePage = function(page){
				$scope.page = page;
				
				$scope.search();
			}
			
			$scope.keyPressed = function(){
				$scope.page = 0;
				
				$scope.search();
			}
			
			$scope.referenceSelected = function(reference){
				if(kubikReferenceSearch.referenceClicked != undefined){
					kubikReferenceSearch.referenceClicked(reference);
				}
				
				if(kubikReferenceSearch.productSelected != undefined && kubikReferenceSearch.productUrl != undefined){
					$http.get(kubikReferenceSearch.productUrl + "?" + $.param({ean13 : reference.ean13, supplierEan13 : reference.supplierEan13})).success(
						function(product){
							kubikReferenceSearch.productSelected(product);						
						}
					);
				}
			};
			
			$scope.sort = function(sortBy, direction){
				$scope.sortBy = sortBy;
				$scope.direction = direction;
				
				$scope.search();
			}
			
			$scope.search = function(){
				var fields = [];
				
				if($scope.title) fields.push("title");
				if($scope.author) fields.push("author");
				if($scope.publisher) fields.push("publisher");
				
				var params = {
					query : $scope.query,
					fields : fields,
					importedInKubik : $scope.importedInKubik,
					page : $scope.page,
					resultPerPage : $scope.resultPerPage,
					sortBy : $scope.sortBy,
					direction : $scope.direction 
				}
				
				$http.get(kubikReferenceSearch.searchUrl + "?" + $.param(params)).success(function(searchResult){
					$scope.searchResult = searchResult;
					
					$timeout(function(){
						for(var referenceIndex in searchResult.content){
							var reference = searchResult.content[referenceIndex];
							
							$("#reference-image-" + reference.id).attr(
								"src", 
								"http://images1.centprod.com/" + $scope.company.ean13 + "/" + reference.imageEncryptedKey + "-cover-thumb.jpg"
							);
						}
						
						if(kubikReferenceSearch.modal != undefined){
							kubikReferenceSearch.modal.find(".modal-backdrop")
						      .css('height', 0)
						      .css('height', kubikReferenceSearch.modal[0].scrollHeight);
						}
					});
				});
			};
			
			$scope.query = "";
			$scope.importedInKubik = kubikReferenceSearch.importedInKubik;
			$scope.page = 0;
			$scope.resultPerPage = kubikReferenceSearch.resultPerPage
			$scope.sortBy = kubikReferenceSearch.defaultSortBy;
			$scope.direction = kubikReferenceSearch.defaultSortDirection;
			
			$.get("/company").success(function(company){
				$scope.company = company;
				
				$scope.search();
			});
		});

        angular.bootstrap($(".kubikReferenceSearch")[0],['KubikReferenceSearch']);
	}, "html");
};

window.KubikReferenceSearch.prototype.openSearch = function(){
	this.modal = this.$modalContainer.find(".modal").modal({
		backdrop : "static",
		keyboard : false
	}).on("shown.bs.modal", function(){
		$("#search-product-query").focus();
	});
	
};