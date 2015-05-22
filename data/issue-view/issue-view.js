self.port.on('set-issue', function(issue){
	console.log("Sample issue: " + issue);
	switch(issue.type){
		case "Task":
		case "Phase":
		case "Epic":
		case "Story":
			activateIssueControls();
			break;
		case "Session":
			break;
		default:
			activateIssueControls();
			break; 
	}
});

function activateIssueControls() {
	console.log("trying to hide some controls");
	$("#annotations").remove();
	$("#phases").remove();
}

console.log("Script is loaded");