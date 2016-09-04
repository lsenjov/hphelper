// ----------------CHARACTER CONSTRUCTOR-------------------
var Player = function(game, playerId) {

    var player = {
        name: "",
        priStats: {},
        secStats: {},
        programGroup: [],
        mutation: {},
        drawbacks: "",
        serviceGroups: [],
        bids: {},
        accessRemaining: 0,
        accessHistory: ""
    };

    this.init = function () {
        getCharsheet();
        // TODO: make this update properly
        getServiceGroups();
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

    var getServiceGroups = function() {
        var link = "http://hp.trm.io/hphelper/api/player/" + game.getGameId() + "/" + playerId + "/minions/";
        $.getJSON(link, function (data) {
            if (data.status === "okay") {
                var sgs = data.serviceGroups;
                for (var sgNum = 0; sgNum < sgs.length; sgNum++) {
                    var sg = sgs[sgNum];
                    if (sg.owner === player.name) {
                        player.serviceGroups.push(sgs[sgNum]);
                    }
                }
            }
            displayServiceGroups();
        });
    };

    var displayPlayer = function () {
        displayName();
        displayPriStats();
        displaySecStats();
        displayProgramGroups();
        displayMutation();
        displayAccess();
        displayDrawbacks();
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

    var displayProgramGroups = function() {
        var list = document.getElementById("programGroups");
        for (var prop in player.programGroup) {
            if (player.programGroup.hasOwnProperty(prop)) {
                var listItem = document.createElement("LI");
                listItem.innerHTML = player.programGroup[prop].ss_name + ": " + player.programGroup[prop].sskills;
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

    var displayDrawbacks = function() {
        if (player.drawbacks === "") {
            var heading = document.getElementById("drawbacksHeading");
            heading.style.display = "none";
            heading.style.visibility = "hidden";
            var child = document.getElementById("drawbacks");
            child.style.display = "none";
            child.style.visibility = "hidden";
        }
    };

    var displayServiceGroups = function() {
        var outerDiv = document.getElementById("serviceGroupsData");
        for (var sg in player.serviceGroups) {
            if (player.serviceGroups.hasOwnProperty(sg)) {
                var sgName = document.createElement("H3");
                sgName.innerHTML = player.serviceGroups[sg].sg_name;
                outerDiv.appendChild(sgName);
                var minionList = document.createElement("UL");
                var sgMinions = player.serviceGroups[sg].minions;
                for (var minion in sgMinions) {
                    if (sgMinions.hasOwnProperty(minion)) {
                        var minionText = "<strong>" + sgMinions[minion].minion_name + "</strong> " + "("
                            + sgMinions[minion].minion_clearance + " " + sgMinions[minion].minion_cost + "): "
                            + sgMinions[minion].mskills;
                        var listItem = document.createElement("LI");
                        listItem.innerHTML = minionText;
                        minionList.appendChild(listItem);
                    }
                }
                outerDiv.appendChild(minionList);
            }
        }
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

    this.getName = function() {
        return player.name;
    }

};