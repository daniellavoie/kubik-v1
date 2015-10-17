(function(){
	var CUSTOMER_URL = "/customer";
	
	var $modal = $(".customers-modal");
	
	angular
		.module("Kubik")
		.controller("CustomerSearchCtrl", CustomerSearchCtrl);
	
	function CustomerSearchCtrl($scope, $http, $timeout){
		var vm = this;
		
		vm.query = "";
		vm.page = 0;
		vm.resultPerPage = 50;
		vm.defaultSortBy = "lastName";
		vm.defaultSortDirection = "ASC";
		vm.customerCreationAllowed = true;
		
		vm.changePage = changePage;
		vm.closeModal = closeModal;
		vm.customerSelected = customerSelected;
		vm.newCustomer = newCustomer;
		vm.openCard = openCard;
		vm.openModal = openModal;
		vm.sort = sort;
		vm.search = search;

		search();
		
		$scope.$on("closeCustomerSearchModal", function($event){
			vm.closeModal();
		})
		
		$scope.$on("customerSaved", function($event, customer){
			vm.search();
		})
		
		$scope.$on("openCustomerSearchModal", function($event){
			vm.openModal();
		});
		
		function changePage(page){
			vm.page = page;
			
			vm.search();
		}
		
		function closeModal(){
			$modal.modal("hide");
		}

		function customerSelected(customer){
			$scope.$emit("customerSelected", customer);
		}
		
		function newCustomer(){
			vm.openCard(null, {});
		}

		function openCard($event, customer){
			if($event != undefined) $event.stopPropagation();
			
			$scope.$broadcast("openCustomerCard", customer);
		}
		
		function openModal(){
			$modal.modal();
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
			}
			
			$http.get(CUSTOMER_URL + "?" + $.param(params)).success(searchSuccess);
			
			function searchSuccess(searchResult){
				vm.searchResult = searchResult;
			}
		}
	}
})();