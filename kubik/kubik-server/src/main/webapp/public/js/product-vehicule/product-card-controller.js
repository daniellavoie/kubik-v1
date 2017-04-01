(function(){
	angular
		.module("KubikProductVehicule")
		.controller("ProductCardCtrl", ProductCardCtrl);
	
	function ProductCardCtrl(productService, categoryService, supplierService, uploadFileService, $scope, $timeout){
		var vm = this;
		
		vm.cancelModify = cancelModify;
		vm.endEditMode = endEditMode;
		vm.modify = modify;
		vm.modifyImage = modifyImage;
		vm.onEan13ChangeEvent = onEan13ChangeEvent;
		vm.openProductCategories = openProductCategories;
		vm.save = save;
		vm.editMode = false;
		
		var $saveBtn = $(".btn.save");
		var $cancelBtn = $(".btn.cancel");
		var $modifyBtn = $(".btn.modify");
		var $closeBtn = $(".btn.modal-close");

		$scope.$on("openProductCard", openCardEvent);
		
		function cancelModify(){
			vm.product = vm.originalProduct;
			
			vm.endEditMode();
		}
		
		function endEditMode(){
			vm.editMode = false;
			
			$saveBtn.addClass("hidden");
			$cancelBtn.addClass("hidden");
			$modifyBtn.removeClass("hidden");
			$closeBtn.removeClass("hidden");
			
			modifyImage();
		}
		
		function modify(){
			vm.editMode = true;
			
			$saveBtn.removeClass("hidden");
			$cancelBtn.removeClass("hidden");
			$modifyBtn.addClass("hidden");
			$closeBtn.addClass("hidden");
			
			vm.originalProduct = $.extend(true, {}, vm.product);
			
			modifyImage();
		}
		
		function modifyImage(){
			vm.editingImage = true;

			$timeout(onTimeout);
			
			function onTimeout(){
				$scope.$broadcast("productImages-setProductEan13", vm.product.ean13);
			}
		}
		
		function onEan13ChangeEvent($event){
			$scope.$broadcast("productImages-setProductEan13", vm.product.ean13);
		}
		
		function openCard(product){
			vm.product = product;
			vm.category = undefined;
			vm.supplier = undefined;
			
			return supplierService
				.findAll()
				.then(loadSuppliersSuccess);
			
			function loadSuppliersSuccess(suppliers){
				vm.suppliers = suppliers;
				
				if(product.ean13 == undefined)
					displayProduct();
				else{
					for(var supplierIndex in suppliers){
						if(suppliers[supplierIndex].id == product.supplierId)
							vm.supplier = suppliers[supplierIndex];
					}
					
					productService
						.findOne(product.ean13)
						.then(loadCategory);
				}
				
				function loadCategory(product){
					return categoryService
						.findOne(product.categoryId)
						.then(findCategorySuccess); 
					
					function findCategorySuccess(category){
						vm.category = category;
						
						displayProduct();
					}
				}
				
				function displayProduct(){
					if(vm.suppliers != undefined && vm.suppliers.length > 0 && vm.supplier == undefined)
						vm.supplier = vm.suppliers[0];
					
					$(".product-card.modal")
						.on('show.bs.modal', onModalEvent)
						.modal({backdrop : "static", keyboard : false});
					
					function onModalEvent(event){
						$timeout(onTimeout);
						
						function onTimeout(){
							if(product.ean13 == undefined)
								vm.modify();
						}
					}
				}
			}
		}
		
		function openProductCategories(){
			$scope.$broadcast("openProductCategories", {categorySelected : categorySelected});
			
			function categorySelected(category){
				categoryService.findAll()
					.then(findAllSuccess);
				
				function findAllSuccess(categories){
					var thirdCategory;
					var secondCategory;
					var firstCategory = category;
					vm.product.categoryLevel1Name = undefined;
					vm.product.categoryLevel2Name = undefined;
					vm.product.categoryLevel3Name = undefined;
					
					if(firstCategory.parentCategory != null)
						secondCategory = findCategory(firstCategory.parentCategory.id, categories);
					if(secondCategory != null && secondCategory.parentCategory)
						thirdCategory = findCategory(category.parentCategory.id, categories);
					
					vm.product.categoryId = firstCategory.id;
					// Level1 Category
					if(thirdCategory != null)
						vm.product.categoryLevel1Name = thirdCategory.name;
					else if(secondCategory != null)
						vm.product.categoryLevel1Name = secondCategory.name;
					else
						vm.product.categoryLevel1Name = firstCategory.name;
						
				    // Level2 Category
					if(thirdCategory != null)
						vm.product.categoryLevel2Name = secondCategory.name;
					else if(secondCategory != null)
						vm.product.categoryLevel2Name = firstCategory.name;
					
					if(thirdCategory != null)
						vm.product.categoryLevel3Name = firstCategory.name;

					vm.category = category;
					
					$scope.$broadcast("closeProductCategoriesModal");
				}
				
				function findCategory(categoryId, categories){
					var categoryToFind;
					for(var i in categories){
						var category = categories[i];
						if(categoryToFind == null)
							if(category.id == categoryId)
								categoryToFind = category;
							else
								categoryToFind = findCategory(categoryId, category.childCategories);
					}
					
					return categoryToFind;
				}
			}
		}
		
		function openCardEvent(event, product){
			openCard(product);
		}
		
		function save(){			
			vm.productErrors = [];
			
			if(vm.supplier != undefined)
				vm.product.supplierId = vm.supplier.id;
			
			if(vm.product.ean13 == undefined || vm.product.ean13 == "")
				vm.productErrors.push("Ean13 non renseigné.");
			
			if(vm.product.buyingPrice == undefined || vm.product.buyingPrice == "")
				vm.productErrors.push("Prix achat HT non rensigné.");
			if(vm.product.sellingPrice == undefined || vm.product.sellingPrice == "")
				vm.productErrors.push("Prix vente TTC non renseigné.");
			if(vm.product.supplierId == undefined)
				vm.productErrors.push("Fournisseur manquant.");
			
				
			if(vm.productErrors.length == 0){
				productService
					.save(vm.product)
					.then(saveSuccess);
				
				function saveSuccess(product){
					vm.product = product;
					vm.endEditMode();
					
					$scope.$broadcast("productImages-validate");
				}
			}
		}
	}
})();