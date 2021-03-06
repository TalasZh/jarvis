var animateNotActive = true;
var currentMousePos = { x: -1, y: -1 };

$(document).mousemove(function(event) {
	currentMousePos.x = event.pageX;
	currentMousePos.y = event.pageY;
});

function closeIssuePopup() {
	$('.b-issue-popup').fadeOut(300, function(){
		$('.b-issue-popup__info').hide();
	});		
}

function highlightTextToTop() {
	$('.b-request-text').each(function(){
		$(this).css('max-height', $(this).parents('.document-lister').height() - 52);
		var findedString = $(this).find('.b-highlight-string');
		if(findedString.length > 0) {
			var highlightOffsetTop = $(this).find('.b-highlight-string').offset().top;
			var topScroll = highlightOffsetTop - $(this).offset().top;
			$(this).scrollTop(topScroll);
		}
	});
}

var degree;
var scrollHeight;
var maxTop;
var minTop;
var parentOffset;
var startPos;
var mouseButtonDown = false;
var totalPopups = 5;
var activePopup = 1;

function initScrollForPopup() {
	degree = $('.parent-scroll').height() / totalPopups;
	var newHeight = $('.parent-scroll').height() / totalPopups;
	$('.js-scroll').height(newHeight);
	$('.js-scroll').css('line-height', newHeight + 'px');
	scrollHeight = $('.js-scroll').height();
	maxTop = $('.parent-scroll').height() - scrollHeight;
	minTop = 0;
	parentOffset = $('.parent-scroll').parent().offset().top;
	setPopupScrollRollerDate();
	setPopupScrollRollerPosition();
}

function setPopupScrollRollerPosition() {
	if(!mouseButtonDown) {
		startPos = (totalPopups - activePopup) * degree;
		$('.js-scroll').animate({'top': startPos}, 300);
	}
}

function setPopupScrollRollerDate() {
	var index_highest = 0;
	var top_doc;

	$('.document-lister').each(function () {
		var index_current = parseInt($(this).css("zIndex"));

		if (index_current > index_highest) {
			index_highest = index_current;
			top_doc = $(this);
		}
	});

	$('.js-scroll .b-scroll-helper').html(top_doc.find('.js-task-date').html());	
}

var Popup = function (data, eventListener) {
    this.DATA = data;
    this.selectedData;
    this.eventListener = eventListener;

    this.SCREEN_WIDTH = window.innerWidth;
    this.SCREEN_HEIGHT = window.innerHeight;
    this.Z_INDEX = 10000;

    this.selected_doc = 0;
    this.activeScroll = false;

    this.left = parseInt(this.SCREEN_WIDTH / 6);
    this.top_offset = parseInt(this.SCREEN_HEIGHT / 6);
    this.width_diff = 15;
    this.height_diff = 30;

	this.issueTypes = {
		"Task": "task.png",
		"Design": "design.png",
		"Research": "research.png",
		"Requirement": "requirments.png",
		"Story": "task.png",
		"Bug": "bug.png",
		"Improvement": "Improvement.png",
		"Playbook": "playbook.png",
		"New Feature": "new-features.png"
	};

	this.issueStatus = {
		"Resolved": "resolved.png",
		"Unresolved": "unresolved.png"
	};

	this.issuePriority = {
		1: {"img": "blocker.png", "title": "Blocker"},
		2: {"img": "critical.png", "title": "Critical"},
		3: {"img": "major.png", "title": "Major"},
		4: {"img": "minor.png", "title": "Minor"},
		5: {"img": "trivial.png", "title": "Trivial"}
	};

    this.audioPopup = new Audio('assets/audio/Popup.wav');

    this.init();
};

Popup.prototype.setData = function (data) {
    this.DATA = data;
}

Popup.prototype.init = function () {
    var self = this;

	$(document).mouseout(function() {
		$(document).on('click', function (event) {
			if ($(event.target).closest('.b-issue-popup').length == 0 && $(event.target).closest('canvas').length == 0) {
				closeIssuePopup();
			}
		});
	});

	function removeWorkLogPopups() {
        this.Z_INDEX = 10000;
        $('.document-lister').remove();
		$('.parent-scroll').fadeOut(300);
	}

    $(document).on('click', 'button.btn-close', function () {
		removeWorkLogPopups();
    });

    $(document).on('click', '.b-issue-popup__close', function (event) {
		closeIssuePopup();
    });

	$(document).keyup(function(e) {
		if(e.keyCode == 27){
			closeIssuePopup();
			removeWorkLogPopups();
		}
	});

	$('.b-issue-popup').on('click', '.b-issue-popup-accordion__title', function(){
		if($(this).hasClass('b-issue-popup-accordion__title_open')) {
			$(this).removeClass('b-issue-popup-accordion__title_open');
		} else {
			$(this).addClass('b-issue-popup-accordion__title_open');
		}
		$(this).next('.b-issue-popup-accordion__body').slideToggle();
	});

	$(document).on('click', '.document-lister', function(event){
		if($(event.target).closest('.btn-close').length > 0){
			return;
		}

		var scrollCounter = 0;
		var currentIndex = $(this).css("zIndex");

		$('.document-lister').each(function () {
			var index_current = parseInt($(this).css("zIndex"));

			if (index_current > currentIndex) {
				scrollCounter++;
			}
		});

		if(scrollCounter > 0){
			for(var i = scrollCounter; i > 0; i--) {
				self.scrollResearch(1, true);
			}
		}
	});

	$('.b-issue-popup').on('click', '.js-issue-popup-show-more', function(){
		$('.b-issue-popup__info').slideUp(300);
		if($($(this).attr('href')).hasClass('js-opened')) {
			$($(this).attr('href')).removeClass('js-opened');
		} else {
			$('.b-issue-popup__info').removeClass('js-opened');
			$($(this).attr('href')).addClass('js-opened').slideToggle(300);
		}
		return false;
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

        if ($(e.target).closest('.document-lister').length > 0 && $(e.target).closest('.task-info').length === 0) {
            if (delta > 0) {
                self.scrollResearch(-1, true);
            } else {
                self.scrollResearch(1, true);
            }
            e.preventDefault ? e.preventDefault() : (e.returnValue = false);
        }
    }

	//reserch scroller
	$('.js-scroll').mousedown(function(){
		mouseButtonDown = true;
	}).mouseup(function(){
		mouseButtonDown = false;
	});

	$('.parent-scroll').mouseover(function(){
		mouseButtonDown = false;
	});

	$('.parent-scroll').mousemove(function(event){
		if(mouseButtonDown == false) return;
		var relY = event.pageY - parentOffset;
		var newPosition = relY - (scrollHeight / 2);
		var nextActive = Math.round(newPosition / degree) + 1;
		console.log(activePopup);
		if(activePopup < nextActive){
			self.scrollResearch(-1);
		} else if(activePopup > nextActive) {
			self.scrollResearch(1);
		}
		activePopup = nextActive;
		if(newPosition >= minTop && newPosition <= maxTop){
			$('.js-scroll').css('top', newPosition);
		}
	});

	$('.b-issue-popup').tooltip({
		selector: '.js-dynamic-tooltip'
	});
};

Popup.prototype.showMinimap = function (id) {

	closeIssuePopup();
    this.audioPopup.play();
    var popupLimit = 4;

    var utmost = false;

	this.activeScroll = false;
    var data = $.grep(this.DATA, function (e) {
        return e.issueId == id;
    });
    data = data[0];

    this.selectedData = data;
	this.activeScroll = true;

	totalPopups = data.annotations.length;

    for (var i = 0; i < data.annotations.length && popupLimit > 0; i++) {
        var currentWidthDiff = ( 5 - popupLimit ) * 15;
        var currentHeightDiff = ( 5 - popupLimit ) * 30;

        if (!utmost) {
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
	initScrollForPopup();
	$('.parent-scroll').fadeIn(300);
}

Popup.prototype.scrollResearch = function (mvmnt, setScrollPosition) {
	if(setScrollPosition === null || setScrollPosition === undefined) setScrollPosition = false;
    if ($('.document-lister').length == 0) {
        return;
    }

	if(animateNotActive && this.activeScroll){
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

			if (this.selected_doc + 1 == this.selectedData.annotations.length) {
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

			if(setScrollPosition){
				activePopup = activePopup + 1;
				setPopupScrollRollerPosition();
			}

			this.Z_INDEX = index_lowest - 1;

			if (this.selected_doc + 4 < this.selectedData.annotations.length) {

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

			if(setScrollPosition){
				activePopup = activePopup - 1;
				setPopupScrollRollerPosition();
			}			

			this.Z_INDEX = index_highest + 1;

			var popupData = this.getPopupData(this.selected_doc, this.selectedData, true);

			this.createPopupWindow(popupData);
		}
		highlightTextToTop();
		setPopupScrollRollerDate();
	}
}

Popup.prototype.getPopupData = function (i, fromData, mainPosition) {

	console.log(fromData.annotations[i].created);
    var showDate = getDateInFormat(fromData.annotations[i].created, 'DD.MM.YYYY');

	var popupText = fromData.annotations[i].text;
	var quote = fromData.annotations[i].quote.replace(/(\r\n|\n|\r)/gm,"");

    var popupData = {
        "zIndex": this.Z_INDEX--,
        "issueKey": fromData.issueKey,
        "taskDate": showDate,
        "taskText": popupText,
        "taskId": i,
        "url": fromData.annotations[i].uri,
        "quote": quote
    };

	popupData.taskText = '<a href="javascript:window.open(\'' + fromData.annotations[i].uri + '\',\'research page\',\'width=500,height=400\')" target="_blank">' +fromData.annotations[i].uri + "</a><br>Quote: " + fromData.annotations[i].quote + "<br>Comments: " + fromData.annotations[i].text;

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
        '<td>%(issueKey)s</td><td style="width: 30px"></td><td class="js-task-date">%(taskDate)s</td></tr></table><div class="divider"></div>' +
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
        var contentText = data.content
			.replace(/<img[^>]*>/g, "")
			.replace(/class=\"(.*?)\"/g, '')
			.replace(/id=\"(.*?)\"/g, '');
		//console.log(contentText.split(/(\s*<\/?[a-zA-Z]>)/g));
        contentText = contentText.replace(searchText, '<span class="b-highlight-string">' + searchText + '</span>');
        var responseContentDiv = $('.js-content-for-' + id);

        responseContentDiv.html(responseContentDiv.html() + '<div class="b-request-text">' + contentText + '</div>');
		responseContentDiv.find('header').remove();
		responseContentDiv.find('footer').remove();
        responseContentDiv.css('max-height', responseContentDiv.parent().height() - 52);

		highlightTextToTop();
    });
}

Popup.prototype.getIssuePopup = function (issueId)
{
    this.audioPopup.play();

	var data = $.grep(this.DATA, function (e) {
		return e.issueId == issueId;
	});
	data = data[0];

	console.log(data);

	if(data == undefined) return;

	if(data.annotations.length == 0) {
		$('.js-research-button').hide();
	} else {
		$('.js-research-button').show();
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

			} else if(key == 'issueKey') {

				text = createLink(text);

			} else if(key == 'type') {

				console.log(text.name);
				var img = this.issueTypes[text.name];
				var title = text.name;

			} else if(key == 'priority') {

				var img = this.issuePriority[text].img;
				var title = this.issuePriority[text].title;

			} else if(key == 'status') {

				if(text == 'Resolved'){
					var img = this.issueStatus[text];
					var title = text;
				} else {
					var img = this.issueStatus['Unresolved'];
					var title = 'Unresolved';
				}

			} else if(key == 'creationDate') {

				text = getDateInFormat(text, 'DD.MM.YYYY');

			} else if(key == 'timeSpentMinutes') {

				text = text + ' minutes';			

			} else if (key == 'originalEstimateMinutes') {

				var hours = Math.floor(data[key] / 60);
				var minutes = data[key] % 60;
				text = hours + 'h ';
				if (minutes > 0) {
					text += minutes + 'm';
				}

			} else if(key == 'issueLinks') {
				for(var i = 0; i < text.length; i++){
					if(text[i].direction == 'OUTWARD') {
						text = createLink(text[i].linkDirection.issueKey);
						break;
					}
				}
			} else if ( Object.prototype.toString.call( text ) === '[object Array]' ) {
				text = text.join(', ');
			}

			if (currentField.hasClass('js-text-to-span')) {
				currentField.find('span').html(text);
			} else if(currentField.hasClass('js-text-to-img')) {
				currentField.attr('src', 'assets/icons/' + img);
				currentField.tooltip('hide')
				  .attr('data-original-title', title)
				  .tooltip('fixTitle');
			} else {
				currentField.html(text);
			}
		}
	}

	if (data.issueWorkLogs !== undefined) {
		var workLogsList = popupBlock.find('.b-issue-popup-work-log');
		var workLogsListHtml = '';
		for (var i = 0; i < data.issueWorkLogs.length; i++) {
			var workLogDate = getDateInFormat(data.issueWorkLogs[i].createDate);
			workLogsListHtml += '<div class="b-issue-popup-work-log__item">' +
						'<p>' + workLogDate + '</p>' +
						'<p><a href="#">Time Spent: ' + Math.round( data.issueWorkLogs[i].timeSpentSeconds / 3600 ) + ' hours</a></p>' +
						'<p>' + data.issueWorkLogs[i].comment + '</p>' +
					'</div>';
		}

		workLogsList.html(workLogsListHtml);
	}

	if (data.changelogList !== undefined) {
		var changeLogList = popupBlock.find('#js-issue-popup-activity-table');
		var changeLogListHtml = '';
		for (var i = 0; i < data.changelogList.length; i++) {
			var changeLogDate = getDateInFormat(data.changelogList[i].changeKey.created);
			var changeLogText = data.changelogList[i].author + ' made changes to ' + data.changelogList[i].field
			var changeLogType = 'Deleted';

			if(data.changelogList[i].fromString != null) {
				changeLogType = data.changelogList[i].fromString;
			} else if(data.changelogList[i].toString != null)  {
				changeLogType = data.changelogList[i].toString;
			}

			changeLogListHtml += '<tr>' +
							'<td>' + changeLogDate + '</td>' +
							'<td> | </td>' +
							'<td>' + changeLogText + '</td>' +
							'<td> ' + changeLogType + '</td>' +
						'</tr>';
		}

		changeLogList.html(changeLogListHtml);
	}

	if (data.issueLinks !== undefined) {
		var issueLinksList = popupBlock.find('#js-issue-popup-issue-links');
		var issueLinksListHtml = '';
		for (var i = 0; i < data.issueLinks.length; i++) {
			var issueLinksListText = data.issueLinks[i].linkType[data.issueLinks[i].direction.toLowerCase()] +
				' ' + data.issueLinks[i].linkDirection.issueKey;
			issueLinksListHtml += '<p>' + issueLinksListText + '</p>';
		}

		issueLinksList.html(issueLinksListHtml);
	}

	if (data.commits !== undefined) {
		var commitsList = popupBlock.find('#js-issue-popup-commits');
		var commitsListHtml = '';
		for (var i = 0; i < data.commits.length; i++) {
			var commitsDate = getDateInFormat(data.commits[i].authorTimestamp);
			commitsListHtml += '<p class="b-date">' + commitsDate + '</p>' +
				'<p>' + data.commits[i].commitMessage + '</p>';
		}

		commitsList.html(commitsListHtml);
	}

	if ( popupBlock.is(":hidden")){
		popupBlock.fadeIn();
	}
};

var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"];

function getDateInFormat(date, mask) {
	if(mask === undefined || mask === null) mask = 'DD.MM.YYYY HH:mm';
	var dateToJsFormar = moment(date);
	/*var formatedDate = dateToJsFormar.getDate() + 
		'/' + this.monthNames[dateToJsFormar.getMonth()] +
		'/' + dateToJsFormar.getFullYear() +
		' ' + dateToJsFormar.getHours() +
		':' + dateToJsFormar.getMinutes();	*/
	return dateToJsFormar.format(mask);
}

function createLink(key) {
	return '<a href="https://jira.subutai.io/browse/' + key + '" target="_blank">' + key + '</a>';
}

