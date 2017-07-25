var app = app || {};
app.User = Backbone.Model.extend({
	urlRoot: "site/users",
	defaults: {
		"userId": null,
		"userFirstName": null,
		"userLastName": null,
		"userImage": null,
		"userCreatedDate": null,
		"isUserDeleted": null,
		"totalScore": null,
		"currentScore": null,
		"fbId": null
	}
});