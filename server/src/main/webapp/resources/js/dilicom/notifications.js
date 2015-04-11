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
	publisherPresentation : "Présentation auteur",
	isbn : "ISBN",
	supplierReference : "Référence fournisseur",
	collectionReference : "Référence collection",
	theme : "Thème",
	publisherIsnb : "ISBN Fournisseur",
	orderableByUnit : "Commande à l'unité"
};

var app = angular.module("KubikNotifications", []);

app.controller("KubikNotificationsController", function($scope, $http){
	$scope.autoValidateNotification = function($event, notification){
		try{
			$scope.notification = notification;
			
			$scope.validateNotification(false);
		}finally{
			$event.stopPropagation();
		}
	};
	
	$scope.changeOverwrite = function(attribute, overwrite){
		$scope.attributesInDiff[attribute].overwrite = overwrite;
	};
	
	$scope.changePage = function(page){
		$scope.page = page;
		
		$scope.loadNotifications();
	}
	
	$scope.ignoreNotification = function($event, notification){
		try{
			$scope.notification = notification;
			
			$scope.overwriteNoAttribute();
			
			$scope.validateNotification(false);
		}finally{
			$event.stopPropagation();
		}
	}
	
	$scope.loadNotifications = function(successCallback){
		var params = {	page : $scope.page,
						resultPerPage : $scope.resultPerPage,
						sortBy : $scope.sortBy,
						direction : $scope.direction};
		
		$http.get("notification?" + $.param(params)).success(function(notificationsPage){
			$scope.notificationsPage = notificationsPage;
			
			if(successCallback != undefined){
				successCallback();
			}
		});
	};
	
	$scope.openNotification = function(notification){
		$scope.notification = notification;
		
		var params = {	ean13 : notification.product.ean13, 
						supplierEan13 : notification.product.supplier.ean13};
		
		$http.get("reference/product?" + $.param(params)).success(function(newProduct){
			$scope.attributesInDiff = {};
			
			for(var attribute in attributesMapping){
				if(attributesMapping.hasOwnProperty(attribute)){
					var oldAttributeValue = notification.product[attribute];
					var newAttributeValue = newProduct[attribute];
					
					if(oldAttributeValue != newAttributeValue){
						$scope.attributesInDiff[attribute] = {
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
	};
	
	$scope.overwriteAllAttributes = function(){
		for(var attributeInDiff in $scope.attributesInDiff){
			$scope.attributesInDiff[attributeInDiff].overwrite = true;
		}
	};
	
	$scope.overwriteNoAttribute = function(){
		for(var attributeInDiff in $scope.attributesInDiff){
			$scope.attributesInDiff[attributeInDiff].overwrite = false;
		}
	};
	
	$scope.validateNotification = function(openNextNotification){
		for(var attributeInDiffIndex in $scope.attributesInDiff){
			var attributeInDiff = $scope.attributesInDiff[attributeInDiffIndex];
			
			if(attributeInDiff.overwrite){
				$scope.notification.product[attributeInDiff.attribute] = attributeInDiff.newValue;
			}
		}
		
		$http.post("notification/" + $scope.notification.id, $scope.notification.product).success(function(){
			$scope.loadNotifications(function(){
				if(openNextNotification != undefined && openNextNotification && $scope.notificationsPage.content.length > 0){
					$scope.openNotification($scope.notificationsPage.content[0]);					
				}else{
					$(".notification-modal").modal("hide");
				}
			});
		});
	}
	
	$scope.page = 0;
	$scope.resultPerPage = 50;
	$scope.sortBy = "creationDate";
	$scope.direction = "ASC";
	
	$scope.loadNotifications();
});