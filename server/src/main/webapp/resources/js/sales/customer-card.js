(function(){
	var CUSTOMER_URL = "/customer";
	
	var $modal = $(".customer-card");

	var $saveBtn = $modal.find(".save")
	var $modifyBtn = $modal.find(".modify")
	var $closeBtn = $modal.find(".closeModal")
	var $cancelBtn = $modal.find(".cancel")
	
	angular
		.module("Kubik")
		.controller("CustomerCardCtrl", CustomerCardCtrl);
	
	function CustomerCardCtrl($scope, $http, $timeout){
		var vm = this;
		
		vm.editMode = false;
		
		vm.cancelModify = cancelModify;
		vm.closeCard = closeCard;
		vm.endEditMode = endEditMode;
		vm.modify = modify;
		vm.openCard = openCard;
		vm.save = save;
		
		$scope.$on("closeCustomerCard", function(event){
			vm.closeCard();
		})
		
		$scope.$on("openCustomerCard", function(event, customer){
			vm.openCard(customer);
		});
		
		function cancelModify(){
			vm.customer = vm.originalCustomer;
			
			vm.endEditMode();
		}
		
		function closeCard(){
			$modal.modal("hide");
		}
		
		function endEditMode(){
			vm.editMode = false;
			
			$saveBtn.addClass("hidden");
			$cancelBtn.addClass("hidden");
			$modifyBtn.removeClass("hidden");
			$closeBtn.removeClass("hidden");
		}
		
		function modify(){
			vm.editMode = true;
			
			$saveBtn.removeClass("hidden");
			$cancelBtn.removeClass("hidden");
			$modifyBtn.addClass("hidden");
			$closeBtn.addClass("hidden");
			
			vm.originalCustomer = $.extend(true, {}, vm.customer);
		}
		
		function openCard(customer){
			$modal.on("shown.bs.modal", function($event){
				$timeout(function(){
					vm.customer = customer;
					
					// Load customer credit.
					if(customer.id != undefined){
						var url = CUSTOMER_URL + "/" + customer.id + "/customerCreditAmount";
						
						$http.get(url).success(customerCreditAmountLoadSuccess);
					}else{
						vm.modify();
					}
					
					function customerCreditAmountLoadSuccess(customerCreditAmount){
						vm.customerCreditAmount = customerCreditAmount;
					}
				});
			}).modal({
				backdrop : "static",
				keyboard : false
			});
		}
		
		function save(){
			$http.post(CUSTOMER_URL, vm.customer).success(saveCustomerSuccess)
			
			function saveCustomerSuccess(customer){
				vm.endEditMode();
				
				$scope.$emit("customerSaved", customer);				
			}
		}
	}
})();