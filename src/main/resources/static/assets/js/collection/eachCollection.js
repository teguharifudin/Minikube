var $collectionSlugName;

$(function() {
	$collectionSlugName = getSlugNameFromLink(window.location.pathname);
});

$(window).scroll(function() {
    // Buffer to trigger the action slightly before reaching the bottom
    var buffer = 100;
    // Check if the user has scrolled to the bottom
    if ($(window).scrollTop() + $(window).height() >= $(document).height() - buffer) {
        // You have scrolled to the bottom of the page (with a buffer), so take action here
        var page = Math.ceil($('.icon-box').length / 60) + 1;
        getQuotesByCollection(page);
    }
});

function getQuotesByCollection(page) {
	$.ajax({
		type: "GET",
		url: "/quote/v1/quotes?collection=" +  $collectionSlugName + "&page=" + page + "&per_page=",
		contentType: "application/json",
		success: function(data, textStatus, jqXHR) {
			var message = '';
			for (var i = 0; i < data.length; i++) {
				message = message
						+ '<div class="item">\n'
						+ '<div class="item-content"><section class="icon-box">\n'
						+ '<h4 class="quote-content">' + data[i].content + '</h4>\n'
						+ '<p class="quote-author">' + (data[i].author ? data[i].author.name : 'Anonymous') + '</p></section>\n'
						+ '<footer><button class="share-button">\n'
						+ '<i class="fa fa-share-alt fa-x"></i></button>\n'
						+ '<button class="exclam-button">\n'
						+ '<i class="fa fa-exclamation-circle fa-x"></i></button></footer></div></div>\n';
			}
			$lstItem.append(message);
			resizeAllMasonryItems();
			setRandomColor(page);
	    },
		error: function(jqXHR, textStatus, errorThrown){
			console.log(errorThrown);
        }
	});
}
