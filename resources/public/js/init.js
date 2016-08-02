window.onload = function() {
	// --------INIT ACCORDIONS----------
	var ids = ["indicesData", "characterData", "accessData", "serviceGroupsData", "callData", "cbayData", "missionsData", "minionsData"];
	for (var i = 0; i < ids.length; i++) {
		var para = document.getElementById(ids[i]);
		para.style.visibility = "hidden";
		para.style.display = "none";
	}
	
	// ----------DATA----------
	
	// Set up indices
	var indices = new Indices();
	indices.displayIndices();
	var testPoolAccess = 50;
	var testGame = new Game(indices, testPoolAccess);
	
	// Set up Cbay auctions
	var testAuctions = ["5 Ton Hot Fun! Barely used! 3 ACCESS 0h31m", "3 Ton Nuclear Waste, going fast! 1 ACCESS 1h10m", "80 Tons water recently recycled! 4 ACCESS 1h34m"];
	var testCbay = new Cbay(testAuctions);
	testCbay.displayAuctions();
	
	
	
	// Set up character data
	// Create test data for now
	var testName = "Test-U-ARE";
	var testMainStats = [6, 8, 19, 12, 5, 13];
	var testSecStats = new SecondaryStats(3, 4, "Combat Mind (gain intuition into exactly how to maneuver forces to win a battle.)", 10, 0);
	var testSocieties = ["The Movement", "Command +4, Total War, Habitat Eng", "Anti-Mutant", "Investigation, Wetwork +4, Biosciences", "FCCC-P", "Interrogation, Thought Control +4, Covert Operations", "Servants of Cthulhu", "Biosciences, Mutant Studies, Intimidation +4"];
	var testBids = {"AF": 0, "CPU": 0, "HPD": 0, "IS": 5, "PLC": 0, "RD": 0, "TS": 6};
	var testDrawbacks = ["Cyborged: You've got bot bits, they're obvious, and you are affected by attacks that affect bots."];
	var testAccess = 30;
	
	var testCharacter = new Character(testName, testMainStats, testSecStats, testSocieties, testDrawbacks, testBids, testAccess);
	testCharacter.displayName();
	testCharacter.displayCharacterAccess();
	testGame.displayPoolAccess();
	
	testCharacter.displayData();
	
	testGame.updatePoolAccess(45);
	
}
