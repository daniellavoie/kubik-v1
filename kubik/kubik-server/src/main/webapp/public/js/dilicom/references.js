(function(){
	var REFERENCE_URL = "/reference";
	
	$modal = $(".references-modal");
	$searchInput = $("#search-reference-query");
	
	angular
		.module("Kubik")
		.controller("ReferenceSearchCtrl", ReferenceSearchCtrl);
	
	function ReferenceSearchCtrl(supplierService, $scope, $http, $timeout){
		var vm = this;
		
		vm.query = "";
		vm.page = 0;
		vm.resultPerPage = 50;
		vm.sortBy = "extendedLabel";
		vm.direction = "ASC";
		vm.referenceCreationAllowed = true;
		
		loadCompany();
		
		vm.changePage = changePage;
		vm.closeModal = closeModal;
		vm.createProduct = createProduct;
		vm.loadCompany = loadCompany;
		vm.openCard = openCard;
		vm.openModal = openModal;
		vm.referenceSelected = referenceSelected;
		vm.search = search;
		vm.sort = sort;
		
		$scope.$on("openReferenceSearchModal", function($event, options){
			vm.openModal(options);
		})
		
		$scope.$on("search", vm.search);
		
		function changePage(page){
			vm.page = page;
			
			vm.search();
		}
		
		function closeModal(){
			$modal.modal("hide");
		}
		
		function createProduct($event, reference){
			$event.stopPropagation();
			
			reference.loading = true;
			$http.post("/reference/" + reference.id + "?createProduct").success(function(product){
				reference.importedInKubik = true;
			}).error(function(data, status, headers, config){
				reference.error = true;
			}).finally(function(){
				reference.loading = false;
			});
		}

		function loadCompany(){
			$.get("/company").success(function(company){
				vm.company = company;
				
				vm.search();
			});
		}
		
		function openCard($event, reference){
			$event.stopPropagation();
			
			$scope.$broadcast("openReferenceCard", reference);
		};
		
		function openModal(options){
			$modal.on("shown.bs.modal", function(){
				$searchInput.focus();
				
				if(options == undefined) options = {};
				
				if(options.query != undefined){
					vm.query = options.query;
					vm.search();	
				}
			}).modal({
				backdrop : "static",
				keyboard : false
			});
		}
		
		function referenceSelected(reference){
			$scope.$emit("referenceSelected", reference);
		};
		
		function search(){
			var params = {
				search : "",
				query : vm.query,
				page : vm.page,
				resultPerPage : vm.resultPerPage,
				sortBy : vm.sortBy,
				direction : vm.direction 
			};
			
			$http
				.get(REFERENCE_URL + "?" + $.param(params))
				.then(searchSuccess);
			
			function searchSuccess(response){
				vm.searchResult = response.data;

				angular.forEach(vm.searchResult.content, onElement);
				
				function onElement(reference, index){
					supplierService
						.findByEan13(reference.supplierEan13)
						.then(findSupplierSuccess)
						.catch(loadDilicomImage);
					
					function findSupplierSuccess(supplier){
						reference.supplierName = supplier.name;
						
						loadDilicomImage(reference);
					}
					
	
					$timeout(function(){
						loadDilicomImage(reference);
					});
					
					function loadDilicomImage(reference){							
						$("#reference-image-" + reference.id).attr(
							"src", 
							"http://images1.centprod.com/" + vm.company.ean13 + "/" + reference.imageEncryptedKey + "-cover-thumb.jpg"
						);
					}
				}
			}
		};
		
		function sort(sortBy, direction){
			vm.sortBy = sortBy;
			vm.direction = direction;
			
			vm.search();
		}
	};
})();