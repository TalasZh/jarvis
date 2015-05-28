const filter = link => selection.containsNode(link, true)
const toHref = link => link.href

const onClick = (node, data) => {
	selection = getSelection();
	console.log( "selection");

	console.log ( selection );
	self.postMessage({
		links: Array.from(document.getElementsByTagName("a")).filter(filter).map(toHref)
	});
};

function getSelectionText() {
    var text = "";
    if (window.getSelection) {
        text = window.getSelection().toString();
    } else if (document.selection && document.selection.type != "Control") {
        text = document.selection.createRange().text;
    }
    self.postMessage([
        document.location.toString(),
        "content",
        text
    ]);
    return text;
}

let selection;

self.on("click", getSelectionText);

