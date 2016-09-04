var News = function() {

    var news = [];
    var outerDiv = document.getElementById("newsfeed");
    var lastItem = null;

    this.init = function() {
        news.push("Welcome to Paranoia! Remember: fun is mandatory.");
        var newsItem = document.createElement("P");
        newsItem.innerHTML = news[0];
        outerDiv.appendChild(newsItem);
        lastItem = newsItem;
    };

    this.addNews = function(message) {
        news.push(message);
        updateNewsDisplay()
    };

    var updateNewsDisplay = function() {
        var newItem = document.createElement("P");
        newItem.className = "newsItem";
        newItem.innerHTML = news[news.length-1];
        outerDiv.insertBefore(newItem, lastItem);
        lastItem = newItem;
    };

};