(function(){
	var SUPPLIER_URL = "/supplier";
	
	var $modal = $(".supplier-card");
	var $saveBtn = $modal.find(".save");
	var $modifyBtn = $modal.find(".modify");
	var $closeBtn = $modal.find(".closeModal");
	var $cancelBtn = $modal.find(".cancel");
	
	angular
		.module("Kubik")
		.controller("SupplierCardCtrl", SupplierCardCtrl);
	
	function SupplierCardCtrl($scope, $http, $timeout){
		var vm = this;
		
		vm.editMode = false;
		
		vm.cancelModify = cancelModify;
		vm.closeCard = closeCard;
		vm.endEditMode = endEditMode;
		vm.modify = modify;
		vm.openCard = openCard;
		vm.save = save;
		
		$scope.$on("openSupplierCard", function($event, supplier){
			vm.openCard(supplier);
		});
		
		function cancelModify(){
			vm.supplier = vm.originalSupplier;
			
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
			
			vm.originalSupplier = $.extend(true, {}, vm.supplier);
		}
		
		function openCard(supplier){
			$modal.on('show.bs.modal', function (e) {
				vm.supplier = supplier;
				
				if(supplier.id == undefined){
					vm.modify();
				}
			}).modal({
				backdrop : "static",
				keyboard : false
			});
		}
		
		function save(){
			var supplier = vm.supplier;
			
			if(supplier.ean13.trim() != "" && supplier.name.trim() != ""){
				$http.post(SUPPLIER_URL, supplier).success(supplierSaved);
			}
			
			function supplierSaved(supplier){				
				vm.endEditMode();
				
				$scope.$emit("supplierSaved", supplier);
			}
		}
	}
})();