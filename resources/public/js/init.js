window.onload = function() {
	// --------INIT ACCORDIONS----------
	var ids = ["indiciesData", "characterData", "accessData", "serviceGroupsData", "callData", "cbayData", "missionsData", "minionsData"];
	for (var i = 0; i < ids.length; i++) {
		var para = document.getElementById(ids[i]);
		para.style.visibility = "hidden";
		para.style.display = "none";
	}
	
	// ----------DATA----------
	
	// Set up indices

	var gameId = "6dc8ad54-f002-4bc3-9b6a-a1e8ce03ca1d";
	var playerId = "d3ea1c2f-b8d5-4dd7-965a-6e885ad4456b";
	var startingPoolAccess = 50;
	var game = new Game(gameId, playerId, startingPoolAccess);
	game.init();
	var gameUpdate = setInterval(game.update(), 100);

	
	// Set up character data
	// Create test data for now
	// var testName = "Test-U-ARE";
	// var testMainStats = [6, 8, 19, 12, 5, 13];
	// // var testSecStats = new SecondaryStats(3, 4, "Combat Mind (gain intuition into exactly how to maneuver forces to win a battle.)", 10, 0);
	// var testSocieties = ["The Movement", "Command +4, Total War, Habitat Eng", "Anti-Mutant", "Investigation, Wetwork +4, Biosciences", "FCCC-P", "Interrogation, Thought Control +4, Covert Operations", "Servants of Cthulhu", "Biosciences, Mutant Studies, Intimidation +4"];
	// var testBids = {"AF": 0, "CPU": 0, "HPD": 0, "IS": 5, "PLC": 0, "RD": 0, "TS": 6};
	// var testDrawbacks = ["Cyborged: You've got bot bits, they're obvious, and you are affected by attacks that affect bots."];
	// var testAccess = 30;
	//
	// var testCharacter = new Player(gameId, playerId);
	// testCharacter.displayName();
	// testCharacter.displayCharacterAccess();
	// testGame.displayPoolAccess();
	//
	// testCharacter.displayData();
	//
	// testGame.updatePoolAccess(45);
	
}
