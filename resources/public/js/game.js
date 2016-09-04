// ---------------GAME CONSTRUCTOR-----------------
var Game = function(gameId, playerId, startingPoolAccess) {
	/* Constructor for game object 
	 * Inputs:
	 * 	- indices = Indices obj, mutable
	 * 	- poolAccess = int, mutable
	 * 	- cbay = string array, mutable?
	 * 	- character = Character obj
	 * Other vars:
	 * 	- poolAccessHistory = string, mutable
	 * 	- calls = string array?, mutable
	 * 	- chatLog = Chat obj, mutable
	 */

	var poolAccess = 0;
	var indicies;
	var player;
	var cbay;

	this.getGameId = function() {
		return gameId;
	};

	this.init = function() {
		// Init access
		poolAccess = startingPoolAccess;
		displayPoolAccess();

		// Init indicies
		indicies = new Indicies(gameId);
		indicies.init();

		// Init player
		player = new Player(this, playerId);
		player.init();

		// Init Cbay
		cbay = new Cbay(this);
		cbay.updateAuctions();
	}

	var displayPoolAccess = function () {
		document.getElementById("poolAccessData").innerHTML = poolAccess;
		//document.getElementById("poolAccessHistory").innerHTML = this.poolAccessHistory;
	};



	// Init service groups

	// this.poolAccess = poolAccess;
	// this.poolAccessHistory = "";
	// this.cbay = cbay;
	// this.calls = [];
	// this.chatLog = "";

	this.update = function() {
		console.log("update");
		indicies.updateIndicies();
	};

	/* --- POOL ACCESS METHODS --- */
	// this.updatePoolAccess = function (newAccess) {
	// 	this.poolAccessHistory += "<li>" + this.poolAccess + "</li>";
	// 	this.poolAccess = newAccess;
	// 	// Update displayed data
	// 	this.displayPoolAccess();
	// };


};