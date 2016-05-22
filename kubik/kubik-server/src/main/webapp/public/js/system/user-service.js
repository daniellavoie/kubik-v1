(function(){
	var USER_URL = "/user";
	
	angular
		.module("Kubik")
		.factory("userService", UserService);
	
	function UserService($http){
		return {
			createUser : createUser,
			find : find,
			updateUserPassword : updateUserPassword
		};
		
		function createUser(user){
			return $http
				.post(USER_URL + "/new?password=" + user.password, user)
				.then(postSuccess);
			
			function postSuccess(response){
				return response.data;
			}
		}
		
		function updateUserPassword(password){
			return $http
				.post(USER_URL + "?password=" + password)
		}
	}
})();