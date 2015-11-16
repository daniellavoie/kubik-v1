$.extend({
	getUrlVars : function() {
		var vars = {};
		
		if(window.location.href.indexOf("?") != -1){
			var hashes = window.location.href.slice(
					window.location.href.indexOf("?") + 1).split("&");
			for (var i = 0; i < hashes.length; i++) {
				var elementArray = hashes[i].split("=");
	
				vars[elementArray[0]] = elementArray[1];
			}
		}
		return vars;
	},
	
	getUrlVar : function(name) {
		return $.getUrlVars()[name];
	}
});