var height = 0;
var layout = 'masonry';

function infinityWindowListener() {
    if(($(window).scrollTop() + $(window).height() >= ($(document).height() - 50))) {
    	disableInfitiyScroll();
        loadNewContent();
    }
}

function isActive(componentId) {
	var component = document.getElementById(componentId);
	return component.classList.contains('active');
}

function enableInfinityScroll() {			
    $(window).bind( "scroll", infinityWindowListener );
}

function display(componentId, show) {
	var component = document.getElementById(componentId);
	if(show) {
		component.style.display = 'block';
	} else {
		component.style.display = 'none';
	}
}

function disableInfitiyScroll() {
    $(window).unbind( "scroll", infinityWindowListener );
}

$(document).ready(function() {
	$('#container').masonry({
		columnWidth: '.grid-sizer',
		itemSelector: '.grid-item',
		percentPosition: true
	});
    // Add active class to the current button (highlight it)
    var navbar = document.getElementById('content-navbar');         
    activeToggle = function() {
        var current = navbar.getElementsByClassName('active');
        if(current.length > 0) {
           	current[0].classList.remove('active');
        }
        this.classList.add('active');
    }
    var btns = navbar.getElementsByTagName('li');
   	for (var i = 0; i < btns.length; i++) {
    	btns[i].addEventListener('click', activeToggle);
   	}
   	aboutToggle = function() {
   		contentAbout.classList.toggle('active');
    }
    var contentAbout = document.getElementById('content-about');
   	contentAbout.addEventListener('click', aboutToggle);
   	refresh();
   	//enableInfinityScroll();
});

function cleanInputText() {
	searchInputQuery = '';
}

function fixLayout() {
	$(container).masonry('layout');
}

function handleLoadStart() {
    loadNewContent(); 
}

function enableLoadMore(enable) {
	document.getElementById('loadMore').style.display = enable;
}

function layoutItems(l) {
	if(layout != l) {
    	var gridItems = document.getElementsByClassName('grid-item');
    	for(var i = 0; i < gridItems.length; i++) {
    	    var gridItem = gridItems[i];
    		gridItem.classList.toggle('grid-item-large');
    	}	    	
    	$('#container').masonry('layout');
    	layout = l;
	}
}

function handleLoadStop() {
	var $items = $('#newContent > div');
	if(layout == 'line') {
		$items.addClass('grid-item-large');
	}
	$('#container').append($items).masonry( 'appended', $items );
	$('#container').masonry('layout');
	if($items.length == 10) {
		enableLoadMore('inline');
    	//enableInfinityScroll();
    } else {
    	enableLoadMore('none');
    }
}

function validKeyEntry(keycode) {
    var valid = (keycode == 8) || // backspace
        (keycode > 47 &&  keycode < 58)   || // number keys
        keycode == 32 || keycode == 13   || // spacebar &amp; return key(s) (if you want to allow carriage returns)
        (keycode > 64 &&  keycode < 91)   || // letter keys
        (keycode > 95 && keycode < 112)  || // numpad keys
        (keycode > 185 && keycode < 193) || // ;=,-./` (in order)
        (keycode > 218 && keycode < 223);   // [\]' (in order)		
    return valid;
}

function refresh() {
	var elements = $('#container').masonry('getItemElements');
	if(elements.length > 0) {
	    $('#container').remove(".grid-item");
    	$('#container').masonry( 'remove', elements ).masonry('layout');
    }
	resetPage();
	loadContent();
}

function equal(str1, str2) {
	return str1 == str2;
}

function searchKeyUp(event) {
	if(validKeyEntry(event.keyCode)) {
		refresh();
		updateSearchBar();
	}
}