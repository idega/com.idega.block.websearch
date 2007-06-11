var search_results = function(data) {
	
	console.log("calback..............");
	console.log("got: "+data);
	$('elightSearchButton').src = elight_search_uri;
	console.log("xx: "+data[0].url);
	console.log("message: "+data[0].message);
	console.log("title: "+data[0].title);
	console.log("content: "+data[0].contents);

}

window.addEvent('domready', function() {
	
	var elight_SLIDED_OUT 	=	1;
	var elight_SLIDED_IN	=	2;
	
	var elight_horizontal_slide = new Fx.Slide('elightInputText', {mode: 'horizontal', duration: 300});
	elight_horizontal_slide.hide();
	elight_horizontal_slide.state = elight_SLIDED_OUT;
//	elight_horizontal_slide.state = elight_SLIDED_IN;

	$('elightSearchButton').addEvent('click', function(e) {
		e = new Event(e);
		
		$('elightSearchInput').focus();
		
		if(elight_horizontal_slide.state == elight_SLIDED_IN) {
			
			elight_horizontal_slide.state = 1;
			elight_horizontal_slide.slideOut();
			
		} else {

			elight_horizontal_slide.state = elight_SLIDED_IN;
			elight_horizontal_slide.slideIn();
		}
		
		if(elight_results_slide.state == elight_SLIDED_IN)
			elight_results_slide.slideOut();

		e.stop();

	});


	
	var elight_results_slide = new Fx.Slide('elightResults', {mode: 'vertical'});
	elight_results_slide.hide();
	elight_results_slide.state = elight_SLIDED_OUT;
//	elight_results_slide.state = elight_SLIDED_IN;

	$('elightSearchInput').addEvent('keypress', function(e) {
		
		if(!isEnterEvent(e))
			return;
			
		$('elightSearchButton').src = elight_working_uri;
		
		ElightSearchResults.search($('elightSearchInput').value, search_results);
			
		e = new Event(e);
		
		elight_results_slide.state = elight_SLIDED_IN;
		elight_results_slide.slideIn();

		e.stop();
	});
});