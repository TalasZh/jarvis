var animateNotActive = true;

var Popup = function (data, eventListener) {
    this.DATA = data;
    this.selectedData;
    this.eventListener = eventListener;

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

Popup.prototype.init = function () {
    var self = this;
    /*$(document).on('click', function (event) {
        if ($(event.target).closest('.b-issue-popup').length == 0 && $(event.target).closest('canvas').length == 0) {
            $('.b-issue-popup').fadeOut(300);
        }
    });*/
    $(document).on('click', 'button.btn-close', function () {
        this.Z_INDEX = 10000;
        $('.document-lister').remove();
    });

    $(document).on('click', '.b-issue-popup__close', function (event) {
        $('.b-issue-popup').fadeOut();
    });

	$('.b-issue-popup').hover(function(){
		var popupEvent = new CustomEvent("PopupOpened", {});
		document.getElementById("renderCanvas").dispatchEvent(popupEvent);
	});

	$(document).on({
		mouseenter: function () {
			var popupEvent = new CustomEvent("PopupOpened", {});
			document.getElementById("renderCanvas").dispatchEvent(popupEvent);
		},
		mouseleave: function () {
		}
	}, '.document-lister');

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
            if (delta > 0) {
                self.scrollResearch(-1);
            } else {
                self.scrollResearch(1);
            }
            e.preventDefault ? e.preventDefault() : (e.returnValue = false);
        }
    }
};

Popup.prototype.showMinimap = function (id, eventId) {
    this.audioPopup.play();
    var popupLimit = 4;

	var popupEvent = new CustomEvent("PopupOpened", {
		detail: {
			"id": id,
			"eventId": eventId
		}
	});
	document.getElementById("renderCanvas").dispatchEvent(popupEvent);	

    var utmost = false;

    var data = $.grep(this.DATA, function (e) {
        return e.issueId == id;
    });
    data = data[0];

    this.selectedData = data;

    for (var i = 0; i < data.changelogList.length && popupLimit > 0; i++) {
        var currentWidthDiff = ( 5 - popupLimit ) * 15;
        var currentHeightDiff = ( 5 - popupLimit ) * 30;

        if (data.changelogList[i].changeKey.changeItemId == eventId) {

            this.selected_doc = i;

            var popupData = this.getPopupData(i, data, true);
            this.createPopupWindow(popupData);

            utmost = true;
        } else if (utmost && popupLimit-- >= 0) {

            var popupData = this.getPopupData(i, data, false);
            popupData.width = (this.left * 4 - currentWidthDiff * 2);
            popupData.height = (this.top_offset * 4);
            popupData.left = (this.left + currentWidthDiff);
            popupData.top = (this.top_offset - currentHeightDiff);

            this.createPopupWindow(popupData);
        }
    }
}

Popup.prototype.scrollResearch = function (mvmnt) {
    if ($('.document-lister').length == 0) {
        return;
    }

	if(animateNotActive){
		var index_lowest = 1000000;
		var index_highest = 0;
		var top_doc;
		var bottom_doc;
		animateNotActive = false;

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
				animateNotActive = true;
				return;
			}
			this.selected_doc++;

			top_doc.remove();

			$('.document-lister').animate({
				top: '+=' + this.height_diff + 'px',
				left: '-=' + this.width_diff + 'px',
				width: '+=' + this.width_diff * 2 + 'px'
			}, function(){
				animateNotActive = true;
			});

			this.Z_INDEX = index_lowest - 1;

			if (this.selected_doc + 4 < this.selectedData.changelogList.length) {

				var popupData = this.getPopupData(this.selected_doc, this.selectedData, false);
				popupData.width = (this.left * 4 - this.width_diff * 4 * 2);
				popupData.height = (this.top_offset * 4);
				popupData.left = (this.left + this.width_diff * 4);
				popupData.top = (this.top_offset - this.height_diff  * 4);

				this.createPopupWindow(popupData);
			}
		} else {
			if (this.selected_doc - 1 < 0) {
				animateNotActive = true;
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
			}, function(){
				animateNotActive = true;
			});

			this.Z_INDEX = index_highest + 1;

			var popupData = this.getPopupData(this.selected_doc, this.selectedData, true);

			this.createPopupWindow(popupData);
		}
		highlightTextToTop();
	}
}

Popup.prototype.getPopupData = function (i, fromData, mainPosition) {

    var dateToDateFormat = new Date(fromData.changelogList[i].changeKey.created);
    var showDate = dateToDateFormat.getDate() + '.' + dateToDateFormat.getMonth() + '.' + dateToDateFormat.getFullYear();

	var popupText = fromData.changelogList[i].toString;

	if(fromData.changelogList[i].fromString !== undefined){
		popupText = fromData.changelogList[i].fromString + ' &rarr; ' + popupText;
	}

	if(fromData.changelogList[i].field !== undefined){
		popupText = 'field: ' + fromData.changelogList[i].field + '<br>' + popupText;
	}	

    var popupData = {
        "zIndex": this.Z_INDEX--,
        "issueKey": fromData.issueKey,
        "taskDate": showDate,
        "taskText": popupText,
        "taskId": i
    };

    if( fromData.changelogList[i].field == "Research Session" )
    {
        popupData.url = fromData.changelogList[i].url;

        popupData.quote = fromData.changelogList[i].quote;
        popupData.taskText = fromData.changelogList[i].url + "<br>Quote: " + fromData.changelogList[i].quote + "<br>Comments: " + fromData.changelogList[i].comments;
    }

    if (mainPosition) {
        popupData.width = (this.left * 4);
        popupData.height = (this.top_offset * 4);
        popupData.left = this.left;
        popupData.top = this.top_offset;
    }

    return popupData;
}

Popup.prototype.createPopupWindow = function (popupData) {
    var strBuilder = sprintf('<div style="width: %(width)dpx; height: %(height)dpx;  left: %(left)d' +
        'px; top: %(top)dpx; z-index: %(zIndex)d;" class="document-lister">' +
        '<table><tr class="task"><td style="width: 25px"><img src="assets/img/icon-task.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
        '<td>TASK</td><td style="width: 30px"><img src="assets/img/icon-time.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
        '<td>DATE</td></tr><tr class="task-name"><td style="width: 25px"></td>' +
        '<td>%(issueKey)s</td><td style="width: 30px"></td><td>%(taskDate)s</td></tr></table><div class="divider"></div>' +
        '<div class="task-info js-content-for-%(taskId)d">%(taskText)s</div>', popupData);

    if (popupData.url) {

        strBuilder += sprintf('<script>getHighlightedStringInText(' +
            '"%(url)s",' +
            '"%(quote)s", %(taskId)d);' +
            '</script>', popupData);
    }

    strBuilder += '<button type="button" class="btn btn-default btn-close"></button></div>';

    $('body').append(strBuilder);
};

function getHighlightedStringInText(url, searchText, id) {
    var readabilityUrl = 'https://www.readability.com/api/content/v1/parser?url=' + url + '&token=0626cdbea9b15ece0ad7d9ee4af00c7a6fd13b40&callback=?'
    return $.getJSON(readabilityUrl).then(function (data) {
        var contentText = data.content.replace(searchText, '<span class="b-highlight-string">' + searchText + '</span>');
        contentText = contentText
			.replace(/<img[^>]*>/g, "")
			.replace(/<\s*(\w+).*?>/, '<$1>');
        var responseContentDiv = $('.js-content-for-' + id);

        responseContentDiv.html(responseContentDiv.html() + '<div class="b-request-text">' + contentText + '</div>');
		responseContentDiv.find('header').remove();
		responseContentDiv.find('footer').remove();
        responseContentDiv.css('max-height', responseContentDiv.parent().height() - 52);

		highlightTextToTop();
    });
}

function highlightTextToTop() {
	$('.b-request-text').each(function(){
		var highlightOffsetTop = $(this).find('.b-highlight-string').offset().top;
		$(this).css('max-height', $(this).parents('.document-lister').height() - 52);		
		var topScroll = highlightOffsetTop - $(this).offset().top;
		$(this).scrollTop(topScroll);
	});
}

Popup.prototype.mouseOverPopup = function (text) {
    if (!this.eventListener.MOVE_CAMERA) {
        $('#popup .b-popup-text').text(text);
        $('#popup').show();
        var popupHeight = $('#popup').height() + 32;
        var popupWidth = $('#popup').width() + 16;
        $('#popup').css("top", ( window.event.clientY - popupHeight - 10) + "px");
        $('#popup').css("left", (window.event.clientX - (popupWidth / 2)) + "px");
    }
};

Popup.prototype.popupHide = function () {
    $('#popup').hide();
};

Popup.prototype.getIssuePopup = function (issueId, issue)
{
    this.audioPopup.play();
    if (issueId == false) {
        data = issue;
    } else {
        var data = $.grep(this.DATA, function (e) {
            return e.issueId == issueId;
        });
        data = data[0];
    }

    var popupBlock = $('.b-issue-popup');
    var currentField = false;
    for (var key in data) {
		if( key.indexOf("$") > -1 ) continue;
        if (popupBlock.find('#js-issue-' + key).length > 0) {
            currentField = popupBlock.find('#js-issue-' + key);
            var text = data[key];
            if (data[key] === null || data[key].length == 0) {
                text = 'none';
            } else if (key == 'originalEstimateMinutes') {
                var hours = Math.floor(data[key] / 60);
                var minutes = data[key] % 60;
                text = hours + 'h ';
                if (minutes > 0) {
                    text += minutes + 'm';
                }
            }
            if (currentField.hasClass('js-text-to-span')) {
                currentField.find('span').text(text);
            } else {
                currentField.text(text);
            }
        }
    }
    if (data.changelogList !== undefined) {
        var changelogList = popupBlock.find('#js-issue-changelog-list');
        var changelogListHtml = '';
        for (var i = 0; i < data.changelogList.length; i++) {
            changelogListHtml += '<div style="margin-top: 12px">' +
                '<div style="width: 40px; display: inline-block"><img src="assets/img/icon-11.svg" alt="" height="28px"/></div>' +
                '<span style="opacity: 0.6;">' + data.changelogList[i].toString + '</span>' +
                '</div>' +
                '<div style="margin-top: 25px; padding-bottom: 13px; border-bottom: 1px solid #00142d;">' +
                '<div style="width: 40px; display: inline-block"><img src="assets/img/icon-12.svg" alt="" height="28px"/></div>' +
                '<span style="opacity: 0.6;">' + data.changelogList[i].field + '</span>' +
                '</div>';
        }

        changelogList.html(changelogListHtml);
    }
    if ( popupBlock.is(":hidden")){
        popupBlock.fadeIn();
    }
};

