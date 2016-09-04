var Indicies = function(gameId) {
	var indicies = {};

	this.init = function () {
	    this.getIndicies();
    };

	this.getIndicies = function () {
		var link = "http://hp.trm.io/hphelper/api/public/" + gameId + "/indicies/";
        //var link = "C:/Users/BlackCat/Documents/hphelper/resources/public/text/indicies.txt";
		$.getJSON(link, function(data) {
		    if(data.status === "ok") {
                var newIndicies = data.indicies;
                for (var ind in newIndicies) {
                    if (newIndicies.hasOwnProperty(ind)) {
                        indicies[ind] = newIndicies[ind];
                    }
                }
                displayIndices();
            }
		});
	};

	var toString = function () {
		// Iterates through object properties and prints "prop val | " for each
		// May want to format this better - sections, ul?
		var data = "";
		for (var ind in indicies) {
			if (indicies.hasOwnProperty(ind)) {
				data = data + ind + " " + indicies[ind] + " | ";
			}
		}
		data = data.slice(0, data.length - 2);
        return data;
	};

	var displayIndices = function () {
        var outerDiv = document.getElementById("indiciesData");
        outerDiv.innerHTML = "";
        var itemsInList = Math.ceil(objSize(indicies) / 3);

        var list = document.createElement("UL");
        list.className = "splitList";
        var count = 0;
        for (var prop in indicies) {
            if (indicies.hasOwnProperty(prop)) {
                if (count !== 0 && (count%itemsInList) === 0) {
                    outerDiv.appendChild(list);
                    list = document.createElement("UL");
                    list.className = "splitList";
                }
                var listItem = document.createElement("LI");
                listItem.innerHTML = prop + ": " + indicies[prop];
                list.appendChild(listItem);
                count++;
            }
        }
        outerDiv.appendChild(list);
   };

    var objSize = function(obj) {
        var count = 0;
        for (var prop in obj) {
            if (obj.hasOwnProperty(prop)) {
                count++;
            }
        }
        return count;
    };

};