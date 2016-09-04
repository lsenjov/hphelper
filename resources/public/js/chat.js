// ---------------CHAT CONSTRUCTOR-----------------
var Chat = function() {

	var outerDiv = document.getElementById("chatMessages");
	var chatLog = [];

	this.init = function() {
		this.addMessage("FC", new Date().getTime(), "Welcome to Paranoia!");
	};

	this.getMessages = function() {
		// TODO: check server for messages
	};

	this.addMessage = function(player, time, message) {
		chatLog.push({
			player: player,
			time: new Date(time).getHours() + ":" + new Date(time).getMinutes(),
			message: message
		});
		updateChatDisplay();
	};

	var updateChatDisplay = function() {
		var newMsg = document.createElement("P");
		newMsg.className = "chatMsg";
		var item = chatLog[chatLog.length-1];
		newMsg.innerHTML = item.time + " " + item.player + ": " + item.message;
		outerDiv.appendChild(newMsg);
	};

};
