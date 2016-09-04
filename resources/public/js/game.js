// ---------------GAME CONSTRUCTOR-----------------
var Game = function(gameId, playerId, startingPoolAccess) {

	var poolAccess = 0;
	var indicies;
	var player;
	var cbay;
	var lastUpdated;

	this.init = function() {
		lastUpdated = new Date().getTime();

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

	var getGameUpdates = function() {
		var link = "http://hp.trm.io/hphelper/api/public/" + gameId + "/updates/" + lastUpdated;
		$.getJSON(link, function (data) {
			if (data.status === "okay" && objSize(data) > 2) {

			}

		});
	};

	var objSize = function(obj) {
		var count = 0;
		for (var prop in obj) {
			if (obj.hasOwnProperty(prop)) {
				count++;
			}
		}
		return count;
	};


};