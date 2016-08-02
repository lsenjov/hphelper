// ----------------CHARACTER CONSTRUCTOR-------------------
var Cbay = function(auctions) {
	this.auctions = auctions;
};

Cbay.prototype.updateAuctions = function(newAuctions) {
	this.auctions = newAuctions;
};

Cbay.prototype.displayAuctions = function() {
	var auctionsCode = "";
	for (var i = 0; i < this.auctions.length; i++) {
		auctionsCode += "<li>" + this.auctions[i] + "</li>";
	}
	document.getElementById("cbayAuctions").innerHTML = auctionsCode;
};