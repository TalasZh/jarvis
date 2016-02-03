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


function highlightSelection()  {
    var selection;

    //Get the selected stuff
    if(window.getSelection) {
        selection = window.getSelection();
    }
    else if(typeof document.selection!="undefined"){
        selection = document.selection;
    }

    //Get a the selected content, in a range object
    var range = selection.getRangeAt(0);

    //If the range spans some text, and inside a tag, set its css class.
    if( range && !selection.isCollapsed )
    {
        if( selection.anchorNode.parentNode == selection.focusNode.parentNode )
        {
            var newNode = document.createElement("div");
            newNode.setAttribute(
               "style",
               "background-color: yellow; display: inline;"
            );
            range.surroundContents(newNode);
        }   
    }

    self.postMessage([
        document.location.toString(),
        "content",
        selection.toString()
    ]);
}

let selection;

self.on("click", highlightSelection);

