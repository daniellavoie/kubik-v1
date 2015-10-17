(function(){
	var PRODUCT_URL = "/product";
	
	var $modal = $(".products-modal");
	
	angular
		.module("Kubik")
		.controller("ProductSearchCtrl", ProductSearchCtrl);
	
	function ProductSearchCtrl($scope, $http, $timeout, $controller){
		var vm = this;
		
		vm.query = "";
		vm.page = 0;
		vm.resultPerPage = 50;
		vm.sortBy = "extendedLabel";
		vm.direction = "ASC";
		vm.productCreationAllowed = true;
		
		vm.changePage = changePage;
		vm.closeModal = closeModal;
		vm.loadCompany = loadCompany;
		vm.newProduct = newProduct;
		vm.openProductCard = openProductCard;
		vm.openModal = openModal;
		vm.productSelected = productSelected;
		vm.sort = sort;
		vm.search = search;
		
		loadCompany();
		
		$scope.$on("openProductSearchModal", function($event, options){
			openModal(options);
		});
		
		$scope.$on("productSaved", function(event, product){
			vm.search();
		});
		
		function changePage(page){
			vm.page = page;
			
			vm.search();
		}
		
		function closeModal(){
			$modal.modal("hide");
		}

		function loadCompany(){
			$.get("/company").success(companyLoaded);
			
			function companyLoaded(company){
				vm.company = company;
				
				vm.search();
			}
		}
		
		function newProduct(){
			$scope.$broadcast("openProductCard", {});
		};
		
		function openModal(options){
			$modal.modal({
				backdrop : "static",
				keyboard : false
			}).on("shown.bs.modal", function(){
				$("#search-product-query").focus();
				
				if(options == undefined) options = {};
				
				if(options.query != undefined){
					vm.query = options.query;
					vm.search();	
				}
			});
		}
		
		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
		
		function productSelected($event, product){			
			$scope.$emit("productSelected", product);
		}
		
		function sort(sortBy, direction){
			vm.sortBy = sortBy;
			vm.direction = direction;
			
			vm.search();
		}
		
		function search(){
			var params = {
				search : "",
				query : vm.query,
				page : vm.page,
				resultPerPage : vm.resultPerPage,
				sortBy : vm.sortBy,
				direction : vm.direction 
			};
			
			$http.get(PRODUCT_URL + "?" + $.param(params)).success(searchCompleted);
			
			function searchCompleted(searchResult){
				vm.searchResult = searchResult;

				$timeout(function(){
					for(var productIndex in searchResult.content){
						var product = searchResult.content[productIndex];
						
						$("#product-image-" + product.id).attr(
							"src", 
							"http://images1.centprod.com/" + vm.company.ean13 + "/" + product.imageEncryptedKey + "-cover-thumb.jpg"
						);
					}					
				});
			}
		};
	}
})();