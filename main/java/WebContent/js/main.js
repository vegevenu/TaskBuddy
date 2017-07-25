/*
 * Lets initialize the variables we need
 */
var $tasks 		= $('#tasks'),
$masterTasks    = $('#masterTasks'),
$users 			= $('#users'),
tasks 			= null,
users 			= null,
currentUserId 	= null,
currentUserModel = null,
userListView 	= null,
taskListView 	= null,
today 			= new Date(),
tomorrow		= new Date(),
assignedUser    = null,
serverUrl		= 'http://localhost:8080/TaskBuddy/site/',
maxPoints       =  50,
selectedRowIndex = null,
selectedRepitition = null;
base_url        = null;
/*
 * We will add one day to the current date to pre-fill Due Date
 */
tomorrow.setDate(today.getDate() + 1);

var months = {
		'1' : 'Jan', '2' : 'Feb', '3' : 'March', '4' : 'April', '5' : 'May', '6' : 'June', '7' : 'July', '8' : 'Aug', '9' : 'Sept', '10' : 'Oct', '11' : 'Nov', '12' : 'Dec'
};

/* 
 * Parse the query parameters present in the URL
 */
function parseUrlParam(urlParam){
	var parsedParam = urlParam.split("=", 2); //limiting the parsed parameters count to two
	return parsedParam[1];
}

/*
 * User model
 */
User = Backbone.Model.extend({
	idAttribute: 'userId',
	defaults: {
		userFirstName: '',
		userLastName: '',
		userImage: null,
		totalScore: 0,
		currentScore: 0,
		currentPoints: 0,
		weeklyPoints: 0,
		userCreatedDate: '',
		fbId: ''
	},
	urlRoot: serverUrl + 'users'
});

/*
 * User collection
 */
UserCollection = Backbone.Collection.extend({
	model: User,
	url: serverUrl + 'users' 
});

/*
 * View for single user item (li)
 */
UserItem = Backbone.View.extend({
	/*
	 * Defining the parent tag that will be created
	 */
	tagName: 'li',
	/*
	 * Constructor like method where we can do needed initializations
	 */
	initialize: function() {
		/*
		 * We are attaching context to render method
		 */
		this.render = _.bind(this.render, this);

		/*
		 * Defining template, we are loading underscore template here
		 */
		this.template = _.template($('#user-item').html());

		/*
		 * Bind any change in the model to render method
		 */
		this.model.bind('change', this.render);
	},
	/*
	 * Defining events which will trigger our methods
	 */
	events: {
		'dblclick a': 'edit',
		'click a': 'loadUsers'
	},
	/*
	 * Rendering template
	 */
	render: function() {
		/*
		 * Filling template with model attributes
		 */
		this.$el.html(this.template(this.model.attributes));
		return this;
	},
	/*
	 * Triggers when user double clicks on user
	 */
	edit: function() {
		/*
		 * Opening user dialog and filling it with current model (the one that was clicked)
		 */
		new UserDialog({model: this.model}).show();
	},
	/*
	 * Method which loads currently selected user tasks
	 */
	loadUsers: function() {
		/*
		 * First lets handle visual side of selecting a user, we'll remove active class for user that was selected earlier and 
		 * add class to currently selected user
		 */
		$users.find('li.active').removeClass('active');
		this.$el.addClass('active');

		/*
		 * We also need to update user title (above the tasks table)
		 */
		$('.user-title span').html(this.model.get('userFirstName'));

		/*
		 * Cleaning bit, we'll remove tasks that was tied to user selected earlier
		 */
		$tasks.empty();

		currentUserId = this.model.get('userId');
		currentUserModel = this.model;

		var goal = currentUserModel.get('currentPoints')+ currentUserModel.get('weeklyPoints')- currentUserModel.get('totalScore');


		$('#score').html(goal.toFixed(4));

		/*
		 * We'll initialize new task collection
		 */

		tasks = new TaskCollection({id: currentUserId});

		tasks.comparator = function(model) {
			return [model.get('taskCompleted'),model.get('taskDueDate')];
		}


		/*
		 * We'll assign current user ID to "global" variable as we need it on several other places
		 */

		/*
		 * Lets fetch tasks for currently selected user (we can access currently selected user through this.model)
		 * processData param here only informs the system that params provided through data param needs to be added to URL as GET params
		 */
		tasks.fetch({data: {user: this.model.taskId}, processData: true, success: function() {
			/*
			 * Initializing task list view and passing tasks collection to it
			 */
			taskListView = new TaskList({
				collection: tasks,
				/*
				 * Telling view to which DOM elements it needs to attach itself
				 */
				el: $tasks
			});
			/*
			 * Rendering list view
			 */
			taskListView.render(taskListView.getSelectedFilterOption());
			//tasks.sort();

		}});
	}
});

/*
 * View for user list, collection of users
 */
UserList = Backbone.View.extend({
	initialize: function() {
		_(this).bindAll('add', 'remove');

		/*
		 * Holder of single user views
		 */
		this._users = [];

		/*
		 * For each element in collection we run the 'add' method
		 */
		this.collection.each(this.add);

		/*
		 * Binding collection events to our methods
		 */
		this.collection.bind('add', this.add);
		this.collection.bind('remove', this.remove);
	},
	render: function() {


		/*
		 * Initializing and setting flag from which we'll know if our view was rendered or not
		 */
		this._rendered = true;

		/*
		 * We render single user items and append it to DOM element
		 */
		_(this._users).each(function(item) {
			$users.append(item.render().el);
		});
	},
	/*
	 * Method that fires when user item is added (either from collection after fetching or creating a new one)
	 */
	add: function(user) {
		var userItem = new UserItem({model: user});
		/*
		 * Adding user item view to the list
		 */
		this._users.push(userItem);

		/*
		 * If view is rendered then we add our rendered item to this view
		 */
		if (this._rendered) {
			this.$el.append(userItem.render().el);
		}
	},
	/*
	 * Fires when removing user item
	 */
	remove: function(user) {
		/*
		 * Determining which view we need to remove from markup
		 */
		var view = _(this._users).select(function(cv) { return cv.model === user; })[0];
		if (this._rendered) {
			$(view.el).remove();
		}

		/*
		 * Triggering click to the first user item
		 */
		$users.find('li:nth-child(2)').find('a').trigger('click');
	}
});

/*
 * User dialog view, form for creating and editing users
 */
UserDialog = Backbone.View.extend({
	/*
	 * Events which we are listening and their respective selectors that triggers them
	 */
	events: {
		'click .save-action': 'save',
		'click .close,.close-action': 'close',
		'change input': 'modify'
	},
	initialize: function() {
		this.template = _.template($('#user-dialog').html());
	},
	render: function() {
		this.$el.html(this.template(this.model.toJSON()));
		return this;
	},
	/*
	 * Displaying the dialog
	 */
	show: function() {
		$(document.body).append(this.render().el);
	},
	/*
	 * Removing the dialog
	 */
	close: function() {
		this.remove();
	},
	/*
	 * Fires when we click save on the form
	 */
	save: function() {
		/*
		 * If this is new user it won't have the ID attribute defined
		 */

		var that = this;

		$.each(this.$el.find('input'), function(i, item) {
			var attribute = {};
			/*
			 * Matching name and value
			 */
			attribute[item.name] = item.value;
			that.model.set(attribute);

		});


		if (null == this.model.userId) {
			/*
			 * We are creating our model through its collection (this way we'll automatically update its views and persist it to DB)
			 */	
			var todaydate = new Date(); 
			var milliseconds = todaydate.getTime();

			this.model.set({userCreatedDate: milliseconds});
			users.create(this.model);
		} else {
			/*
			 * Simple save will persist the model to the DB and update its view
			 */
			this.model.save();

		}

		var goal = currentUserModel.get('currentPoints')+ currentUserModel.get('weeklyPoints')- currentUserModel.get('totalScore');

		$('#score').html(goal.toFixed(4));

		/*
		 * Hiding modal dialog window
		 */
		this.remove();
	},
	/*
	 * We listen to every change on forms input elements and as they have the same name as the model attribute we can easily update our model
	 */
	modify: function(e) {
		var attribute = {};
		/*
		 * We'll fetch name and value from element that triggered "change" event
		 */
		attribute[e.currentTarget.name] = e.currentTarget.value;
		this.model.set(attribute);
	}
});


/*
 * This is our Backbone model representation (as you can see attributes are the same as in the database table)
 */
Task = Backbone.Model.extend({

	/*
	 * We need to define default values which will be used in model creation (and to give Backbone some info what our model look like)
	 */
	idAttribute: 'taskId',
	defaults: {
		taskCreatedDate: Date.now(),
		taskDueDate: tomorrow.getFullYear() + '-' + ((1 + tomorrow.getMonth()) < 10 ? '0' + (1 + tomorrow.getMonth()) : '' + (1 + tomorrow.getMonth())) + '-' + (tomorrow.getDate() < 10 ? '0' + tomorrow.getDate() : '' + tomorrow.getDate()) + ' ' + (tomorrow.getHours() < 10 ? '0' + tomorrow.getHours() : '' + tomorrow.getHours()) + ':' + (tomorrow.getMinutes() < 10 ? '0' + tomorrow.getMinutes() : '' + tomorrow.getMinutes()) + ':' + (tomorrow.getSeconds() < 10 ? '0' + tomorrow.getSeconds() : '' + tomorrow.getSeconds()),
		userId: null,
		taskCreatedBy: 1,
		taskAssignedDate: Date.now(),
		taskDeleted: false,
		taskAssigned: '',
		taskCompleted: false,
		taskTitle: '',
		taskOriginalPointValue: 0,
		taskUpdatedPointValue: 0,
		taskDescription: '',
		taskMaster: true,
		taskDueDuration: 0,
		taskOverdue: false,
		taskRepetition: 'NoRepeat'
	},

	/*
	 * This is the URI where the Backbone will communicate with our server part
	 */
	urlRoot : serverUrl + 'tasks' 

});

/*
 * We also need a collection object which will hold our models (so it will be able to communicate with Backbone views more easily)
 */
TaskCollection = Backbone.Collection.extend({

	initialize: function(options) {
		this.url = serverUrl + 'tasks' + '/user/' + options.id
		this.on( "change:taskCompleted", this.triggerReset, this);

	},

	/*
	 * Model which this collection will hold and manipulate
	 */
	model: Task,

	comparator : function(model) {
		return [model.get('taskCompleted'),model.get('taskDueDate')];
	},

	triggerReset: function(){
		this.trigger('reset');
	}
});


/*
 * Single task view
 */
TaskItem = Backbone.View.extend({
	tagName: 'tr',
	initialize: function() {
		this.render = _.bind(this.render, this);
		this.template = _.template($('#task-item').html());
		this.model.bind('change', this.render);
	},
	events: {
		'dblclick': 'edit',
		'change input': 'modify',
		'click a.delete-action': 'delete'
	},
	render: function() {

		this.$el.html(this.template(this.model.attributes));
		return this;
	},
	edit: function() {
		new TaskDialog({model: this.model}).show();
	},
	/*
	 * We are listening for status checkbox, it updates the model and presist status to the DB
	 */
	modify: function(e) {
		var status = e.currentTarget.checked ? true : false;

		var taskCompleted = this.model.get('taskCompleted');

		var totalScore = currentUserModel.get('totalScore');

		var updatedPoints = null;

		if(status){
			updatedPoints = totalScore + this.model.get('taskOriginalPointValue');	
			console.log(" update points "+ updatedPoints)
		}
		else{
			updatedPoints = 
				totalScore - this.model.get('taskOriginalPointValue');
			console.log(" update points "+ updatedPoints)

		}

		currentUserModel.set({'totalScore': updatedPoints});

		currentUserModel.save();

		this.model.set({taskCompleted: status});


		var goal = currentUserModel.get('currentPoints')+ currentUserModel.get('weeklyPoints')- currentUserModel.get('totalScore');

		console.log("goal is "+goal);

		$('#score').html(goal.toFixed(4));

		this.model.save();
		/*
		 * We'll add strikethrough class to the title and date just to visually distinguish finished from unfinished task
		 */
		if (status) {
			this.$el.find('td').addClass('finished');
		} else {
			this.$el.find('td').removeClass('finished');
		}
	}
	/*
	 * Handling the deletion of item
	 */

});

/*
 * Task list/collection view
 */
TaskList = Backbone.View.extend({
	initialize: function() {
		_(this).bindAll('add','reset','filterBy');


		this._tasks = [];

		this.collection.each(this.add);

		this.collection.bind('add', this.add);
		this.collection.bind('reset', this.reset);
		this.collection.bind("filter", this.filterBy);

	},


	render: function(filterBy) {
		this._rendered = true;
		this.$el.empty();

		this.collection.fetch({});

		this.collection.each(function(task) {

			var taskDueDate = task.get('taskDueDate');
			var taskOverdue = task.get('taskOverdue');


			if (!filterBy || filterBy === "all")
			{

				var taskItem = new TaskItem({model: task});
				this.$el.append(taskItem.render().el);	
			}
			else{

				if (filterBy === "week"){
					if(!taskOverdue) {
					if (isDueDateInCurrentWeek(taskDueDate) === "week"){
						var taskItem = new TaskItem({model: task});
						this.$el.append(taskItem.render().el);	
					}
					}
				}

				else if (filterBy === "later"){
					if(!taskOverdue) {
					if (isDueDateInCurrentWeek(taskDueDate) === "later"){
						var taskItem = new TaskItem({model: task});
						this.$el.append(taskItem.render().el);	
					}
					}
				}

				else if (filterBy === "previous"){
					if (isDueDateInCurrentWeek(taskDueDate) === "past"){
						var taskItem = new TaskItem({model: task});
						this.$el.append(taskItem.render().el);	
					}
					if(taskOverdue) {
						var taskItem = new TaskItem({model: task});
						this.$el.append(taskItem.render().el);
					}
				}
			}	
		},this);


	},
	add: function(task) {
		var taskItem = new TaskItem({model: task});

		if (this._rendered) {
			this.$el.append(taskItem.render().el);
			this.render(this.getSelectedFilterOption());
		}		
		this._tasks.push(taskItem);
	},

	reset: function(){		
		this.collection.sort();
		this.render(this.getSelectedFilterOption());
	},

	filterBy: function(filterBy){
		this.render(filterBy);
	},


	getSelectedFilterOption: function(){
		var filterBy;	
		var group = document.getElementsByName('group1');
		for(var i = 0; i < group.length; i++){
			if(group[i].checked){
				filterBy = group[i].value;
			}
		}

		return filterBy;
	},


	isDueDateInCurrentWeek: function(date){
		var startDay = 1; 
		var now = new Date;
		var d = now.getDay(); 
		//  var weekStart = now.valueOf() - (d<=0 ? 7-startDay:d-startDay)*86400000; 
		var weekStart =  now.valueOf() ;
		var weekEnd =  weekStart.valueOf() + 6*86400000;
		var taskDueDate = date;

		if(taskDueDate < weekStart)
			return "past";
		else if(taskDueDate>= weekStart && taskDueDate<= weekEnd)
			return "week";
		else if(taskDueDate >= weekEnd)
			return "later";
		else if(taskDueDate <= weekStart)
			return "previous";
		else
			return "all"       

	}
});

/*
 * Modal dialog/form for creating or editing single task
 */
TaskDialog = Backbone.View.extend({
	/*
	 * As you may see we don't listen for change on input elements. We'll show a different strategy for fetching data here
	 */

	events: {
		'click .save-action': 'save',
		"click a#userli": "selectUser",
		"click button#close": "closeModal",
		'click .close,.close-action': 'close'
	},
	initialize: function() {
		this.template = _.template($('#task-dialog').html());
	},
	render: function(list_of_users) {

		this.$el.html(this.template(this.model.toJSON() , list_of_users ));
		/*
		 * We'll initialize datetime picker
		 */


		this.$el.find('#dp1').datetimepicker();
		return this;
	},

	closeModal: function(e){
		this.remove();

	},
	selectUser: function(e) {
		e.preventDefault();
		var selectedUserName = $(e.currentTarget).html();
		assignedUser = parseInt(e.currentTarget.attributes.title.value);
		this.$el.find('textarea#displayUserName').html(selectedUserName);
	},

	show: function(list_of_users) {
		$(document.body).append(this.render(list_of_users).el);
	},
	close: function() {
		this.remove();
	},
	/*
	 * Handling the save click, adding item to collection and persisting data to DB
	 */
	save: function() {
		/*
		 * We'll save a reference to current context
		 */
		var that = this;

		/*
		 * Traversing input elements in current dialog
		 */

		$.each(this.$el.find('input'), function(i, item) {
			var attribute = {};
			/*
			 * Matching name and value
			 */
			if(item.name !== "taskDueDate"){
				attribute[item.name] = item.value;
				that.model.set(attribute);
			}

			if(item.name === 'taskDueDate'){
				var date = new Date(item.value); 
				var milliseconds = date.getTime();
				that.model.set({taskDueDate: milliseconds});
			}

		});

		/*
		 * Same logic as in the user dialog, different approach for new and modified task
		 */
		if (null == this.model.taskId) {
			/*
			 * Adding user ID information read from "global" variable
			 */

			var repetition = getSelectedFilterOption();


			this.model.set({userId: assignedUser, taskAssigned: true, taskCreatedBy: currentUserId , taskRepetition: repetition});

			if (null != assignedUser)
				this.model.set({taskMaster: false});

		//	console.log(" assigned User is "+ assignedUser);

			tasks.create(this.model,{ wait: true });

			$('.assigned').fadeIn(400).delay(3000).fadeOut(400);

			$("#task-table #tasks tr:nth-child("+selectedRowIndex+") td:nth-child(1) input").trigger('click');

		} else {
			this.model.save();

			$('.assigned').fadeIn(400).delay(3000).fadeOut(400);
		}
		this.remove();
	}
});

//model for master Task


MasterTask = Backbone.Model.extend({

	/*
	 * We need to define default values which will be used in model creation (and to give Backbone some info what our model look like)
	 */
	idAttribute: 'taskId',
	defaults: {
		taskCreatedDate: Date.now(),
		taskDueDate: Date.now(),
		userId: 0,
		taskCreatedBy: 1,
		taskAssignedDate: Date.now(),
		taskDeleted: false,
		taskAssigned: '',
		taskCompleted: false,
		taskTitle: '',
		taskOriginalPointValue: 0,
		taskUpdatedPointValue: 0,
		taskDescription: '',
		taskMaster: true,
		taskDueDuration: 0,
		taskOverdue: false,
		taskRepetition: 'NoRepeat'
	},

	/*
	 * This is the URI where the Backbone will communicate with our server part
	 */
	urlRoot : serverUrl + 'tasks' + '/master'

});


//master task collection

MasterTaskCollection = Backbone.Collection.extend({

	url: serverUrl + 'tasks' + '/master', 

	initialize: function(options) {
		this.on( "change:taskCompleted", this.triggerReset, this);

	},

	/*
	 * Model which this collection will hold and manipulate
	 */
	model: MasterTask,

	comparator : function(model) {
		return [model.get('taskCompleted'),model.get('taskDueDate')];
	},

	triggerReset: function(){
		this.trigger('reset');
	}
});

/*
 * Single Master task view
 */
MasterTaskItem = Backbone.View.extend({
	tagName: 'tr',
	initialize: function() {
		this.render = _.bind(this.render, this);
		this.template = _.template($('#master-task-item').html());
		this.model.bind('change', this.render);
	},
	events: {
		'dblclick': 'assignTask',
		'change input': 'modify',
		'click .icon-edit':'assignTask',
		'click a.delete-action': 'delete'
	},
	render: function() {

		this.$el.html(this.template(this.model.attributes));
		return this;
	},
	edit: function() {
		new TaskDialog({model: this.model}).show();
	},
	/*
	 * We are listening for status checkbox, it updates the model and presist status to the DB
	 */

	assignTask: function(e){
		console.log('clicked edit');

		selectedRowIndex = this.el.rowIndex;
		console.log(' row index is '+ selectedRowIndex);
		list_of_users=new Array();

		var dummyModel = new Task(this.model.attributes);

		var currentTime = new Date();
		var numberOfDaysToAdd = this.model.get("taskDueDuration");
		currentTime.setDate(currentTime.getDate() + numberOfDaysToAdd);
		var day = currentTime.getDate();
		var month = currentTime.getMonth() + 1;
		var year = currentTime.getFullYear();
		var hour = currentTime.getHours();
		var mins = currentTime.getMinutes();

		var date =  year + "-" + month + "-"+ (day < 10 ? '0' : '') + day  + " " + hour + ":" + (mins < 10 ? '0' : '') + mins + ':00';

		dummyModel.set({"taskDueDate" : date.toString()});

		dummyModel.set({'taskId': null});

		var view = new TaskDialog({model: dummyModel});

		$.ajax({
			url: serverUrl + 'users',
			success:function(result){

				result.forEach(function(entry) {
					var eachUser = {};
					eachUser['userId'] = entry.userId;
					eachUser['name'] = entry.userFirstName+" "+entry.userLastName;
					list_of_users.push(eachUser);             	

				});

				view.show(list_of_users);

			}
		});
	},

	modify: function(e) {
		var status = e.currentTarget.checked ? true : false;

		this.model.set({taskCompleted: status});

		this.model.save();
		/*
		 * We'll add strikethrough class to the title and date just to visually distinguish finished from unfinished task
		 */
		if (status) {
			this.$el.find('td').addClass('finished');
		} else {
			this.$el.find('td').removeClass('finished');
		}
	}
	/*
	 * Handling the deletion of item
	 */

});

MasterTaskList = Backbone.View.extend({
	initialize: function() {

		_(this).bindAll('add','reset');

		this._tasks = [];

		this.collection.each(this.add);

		this.collection.bind('add', this.add);
		this.collection.bind('reset', this.reset);

	},


	render: function() {
		this._rendered = true;
		this.$el.empty();

		this.collection.fetch();

		this.collection.each(function(masterTask) {

			var masterTaskItem = new MasterTaskItem({model: masterTask});
			this.$el.append(masterTaskItem.render().el);	
		},this);


	},


	add: function(masterTask) {
		var masterTaskItem = new MasterTaskItem({model: masterTask});

		if (this._rendered) {
			this.$el.append(masterTaskItem.render().el);
			this.render();
		}		
		this._tasks.push(masterTaskItem);

	},

	reset: function(){		
		this.collection.sort();
		this.render();
	},
});

MasterTaskDialog = Backbone.View.extend({
	/*
	 * As you may see we don't listen for change on input elements. We'll show a different strategy for fetching data here
	 */

	events: {
		'click .save-action': 'save',
		"click a#userli": "selectUser",
		"click button#close": "closeModal",
		'click .close,.close-action': 'close'
	},
	initialize: function() {
		this.template = _.template($('#master-task-dialog').html());
	},
	render: function(list_of_users) {

		this.$el.html(this.template(this.model.toJSON() , list_of_users ));
		/*
		 * We'll initialize datetime picker
		 */

		this.$el.find('#dp1').datetimepicker();
		return this;
	},

	closeModal: function(e){
		this.remove();

	},
	selectUser: function(e) {
		e.preventDefault();
		var selectedUserName = $(e.currentTarget).html();
		assignedUser = parseInt(e.currentTarget.attributes.title.value);
		this.$el.find('textarea#displayUserName').html(selectedUserName);
	},

	show: function(list_of_users) {
		$(document.body).append(this.render(list_of_users).el);
	},
	close: function() {
		this.remove();
	},
	/*
	 * Handling the save click, adding item to collection and persisting data to DB
	 */
	save: function() {
		/*
		 * We'll save a reference to current context
		 */
		var that = this;

		/*
		 * Traversing input elements in current dialog
		 */

		$.each(this.$el.find('input'), function(i, item) {
			var attribute = {};
			/*
			 * Matching name and value
			 */
			if(item.name !== "taskDueDate"){
				attribute[item.name] = item.value;
				that.model.set(attribute);
			}

			if(item.name === 'taskDueDate'){
				var date = new Date(item.value); 
				var milliseconds = date.getTime();
				that.model.set({taskDueDate: milliseconds});
			}

		});

		/*
		 * Same logic as in the user dialog, different approach for new and modified task
		 */
		if (null == this.model.taskId) {
			/*
			 * Adding user ID information read from "global" variable
			 */

			var repetition = getSelectedFilterOption();


			this.model.set({userId: assignedUser, taskAssigned: true, taskCreatedBy: currentUserId});

			if (null != assignedUser)
				this.model.set({taskMaster: false});

			console.log(" assigned User is "+ assignedUser);

			masterTasks.create(this.model,{ wait: true });
			
			masterTaskListView.render();


			$('.success').fadeIn(400).delay(3000).fadeOut(400);
		} else {
			this.model.save();

			$('.success').fadeIn(400).delay(3000).fadeOut(400);
		}
		this.remove();
	}
});

var AppRouter = Backbone.Router.extend({

	routes: {
		"master": "masterTasks",
		"user": "user",
		"*actions": "defaultRoute" // Backbone will try match the route above first

	}
});

base_url = window.location.href;

//Instantiate the router
location.hash = '';
var app_router = new AppRouter;
Backbone.history.start();


app_router.on('route:masterTasks', function () {
	// Note the variable in the route definition being passed in here
	console.log( " logged master ");   

	$('#div1').show();
	$('#taskFilterList').hide();

	$tasks.empty();


	masterTasks = new MasterTaskCollection();

	masterTasks.comparator = function(model) {
		return [model.get('taskCompleted'),model.get('taskDueDate')];
	}


	/*
	 * We'll assign current user ID to "global" variable as we need it on several other places
	 */

	/*
	 * Lets fetch tasks for currently selected user (we can access currently selected user through this.model)
	 * processData param here only informs the system that params provided through data param needs to be added to URL as GET params
	 */
	masterTasks.fetch({processData: true, success: function() {
		/*
		 * Initializing task list view and passing tasks collection to it
		 */
		masterTaskListView = new MasterTaskList({
			collection: masterTasks,
			/*
			 * Telling view to which DOM elements it needs to attach itself
			 */
			el: $tasks
		});
		/*
		 * Rendering list view
		 */
		masterTaskListView.render();
		//tasks.sort();

	}});
});


app_router.on('route:user', function () {
	// Note the variable in the route definition being passed in here
	initilializeMyTasks();

	//$tasks.empty();

	$users.empty();

	$users.append(" <li class='nav-header'><h6 class='friends'>Friends</h6></li>");

	users = new UserCollection();
	/*
	 * Fetching users from DB
	 */
	users.fetch({success: function(data) {
		var currentUser = parseUrlParam(location.search);
		var dataModel = data.models;
		var userNumber = 2;
		for(var i = 0; i < dataModel.length; i++){
			var attributes = JSON.stringify(dataModel[i].attributes);

			var currId =  $.parseJSON(attributes).userId;
			if(currId == currentUser){
				userNumber = currId + 1;
				break;
			}else { continue; } 
		}
		userListView = new UserList({
			collection: users,
			el: $users
		});
		userListView.render();

		/*
		 * Triggering click on the currently logged in user which will load its tasks
		 */

		$users.find('li:nth-child(' + userNumber + ')').find('a').trigger('click');

	}});
});

app_router.on('route:defaultRoute', function (path) {		
	if(window.location.href === base_url && path == null){
		initilializeMyTasks();
	}			
});


function initilializeMyTasks(){
//	console.log( " my Tasks " + base_url);   
	$('#div1').hide();
	$('#taskFilterList').show();

}

users = new UserCollection();
/*
 * Fetching users from DB
 */
users.fetch({success: function(data) {
	var currentUser = parseUrlParam(location.search);
	var dataModel = data.models;
	var userNumber = 2;
	for(var i = 0; i < dataModel.length; i++){
		var attributes = JSON.stringify(dataModel[i].attributes);

		var currId =  $.parseJSON(attributes).userId;
		if(currId == currentUser){
			userNumber = currId + 1;
			break;
		}else { continue; } 
	}
	userListView = new UserList({
		collection: users,
		el: $users
	});
	userListView.render();

	/*
	 * Triggering click on the currently logged in user which will load its tasks
	 */
	$users.find('li:nth-child(' + userNumber + ')').find('a').trigger('click');
}});


/*
 * Attaching to "Add User" button
 */
$('#add-user').click(function(e) {
//	var view = new UserDialog({model: new User()});
//	view.show();
//	return false;
});

/*
 * Attaching to "Delete User" button
 */
$('#delete-user').click(function(e) {
	users.get(currentUserId).destroy();
	return false;	
});

/*
 * Attaching to "Add Task" button
 */
$('#add-task').click(function(e) {
	list_of_users=new Array();
	var view = new MasterTaskDialog({model: new MasterTask()});

	$.ajax({
		url: serverUrl + 'users',
		success:function(result){

			result.forEach(function(entry) {
				var eachUser = {};
				eachUser['userId'] = entry.userId;
				eachUser['name'] = entry.userFirstName+" "+entry.userLastName;
				list_of_users.push(eachUser);             	

			});

			view.show(list_of_users);

		}
	});

	return false;

});

$("#previous, #this_week, #later, #all_tasks").change(function () {

	var filterBy;	
	var group = document.getElementsByName('group1');
	for(var i = 0; i < group.length; i++){
		if(group[i].checked){
			filterBy = group[i].value;
		}
	}
	tasks.trigger('filter',filterBy);

});


function getSelectedFilterOption(){
	var filterBy;	
	var group = document.getElementsByName('taskRepetition');
	for(var i = 0; i < group.length; i++){
		if(group[i].checked){
			filterBy = group[i].value;
		}
	}

	return filterBy;
}

function isDueDateInCurrentWeek(date){

	// assumed start day of the current week is monday
	var now = new Date;
	var d = now.getDay(); 
	var startDay = 1; 
	//    var weekStart = now.valueOf() - (d<=0 ? 7-startDay:d-startDay)*86400000; 
	var weekStart = now.valueOf();
	var weekEnd =  weekStart.valueOf() + 6*86400000;
	var taskDueDate = date;


	if(taskDueDate>= weekStart && taskDueDate<= weekEnd)
		return "week";
	else if(taskDueDate >= weekEnd)
		return "later";
	else if(taskDueDate < weekStart)
		return "past";
	else
		return "all"       

}

function isNumberKey(evt){
	var charCode = (evt.which) ? evt.which : event.keyCode
			if (charCode > 31 && (charCode < 48 || charCode > 57))
				return false;
	return true;
}

$('#div1').hide();

$("#myTab a").click(function(e){
	e.preventDefault();
	$(this).tab('show');
	var id  = this.id;

	if(id === "user")
		myTask();
	else if (id === "masterTasks")
		masterTask();
});


function masterTask(){

	$.ajax({
		url: serverUrl + 'tasks/master',
		success:function(result){
		}
	});
}

function activeMasterTasks(){
	$('#div1').show();
	$('#taskFilterList').hide();
	$('#master-title').addClass("active")
	$('#my-tasks-title').removeClass("active")
}

function activeMyTasks(){
	$('#div1').hide();
	$('#taskFilterList').show();
	$('#my-tasks-title').addClass("active")
	$('#master-title').removeClass("active")
}

function advanceWeek() {
	$.post('http://localhost:8080/TaskBuddy/site/advance');
	location.reload();
}

function resetWeek(){
	$.post('http://localhost:8080/TaskBuddy/site/reset');
	location.reload();
}
