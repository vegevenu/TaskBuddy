window.fbAsyncInit = function() {
	FB.init({
		appId      : '1491815327739701',
		xfbml      : true,
		version    : 'v2.2'
	});
};

(function(d, s, id){
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) {return;}
	js = d.createElement(s); js.id = id;
	js.src = "//connect.facebook.net/en_US/sdk.js";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

function fbLogin(){
	FB.login(function(response){
		FB.getLoginStatus(function(response) {
			if(response.status === "connected"){
				welcomeUser();
			}else{
				alert("Please Login and Authorize TaskBuddy!!!");
				FB.login();
			}
		});
	});
}

function welcomeUser(){
	FB.api('/me', function(response){
		$.post("signin",
				{
			fbId: response.id,
			firstName: response.first_name,
			lastName: response.last_name
				},
				function(data, status){
					var userId = parseInt(data);
					if(userId != -1){
						window.location.href = "main.html?id=" + userId;
					}else{
						alert("Error: Something went wrong!!!");
					}
				});

	}); // End of Fb.api 
}

function fbLogout() {

	FB.getLoginStatus(function(response) {
		if (response && response.status === 'connected') {
			FB.logout(function(response) {
				document.location.reload();
				window.location.href = "home.html";
			});
		}
	});
}
