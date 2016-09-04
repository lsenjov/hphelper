// ---------------GAME CONSTRUCTOR-----------------
var Game = function(gameId, playerId, startingPoolAccess) {

	var poolAccess = 0;
	var indicies;
	var player;
	var cbay;

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
		cbay.getAuctions();
	};

	var displayPoolAccess = function () {
		document.getElementById("poolAccessData").innerHTML = poolAccess;
		//document.getElementById("poolAccessHistory").innerHTML = this.poolAccessHistory;
	};

	this.update = function() {
		console.log("update");
		indicies.getIndicies();
	};

	this.getGameId = function() {
		return gameId;
	};

};