function accordion(id) {
	var child = document.getElementById(id);
	var heading = document.getElementById(id.slice(0, -4));
	
	if (child.style.visibility == "hidden") {
		child.style.visibility = "visible";
		child.style.display = "inline-block";
		heading.className += " activeHeading";
	} else {
		child.style.visibility = "hidden";
		child.style.display = "none";
		heading.className = "dataHeading accordion";
	}
}