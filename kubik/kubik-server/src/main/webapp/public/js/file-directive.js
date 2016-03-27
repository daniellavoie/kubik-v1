(function(){
	angular
		.module("Kubik")
		.directive("fileModel", FileModel)
	
	function FileModel($parse){
		return {
			restrict: 'A',
			link: link
		};
		
		function link(scope, element, attrs){
			var model = $parse(attrs.fileModel);
			var onChange = $parse(attrs.fileChange);
            var modelSetter = model.assign;
            
            element.bind('change', onChangeEvent);
            
            function onChangeEvent(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                    
                    if(onChange != undefined){
                    	onChange(scope);
                    }
                });
            }
		}
	}
})();