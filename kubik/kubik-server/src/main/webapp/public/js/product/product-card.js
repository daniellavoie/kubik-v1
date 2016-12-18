(function(){
	var INVENTORY_COUNT_URL = "/inventoryCount";
	var PRODUCT_URL = "/product";
	
	var $modal = $(".kubikProductCard");
	var $saveBtn = $modal.find(".save")
	var $modifyBtn = $modal.find(".modify")
	var $closeBtn = $modal.find(".closeModal")
	var $cancelBtn = $modal.find(".cancel")
	
	angular
		.module("Kubik")
		.controller("ProductCardCtrl", ProductCardCtrl);
	
	function ProductCardCtrl($scope, $http, $timeout, $controller){
		var vm = this;
		
		vm.editMode = false;
		vm.product = {};
		vm.inventoryTabs = {	customerCredit : {	searchParams : {
										page : 0,
										resultPerPage : 20,
										sortBy : "customerCredit.completeDate",
										direction : "DESC" }
								}, 
								paidInvoice : {	searchParams : {
									page : 0,
									resultPerPage : 20,
									sortBy : "invoice.paidDate",
									direction : "DESC"  }
								}, 
								confirmedInvoice : {	searchParams : {
									page : 0,
									resultPerPage : 20,
									sortBy : "invoice.confirmedDate",
									direction : "DESC"  }
								}, 
								reception : {	searchParams : {
									page : 0,
									resultPerPage : 20,
									sortBy : "reception.dateReceived",
									direction : "DESC"  }
								}, 
								rma : {	searchParams : {
									page : 0,
									resultPerPage : 20,
									sortBy : "rma.shippedDate",
									direction : "DESC"  }
								},
								inventoryCount : {	searchParams : {
									page : 0,
									resultPerPage : 20,
									sortBy : "dateCounted",
									direction : "DESC"  }
								}};
		
		loadSupplier();
		
		vm.addInventoryCount = addInventoryCount;
		vm.cancelInventoryCount = cancelInventoryCount;
		vm.cancelModify = cancelModify;
		vm.changeInventoryTab = changeInventoryTab;
		vm.changePage = changePage;
		vm.changeTab = changeTab;
		vm.confirmInventoryCount = confirmInventoryCount;
		vm.endEditMode = endEditMode;
		vm.getSupplier = getSupplier;
		vm.loadCompany = loadCompany;
		vm.loadInventoryTab = loadInventoryTab;
		vm.loadInventoryTabs = loadInventoryTabs;
		vm.loadProduct = loadProduct;
		vm.loadSupplier = loadSupplier;
		vm.modify = modify;
		vm.modifyImage = modifyImage;
		vm.openCard = openCard;
		vm.openProductCategories = openProductCategories;
		vm.save = save;
		
		$scope.$on("openProductCard", function(event, product){
			vm.openCard(product);
		});
		
		
		function addInventoryCount(){
			vm.inventoryCount = {product : vm.product, dateCounted : new Date(), quandity : 0, reason : ""};
		}
		
		function cancelInventoryCount(){
			vm.inventoryCount = null;
		}
		
		function cancelModify(){
			vm.product = vm.originalProduct;
			
			vm.endEditMode();
		}
		
		function changeInventoryTab(tabClassName){
			if(vm.inventoryTab != tabClassName){
				vm.inventoryTab = tabClassName;
			}
		}
		
		function changePage(page){
			vm.inventoryTabs[vm.inventoryTab].searchParams.page = page;
			
			vm.loadInventoryTab(vm.inventoryTab);
		}
		
		function changeTab(tab){
			vm.productTab = tab;
		}
		
		function confirmInventoryCount(){
			$http.post(INVENTORY_COUNT_URL, vm.inventoryCount).finally(confirmInventoryCountCompleted);
			
			function confirmInventoryCountCompleted(){
				vm.inventoryCount = null;
				
				vm.loadProduct(vm.product.id);
				vm.changePage(0);
			}
		}
		
		function endEditMode(){
			vm.editMode = false;
			
			$saveBtn.addClass("hidden");
			$cancelBtn.addClass("hidden");
			$modifyBtn.removeClass("hidden");
			$closeBtn.removeClass("hidden");
		}
		
		function getSupplier(id){
			for(var supplierIndex in vm.suppliers){
				var supplier = vm.suppliers[supplierIndex];
				if(supplier.id == id){
					return supplier;
				}
			}
			
			return null;
		}
		
		function loadCompany(){
			$.get("/company").success(companyLoaded);
			
			function companyLoaded(company){
				vm.company = company;
			}
		}
		
		function loadInventoryTab(tabName){
			var url = PRODUCT_URL + "/" + vm.product.id + "/" + tabName + "?" + $.param(vm.inventoryTabs[tabName].searchParams);
			
			$http.get(url).success(tabLoaded);
			
			function tabLoaded(searchResult){
				vm.inventoryTabs[tabName].result = searchResult;
			}
		}
		
		function loadInventoryTabs(){
			for(var inventoryTabIndex in vm.inventoryTabs){				
				vm.loadInventoryTab(inventoryTabIndex);
			}
		}
		
		function loadProduct(productId, loadProductSuccessCallback){
			$http.get(PRODUCT_URL + "/" + productId).success(loadProductSuccess);
			
			function loadProductSuccess(product){
				vm.product = product;
				
				if(loadProductSuccessCallback != undefined){
					loadProductSuccessCallback(product);					
				}
			}
		}
		
		function loadSupplier(){
			$http.get("/supplier").success(function(suppliers){
				vm.suppliers = suppliers;
			});
		}
		
		function modify(){
			vm.editMode = true;
			
			$saveBtn.removeClass("hidden");
			$cancelBtn.removeClass("hidden");
			$modifyBtn.addClass("hidden");
			$closeBtn.addClass("hidden");
			
			vm.originalProduct = $.extend(true, {}, vm.product);
			
			$timeout(function(){
				if(vm.editMode){
					$(".date-published, .publish-end-date").each(function(index, element){
						var $element = $(element);
						
						if($element.val() != ""){
							$element.val(moment(parseInt($element.val())).format("DD/MM/YYYY"))							
						}
						
						$element.datepicker({format : 'dd/mm/yyyy'});
					});
				}
				
				var supplier = null;
				if(vm.product.supplier != null) {
					supplier = vm.getSupplier(vm.product.supplier.id);
				}
				if(supplier == null){
					supplier = vm.suppliers[0];
				}
				
				if(supplier != null)
					$("#product-supplier-" + supplier.id).attr("SELECTED", "SELECTED");
				
				$scope.$apply();
			});
		}
		
		function modifyImage(){
			vm.editingImage = true;

			$timeout(function(){
				$scope.$broadcast("productImages-setProductId", vm.product.id);					
			});
		}
		
		function openCard(product){
			if(product.id == undefined){
				displayProduct(product);
			}else{
				vm.loadProduct(product.id, displayProduct);
			}
			
			function displayProduct(product){
				$modal.on('show.bs.modal', function (e) {
					$timeout(function(){
						vm.productTab = "product";
						vm.inventoryTab = "reception";
						
						if(product.id == undefined){
							vm.modify();
						}else{
							vm.loadInventoryTabs();
						}
					});
				}).modal({
					backdrop : "static",
					keyboard : false
				});;
			}
		}
		
		function openProductCategories(){
			$scope.$broadcast("openProductCategories", {categorySelected : categorySelected});
			
			function categorySelected(category){
				vm.product.category = category;
				
				$scope.$broadcast("closeProductCategoriesModal");
			}
		}
		
		function save(){			
			if(!vm.product.dilicomReference){
				vm.product.supplier = vm.getSupplier($("select.product-supplier").val());
			}
			
			vm.productErrors = [];
			
			if(vm.product.ean13 == undefined || vm.product.ean13 == "")
				vm.productErrors.push("Ean13 non renseigné.");
			
			if(vm.product.priceTaxIn == undefined || vm.product.priceTaxIn == "")
				vm.productErrors.push("Prix TTC non rensigné.");
			if(vm.product.supplier == null)
				vm.productErrors.push("Fournisseur non renseigné.");
				
			if(vm.productErrors.length == 0){
				vm.product.datePublished = $(".date-published").datepicker("getDate");
				vm.product.publishEndDate = $(".publish-end-date").datepicker("getDate");
				
				$http.post(PRODUCT_URL, vm.product).success(function(product){
					vm.product = product;
					vm.endEditMode();
					$scope.$emit("productSaved", product)
				});
			}
		}
	}
})();
