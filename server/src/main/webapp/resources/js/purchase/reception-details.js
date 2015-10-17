(function(){
	var receptionId = window.location.pathname.split("/")[2];
	
	angular
		.module("Kubik")
		.controller("ReceptionDetailsCtrl", ReceptionDetailsCtrl);
	
	function ReceptionDetailsCtrl($scope, $http, $timeout){
		var vm = this;
		
		vm.calculateReceptionQuantity = calculateReceptionQuantity;
		vm.cancelReception = cancelReception;
		vm.confirmReceptionValidation = confirmReceptionValidation;
		vm.loadReception = loadReception;
		vm.openProductCard = openProductCard;
		vm.openSupplierCard = openSupplierCard;
		vm.receptionChanged = receptionChanged;
		vm.redirectToPurchaseOrders = redirectToPurchaseOrders;
		vm.redirectToReceptions = redirectToReceptions;
		vm.saveReception = saveReception;
		vm.validateReception = validateReception;
		
		loadReception();
		
		$scope.$on("supplierSaved", function($event, supplier){
			vm.loadReception();
		});
		
		function calculateReceptionQuantity(reception){
			var quantity = 0;
			if(reception != undefined){
				for(var detailIndex in reception.details){
					var detail = reception.details[detailIndex];
					
					quantity += detail.quantityToReceive;
				}
			}
			
			return quantity;
		}

		function cancelReception(){
			vm.order.status = "CANCELED";
			
			vm.saveOrder(function(){
				$(".confirm-cancel").modal("hide");
				
				$(".redirection-modal").modal();
			});
		}

		function confirmReceptionValidation(){
			$(".confirm-validation").modal();
		}
		
		function loadReception(){
			$http.get(receptionId).success(receptionLoaded);
			
			function receptionLoaded(reception){
				vm.reception = reception;

				$timeout(function(){
					if(vm.inputIdToFocus != undefined){
						$("#" + vm.inputIdToFocus).focus();
					}
				});
			}
		}
		
		function openProductCard($event, product){
			$event.stopPropagation();
			
			$scope.$broadcast("openProductCard", product);
		}
		
		function openSupplierCard($event, supplier){
			$event.stopPropagation();
			
			$scope.$broadcast("openSupplierCard", supplier);
		}
		
		function receptionChanged($event){
			vm.inputIdToFocus = $event.target.id;
			if(vm.quantityChangedTimer != undefined) clearTimeout(vm.quantityChangedTimer);
		    
			vm.quantityChangedTimer = setTimeout(vm.saveReception, 1000);
		}
		
		function redirectToPurchaseOrders(){
			location.href = "../purchaseOrder";
		}
		
		function redirectToReceptions(){
			location.href = "../reception";		
		}
		
		function saveReception(success){
			$http.post(".", vm.reception).success(receptionSaved);
			
			function receptionSaved(reception){				
				vm.loadReception();
				
				if(success != undefined){
					success();
				}
			}
		}

		function validateReception(){
			vm.reception.status = "CLOSED";
			
			vm.saveReception(receptionSaved);
			
			function receptionSaved(){
				$(".confirm-validation").modal("hide");
				
				$(".redirection-modal").modal();
			}
		}
	}
})();