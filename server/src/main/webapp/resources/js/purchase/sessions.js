(function(){
	angular
		.module("Kubik")
		.controller("SessionCtrl", SessionCtrl);
	
	function SessionCtrl($scope, $http){
		var vm = this;

		vm.page = 0;
		vm.resultPerPage = 50;
		vm.sortBy = "openDate";
		vm.direction = "DESC";
		
		vm.changePage = changePage;
		vm.loadSessions = loadSessions;
		vm.loadUserAndSessions = loadUserAndSessions;
		vm.newSession = newSession;
		vm.openSession = openSession;
		vm.updateStatus = updateStatus;
		
		loadUserAndSessions();
		
		function changePage(page){
			vm.page = page;
			
			vm.loadSessions();
		}
		
		function loadSessions(){
			var params = {	status : vm.user.preferences.purchaseSession.status, 
							page : vm.page,
							resultPerPage : vm.resultPerPage,
							sortBy : vm.sortBy,
							direction : vm.direction};

			$http.get("purchaseSession?" + $.param(params)).success(function(sessionsPage){
				vm.sessionsPage = sessionsPage;
			});
		};
		
		function loadUserAndSessions(){
			$http.get("user").success(function(user){
				vm.user = user;
				
				vm.loadSessions();
			})
		}
		
		function newSession(){
			$http.post("purchaseSession", {}).success(function(session){
				vm.openSession(session.id);
			});
		};
		
		function openSession(id){
			window.location.href = "purchaseSession/" + id;
		};
		
		function updateStatus(status){		
			var statusIndex = vm.user.preferences.purchaseSession.status.indexOf(status);
			if(statusIndex != -1){
				vm.user.preferences.purchaseSession.status.splice(statusIndex, 1);
			}else{
				vm.user.preferences.purchaseSession.status.push(status);
			}
			
			$http.post("user", vm.user);
			
			vm.loadSessions();
		};
	}
})();