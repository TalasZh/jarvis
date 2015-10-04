var Popup = function (data, eventListener) {
	this.DATA = data;
	this.selectedData;
    this.eventListener = eventListener

	this.SCREEN_WIDTH = window.innerWidth;
	this.SCREEN_HEIGHT = window.innerHeight;
	this.Z_INDEX = 10000;

	this.selected_doc = 0;

	this.left = parseInt(this.SCREEN_WIDTH / 6);
	this.top_offset = parseInt(this.SCREEN_HEIGHT / 6);
	this.width_diff = 15;
	this.height_diff = 30;

    this.audioPopup = new Audio('assets/audio/Popup.wav');

    this.init();
};

Popup.prototype.init = function() {
    var self = this;

    $(document).on('click', 'button.btn-close', function () {
        this.Z_INDEX = 10000;
        $('.document-lister').remove();
    });

	$(document).on('click', '.b-issue-popup__close', function (event) {
		$('.b-issue-popup').hide();
	});	

    var elem = document;
    if (elem.addEventListener) {
        if ('onwheel' in document) {
            elem.addEventListener("wheel", onWheel);
        }
    }

    function onWheel(e) {
        e = e || window.event;

        var delta = e.deltaY || e.detail || e.wheelDelta;

        if ($(event.target).closest('.document-lister').length > 0 && $(event.target).closest('.task-info').length === 0) {
            if(delta > 0) {
                self.scrollResearch(-1);
            } else {
                self.scrollResearch(1);
            }
            e.preventDefault ? e.preventDefault() : (e.returnValue = false);
        }
    }
};

Popup.prototype.showMinimap = function(id, eventId)
{
    this.audioPopup.play();
	var popupLimit = 4;

    var utmost = false;

    var data = $.grep(this.DATA, function (e) {
        return e.issueId == id;
    });
    data = data[0];

    this.selectedData = data;

    for (var i = 0; i < data.changelogList.length && popupLimit > 0; i++) {
        var width_diff = ( 5 - popupLimit ) * 15;
        var height_diff = ( 5 - popupLimit ) * 30;

        if (data.changelogList[i].changeKey.changeItemId == eventId) {

			this.selected_doc = i;

			var popupData = this.getPopupData(i, data, true);
			this.createPopupWindow(popupData);

            utmost = true;
        } else if (utmost && popupLimit-- >= 0) {

			var popupData = this.getPopupData(i, data, false);
			popupData.width = (this.left * 4 - width_diff * 2);
			popupData.height = (this.top_offset * 4);
			popupData.left = (this.left + width_diff);
			popupData.top = (this.top_offset - height_diff);

			this.createPopupWindow(popupData);
        }
    }
}

Popup.prototype.scrollResearch = function(mvmnt) {
    if ($('.document-lister').length == 0) {
        return;
    }

    var index_lowest = 1000000;
    var index_highest = 0;
    var top_doc;
    var bottom_doc;

    $('.document-lister').each(function () {
        var index_current = parseInt($(this).css("zIndex"));

        if (index_current > index_highest) {
            index_highest = index_current;
            top_doc = $(this);
        }

        if (index_current < index_lowest) {
            index_lowest = index_current;
            bottom_doc = $(this);
        }
    });

    if (mvmnt > 0) {

        if (this.selected_doc + 1 == this.selectedData.changelogList.length) {
            return;
        }
        this.selected_doc++;

        top_doc.remove();

        $('.document-lister').animate({
            top: '+=' + this.height_diff + 'px',
            left: '-=' + this.width_diff + 'px',
            width: '+=' + this.width_diff * 2 + 'px'
        });

        this.Z_INDEX = index_lowest - 1;

        if (this.selected_doc + 4 < this.selectedData.changelogList.length) {

			var popupData = this.getPopupData(this.selected_doc, this.selectedData, false);
			popupData.width = (this.left * 4 - this.width_diff * 2);
			popupData.height = (this.top_offset * 4);
			popupData.left = (this.left + this.width_diff);
			popupData.top = (this.top_offset - this.height_diff );

			this.createPopupWindow(popupData);
        }
    } else {
        if (this.selected_doc - 1 < 0) {
            return;
        }
        this.selected_doc--;

        if ($('.document-lister').length >= 5) {
            bottom_doc.remove();
        }

        $('.document-lister').animate({
            top: '-=' + this.height_diff + 'px',
            left: '+=' + this.width_diff + 'px',
            width: '-=' + this.width_diff * 2 + 'px'
        });

        this.Z_INDEX = index_highest + 1;

		var popupData = this.getPopupData(this.selected_doc, this.selectedData, true);

		this.createPopupWindow(popupData);
    }
}

Popup.prototype.getPopupData = function(i, fromData, mainPosition) {

	var dateToDateFormat = new Date(fromData.changelogList[i].changeKey.created);
	var showDate = dateToDateFormat.getDate() + '.' + dateToDateFormat.getMonth() + '.' + dateToDateFormat.getFullYear();

	var popupData = {
		"zIndex": this.Z_INDEX--,
		"issueKey": fromData.issueKey,
		"taskDate": showDate,
		"taskText": fromData.changelogList[i].toString,
		"taskId": i
	};

	if(mainPosition) {
		popupData.width = (this.left * 4);
		popupData.height = (this.top_offset * 4);
		popupData.left = this.left;
		popupData.top = this.top_offset;
	}

	return popupData;
}

Popup.prototype.createPopupWindow = function(popupData) {
	var strBuilder = sprintf('<div style="width: %(width)dpx; height: %(height)dpx;  left: %(left)d' +
		'px; top: %(top)dpx; z-index: %(zIndex)d;" class="document-lister">' +
		'<table><tr class="task"><td style="width: 25px"><img src="assets/img/icon-task.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
		'<td>TASK</td><td style="width: 30px"><img src="assets/img/icon-time.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
		'<td>DATE</td></tr><tr class="task-name"><td style="width: 25px"></td>' +
		'<td>%(issueKey)s</td><td style="width: 30px"></td><td>%(taskDate)s</td></tr></table><div class="divider"></div>' +
		'<div class="task-info js-content-for-%(taskId)d">%(taskText)s</div>' +
		'<script>getHighlightedStringInText(' +
		'"https://paul.kinlan.me/rise-of-the-meta-platforms/",' +
		'"Every single platform", %(taskId)d);' +
		'</script>' +
		'<button type="button" class="btn btn-default btn-close"></button></div>', popupData);

	$('body').append(strBuilder);
};

function getHighlightedStringInText (url, searchText, id) {
    var readabilityUrl = 'https://www.readability.com/api/content/v1/parser?url=' + url + '&token=0626cdbea9b15ece0ad7d9ee4af00c7a6fd13b40&callback=?'
    return $.getJSON(readabilityUrl).then(function(data){
        var contentText = data.content.replace(searchText, '<span class="b-highlight-string">'+searchText+'</span>');
        contentText = contentText.replace(/<img[^>]*>/g, "");
        contentText = contentText.replace('id="content"', '');
        var responseContentDiv = $('.js-content-for-' + id);

        responseContentDiv.html(responseContentDiv.html() +  '<div class="b-request-text">' + contentText + '</div>');
		responseContentDiv.css('max-height', responseContentDiv.parent().height() - 52);

		var highlightOffsetTop = responseContentDiv.find('.b-highlight-string').offset().top;
		var responseDiv = responseContentDiv.find('.b-request-text').css('max-height', responseContentDiv.parent().height() - 52);
        var topScroll = highlightOffsetTop - responseDiv.offset().top;
        responseDiv.scrollTop(topScroll);
    });
}

Popup.prototype.mouseOverPopup = function(text) {
    if (!this.eventListener.MOVE_CAMERA) {
        $('#popup .b-popup-text').text(text);
        $('#popup').show();
		var popupHeight = $('#popup').height() + 32;
		var popupWidth = $('#popup').width() + 16;
        $('#popup').css("top", ( window.event.clientY - popupHeight - 10) + "px");
        $('#popup').css("left", (window.event.clientX - (popupWidth / 2)) + "px");
    }
};

Popup.prototype.popupHide = function() {
    $('#popup').hide();
};

Popup.prototype.getIssuePopup = function(issueId, issue) {

	if(issueId == false) {
		data = issue;
	} else {
		var data = $.grep(this.DATA, function (e) {
			return e.issueId == issueId;
		});
		data = data[0];
	}
	console.log(data);

	var popupBlock = $('.b-issue-popup');
	var currentField = false;
	for(var key in data){
		if(popupBlock.find('#js-issue-' + key).length > 0){
			currentField = popupBlock.find('#js-issue-' + key);
			var text = data[key];
			if(data[key] === null || data[key].length == 0){
				text = 'none';
			} else if(key == 'originalEstimateMinutes') {
				var hours = Math.floor( data[key] / 60);          
				var minutes = data[key] % 60;
				text = hours + 'h ';
				if(minutes > 0) {
					text += minutes + 'm';
				}
			}
			if(currentField.hasClass('js-text-to-span')){
				currentField.find('span').text(text);
			} else {
				currentField.text(text);
			}
		}
	}
	if(data.changelogList !== undefined) {
		var changelogList = popupBlock.find('#js-issue-changelog-list');
		var changelogListHtml = '';
		for(var i = 0; i < data.changelogList.length; i++){
			changelogListHtml += '<div style="margin-top: 12px">' +
				'<div style="width: 40px; display: inline-block"><img src="assets/img/icon-11.svg" alt="" height="28px"/></div>' +
				'<span style="opacity: 0.6;">' + data.changelogList[i].toString + '</span>' +
			'</div>' +
			'<div style="margin-top: 25px; padding-bottom: 13px; border-bottom: 1px solid #00142d;">' +
				'<div style="width: 40px; display: inline-block"><img src="assets/img/icon-12.svg" alt="" height="28px"/></div>' +
				'<span style="opacity: 0.6;">' + data.changelogList[i].field + '</span>' +
			'</div>';
		}
		console.log(changelogList);
		changelogList.html(changelogListHtml);
	}
	popupBlock.show();
};

