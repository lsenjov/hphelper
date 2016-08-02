// ---------------GAME CONSTRUCTOR-----------------
var Game = function(indices, poolAccess, cbay, character) {
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
	this.indices = indices;
	this.poolAccess = poolAccess;
	this.poolAccessHistory = "";
	this.cbay = cbay;
	this.calls = [];
	this.chatLog = "";
};


// ---------------INDICES METHODS-----------------
// Note that Indices obj is in indices.js
Game.prototype.updateIndices = function() {
	// Make this update the indices via the functions below
};


// ---------------POOL ACCESS METHODS-----------------
Game.prototype.updatePoolAccess = function(newAccess) {
	this.poolAccessHistory += "<li>" + this.poolAccess + "</li>";
	this.poolAccess = newAccess;
	// Update displayed data
	this.displayPoolAccess();
}

Game.prototype.displayPoolAccess = function() {
	document.getElementById("poolAccessData").innerHTML = "Access Pool: " + this.poolAccess;
	//document.getElementById("poolAccessHistory").innerHTML = this.poolAccessHistory;
};