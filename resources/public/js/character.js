// ----------------CHARACTER CONSTRUCTOR-------------------
var Character = function(name, mainStats, sndStats, societies, drawbacks, serviceGroups, access) {
	/* Inputs:
		- name = string, immutable
		- mainStats = int array, immutable, order alphabetically: H, M, SO, SU, V, W
		- sndStats = obj, immutable except for standing
		- societies = string array, immutable
		- drawbacks = string, immutable
		- bids = int array, mutable, order alphabetically: AF, CPU, HPD, IS, PLC, RD, TD, TS
		- access = int, mutable
	*/
	this.name = name;
	this.mainStats = mainStats;
	this.sndStats = sndStats;
	this.societies = societies;
	this.drawbacks = drawbacks;
	this.serviceGroups = serviceGroups;
	this.access = access;
	this.accessHistory = "";
}

// ----------------NAME METHODS-------------------

// ----------------ACCESS METHODS-------------------

// ----------------MAINSTATS METHODS-------------------

// ----------------SNDSTATS METHODS-------------------

// ----------------SOCIETIES METHODS-------------------

// ----------------DRAWBACKS METHODS-------------------

// ----------------BIDS METHODS-------------------




Character.prototype.setAccess = function(newAccess) {
	this.access = newAccess;
};

Character.prototype.changeAccess = function(accessChange) {
	this.accessHistory += "<li>" + this.access + "</li>";
	this.access += accessChange;
};

// Display data - name in character heading, access in access heading, rest in character data
Character.prototype.displayName = function() {
	document.getElementById("characterName").innerHTML = "Character: " + this.name;
};

Character.prototype.displayData = function() {
	// Set main stats
	this.displayMainStats();
	this.displaySndStats();
	this.displaySocieties();
	this.displayDrawbacks();
	this.displayBids();
};

Character.prototype.displayMainStats = function() {
	var mainStatsCode1 = "<li>Hardware: " + this.mainStats[0] + "</li>" + 
			"<li>Management: " + this.mainStats[1] + "</li>" + 
			"<li>Software: " + this.mainStats[2] + "</li>";
	var mainStatsCode2 = "<li>Subterfuge: " + this.mainStats[3] + "</li>" + 
			"<li>Violence: " + this.mainStats[4] + "</li>" + 
			"<li>Wetware: " + this.mainStats[5] + "</li>";
	document.getElementById("mainStatsList1").innerHTML = mainStatsCode1;
	document.getElementById("mainStatsList2").innerHTML = mainStatsCode2;
};
	
Character.prototype.displaySndStats = function() {
	// Set secondary stats
	// NOTE: CHANGE TO UL
	var sndStatsCode = "<li>Clone Degradation: " + this.sndStats.degrad + "</li>" +
			"<li>Program Group Size: " + this.sndStats.groupSize + "</li>" +
			"<li>Mutation: " + this.sndStats.mutation + "</li>" +
			"<li>Mutation Strength: " + this.sndStats.mutationStrength + "</li/>" +
			"<li>Public Standing: " + this.sndStats.standing + "</li/>";
	document.getElementById("sndStats").innerHTML = sndStatsCode;
};

Character.prototype.displaySocieties = function() {
	// this.societies is currently an array of names and skills - change to obj?
	var societiesCode = "";
	for (var i = 0; i < this.societies.length; i += 2) {
		societiesCode += "<li>" + this.societies[i] + ": " + this.societies[i+1] + "</li>";
	}
	document.getElementById("societies").innerHTML = societiesCode;
};

Character.prototype.displayDrawbacks = function() {
	var drawbacksCode = "";
	for (var i = 0; i < this.drawbacks.length; i ++) {
		drawbacksCode += "<li>" + this.drawbacks[i] + "</li>";
	}
	document.getElementById("drawbacks").innerHTML = drawbacksCode;
};

Character.prototype.displayBids = function() {
	var bidsCode1 = "";
	var bidsCode2 = "";
	var count = 0;
	for (var x in this.bids) {
		if (this.bids.hasOwnProperty(x)) {
			if (count % 2 == 0) {
				bidsCode1 += "<li>" + x + ": " + this.bids[x] + "</li>";
			} else {
				bidsCode2 += "<li>" + x + ": " + this.bids[x] + "</li>";
			}
			count++;
		}
	}
	document.getElementById("bidsList1").innerHTML = bidsCode1;
	document.getElementById("bidsList2").innerHTML = bidsCode2;
}

Character.prototype.displayCharacterAccess = function() {
	document.getElementById("characterAccessData").innerHTML = "Your Access: " + this.access;
	//document.getElementById("characterAccessHistory").innerHTML = this.accessHistory;
};

// Secondary Stats obj
var SecondaryStats = function(degradation, progGroupSize, mutation, mutationStrength, standing) {
	this.degrad = degradation; // int, immutable
	this.groupSize = progGroupSize; // int, immutable
	this.mutation = mutation; // str, immutable
	this.mutationStrength = mutationStrength; // int, immutable
	this.standing = standing; // int, mutable
};

SecondaryStats.prototype.setStanding = function(newStanding) {
	this.standing = newStanding;
};

SecondaryStats.prototype.changeStanding = function(standingChange) {
	this.standing = standingChange;
};