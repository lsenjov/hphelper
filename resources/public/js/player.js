// ----------------CHARACTER CONSTRUCTOR-------------------
var Player = function(game, playerId) {

    var player = {
        name: "",
        priStats: {},
        secStats: {},
        programGroup: [],
        mutation: {},
        drawbacks: "",
        serviceGroups: {},
        bids: {},
        accessRemaining: 0,
        accessHistory: ""
    };

    this.init = function () {
        getCharsheet();
    };

    var getCharsheet = function () {
        var link = "http://hp.trm.io/hphelper/api/player/" + game.getGameId() + "/" + playerId + "/charsheet/";
        $.getJSON(link, function (data) {
            for (var prop in data) {
                if (data.hasOwnProperty(prop) && player.hasOwnProperty(prop)) {
                    player[prop] = data[prop];
                }
            }
            displayPlayer();
        });
    };

    var displayPlayer = function () {
        displayName();
        displayPriStats();
        displaySecStats();
        displayMutation();
        displayAccess();
    };

    // Display data - name in character heading, access in access heading, rest in character data
    var displayName = function () {
        document.getElementById("characterName").innerHTML = "Character: " + player.name;
    };

    var displayPriStats = function () {
        var outerDiv = document.getElementById("priStatsDiv");
        var itemsInList = Math.ceil(objSize(player.priStats) / 2);

        var list = document.createElement("UL");
        list.className = "splitListLeft";
        var count = 0;
        for (var prop in player.priStats) {
            if (player.priStats.hasOwnProperty(prop)) {
                if (count !== 0 && (count % itemsInList) === 0) {
                    outerDiv.appendChild(list);
                    list = document.createElement("UL");
                    list.className = "splitListRight";
                }
                var listItem = document.createElement("LI");
                listItem.innerHTML = prop + ": " + player.priStats[prop];
                list.appendChild(listItem);
                count++;
            }
        }
        outerDiv.appendChild(list);
    };

    var displaySecStats = function () {
        var list = document.getElementById("secStats");
        for (var prop in player.secStats) {
            if (player.secStats.hasOwnProperty(prop)) {
                var listItem = document.createElement("LI");
                listItem.innerHTML = prop + ": " + player.secStats[prop];
                list.appendChild(listItem);
            }
        }
    };

    var displayMutation = function () {
        var para = document.getElementById("mutation");
        para.innerHTML = "<li>" + player.mutation.description + "</li><li>Power: " + player.mutation.power + "</li>";
    };

    var displayAccess = function () {
        document.getElementById("characterAccessData").innerHTML = player.accessRemaining;
    };

    var objSize = function (obj) {
        var count = 0;
        for (var prop in obj) {
            if (obj.hasOwnProperty(prop)) {
                count++;
            }
        }
        return count;
    };

    /* Access methods */
    this.setAccess = function (newAccess) {
        this.access = newAccess;
    };

    this.changeAccess = function (accessChange) {
        this.accessHistory += "<li>" + this.access + "</li>";
        this.access += accessChange;
    };

};