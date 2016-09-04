// ----------------CHARACTER CONSTRUCTOR-------------------
var Cbay = function(game) {

	var auctions = [];

	this.updateAuctions = function () {
		var link = "http://hp.trm.io/hphelper/api/public/" + game.getGameId() + "/cbay/";
		$.getJSON(link, function(data) {
			if (data.status === "ok") {
				for (var prop in data) {
					auctions = data.cbay;
				}
				displayAuctions();
			}
		});
	};

	var displayAuctions = function () {
		var list = document.getElementById("cbayAuctions");
		for (var i = 0; i < auctions.length; i++) {
			var listItem = document.createElement("LI");
			listItem.innerHTML = auctions[i];
			list.appendChild(listItem);
		}
	};
};