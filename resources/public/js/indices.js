var Indices = function() {
	this.CI = 0;
	this.HI = 0;
	this.LI = 0;
	this.SI = 0;
	this.AF = 0;
	this.CP = 0;
	this.HP = 0;
	this.IS = 0;
	this.PL = 0;
	this.PS = 0;
	this.RD = 0;
	this.TD = 0;
	this.TS = 0;
};



Indices.prototype.updateIndices = function() {
	// probably add current values to history (stringify obj and store in string array?) and update new ones?
};

Indices.prototype.toString = function() {
	// Iterates through object properties and prints "prop val | " for each
	// May want to format this better - sections, ul? 
	var data = "";
	for (var x in this) {
		if (this.hasOwnProperty(x)) {
			data = data + x + " " + this[x] + " | ";
		}
	}
	data = data.slice(0, data.length-2);
	return data;
};

Indices.prototype.displayIndices = function() {
	var indices = document.getElementById("indicesData");
	indices.innerHTML = this.toString();
}