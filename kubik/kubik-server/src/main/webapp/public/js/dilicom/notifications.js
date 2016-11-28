(function(){
	var attributesMapping = {
			availabilityCode : "Code de disponibilité",
			priceType : "Type de prix",
			priceTaxIn : "Prix TTC",
			tvaRate1 : "Taux taxe 1",
			tvaRate2 : "Taux taxe 2",
			tvaRate3 : "Taux taxe 3",
			returnType : "Type de retour",
			availableForOrder : "Disponible à la commande",
			datePublished : "Date de publication",
			productType : "Type de produit",
			publishEndDate : "Date de fin de disponibilité",
			extendedLabel : "Titre",
			standardLabel : "Titre court",
			cashRegisterLabel : "Titre caisse",
			thickness : "Epaisseur",
			width : "Largeur",
			height : "Hauteur",
			weight : "Poids",
			publisher : "Editeur",
			collection : "Collection",
			author : "Auteur",
			publisherPresentation : "Présentation éditeur",
			isbn : "ISBN",
			supplierReference : "Référence fournisseur",
			collectionReference : "Référence collection",
			theme : "Thème",
			publisherIsnb : "ISBN Fournisseur",
			orderableByUnit : "Commande à l'unité"
		};
	
	angular
		.module("Kubik")
		.controller("DilicomNotificationsCtrl", DilicomNotificationsCtrl);
	
	function DilicomNotificationsCtrl($http){
		var vm = this;
		
		vm.page = 0;
		vm.resultPerPage = 50;
		vm.sortBy = "creationDate";
		vm.direction = "ASC";
		
		loadNotifications();
		
		vm.autoValidateNotification = autoValidateNotification;
		vm.changeOverwrite = changeOverwrite;
		vm.changePage = changePage;
		vm.ignoreNotification = ignoreNotification;
		vm.loadNotifications = loadNotifications;
		vm.openNotification = openNotification;
		vm.overwriteAllAttributes = overwriteAllAttributes;
		vm.overwriteNoAttribute = overwriteNoAttribute;
		vm.validateNotification = validateNotification;
		
		function autoValidateNotification($event, notification){
			$event.stopPropagation();
			
			vm.notification = notification;
			
			vm.validateNotification(false);
		}
		
		function changeOverwrite(attribute, overwrite){
			vm.attributesInDiff[attribute].overwrite = overwrite;
		}
		
		function changePage(page){
			vm.page = page;
			
			vm.loadNotifications();
		}
		
		function ignoreNotification($event, notification){
			$event.stopPropagation();
			
			vm.notification = notification;
			
			vm.overwriteNoAttribute();
			
			vm.validateNotification(false);
		}
		
		function loadNotifications(successCallback){
			var params = {	page : vm.page,
							resultPerPage : vm.resultPerPage,
							sortBy : vm.sortBy,
							direction : vm.direction};
			
			$http.get("notification?" + $.param(params)).success(loadNotificationSuccess);
			
			function loadNotificationSuccess(notificationsPage){
				vm.notificationsPage = notificationsPage;
			
				if(successCallback != undefined){
					successCallback();
				}
			}
		}
		
		function openNotification(notification){
			vm.notification = notification;
			
			var params = {	ean13 : notification.product.ean13, 
							supplierEan13 : notification.product.supplier.ean13};
			
			$http.get("reference/product?" + $.param(params)).success(function(newProduct){
				vm.attributesInDiff = {};
				
				for(var attribute in attributesMapping){
					if(attributesMapping.hasOwnProperty(attribute)){
						var oldAttributeValue = notification.product[attribute];
						var newAttributeValue = newProduct[attribute];
						
						if(oldAttributeValue != newAttributeValue){
							vm.attributesInDiff[attribute] = {
								attribute : attribute,
								attributeLabel : attributesMapping[attribute],
								oldValue : oldAttributeValue, 
								newValue : newAttributeValue,
								overwrite : true};
						}
					}
				}
				
				$(".notification-modal").modal();
			});
		}
		
		function overwriteAllAttributes(){
			for(var attributeInDiff in vm.attributesInDiff){
				vm.attributesInDiff[attributeInDiff].overwrite = true;
			}
		}
		
		function overwriteNoAttribute(){
			for(var attributeInDiff in vm.attributesInDiff){
				vm.attributesInDiff[attributeInDiff].overwrite = false;
			}
		}
		
		function validateNotification(openNextNotification){
			for(var attributeInDiffIndex in vm.attributesInDiff){
				var attributeInDiff = vm.attributesInDiff[attributeInDiffIndex];
				
				if(attributeInDiff.overwrite){
					vm.notification.product[attributeInDiff.attribute] = attributeInDiff.newValue;
				}
			}
			
			$http.post("notification/" + vm.notification.id, vm.notification.product).success(postNotificationSuccess);
			
			function postNotificationSuccess(){
				vm.loadNotifications(successCallback);
				
				function successCallback(){
					if(openNextNotification != undefined && openNextNotification && vm.notificationsPage.content.length > 0){
						vm.openNotification(vm.notificationsPage.content[0]);					
					}else{
						$(".notification-modal").modal("hide");
					}
				}
			}
		}
	}
})();