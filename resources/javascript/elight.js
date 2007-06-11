if(Elight == null) var Elight = {};
if(ElightResult == null) var ElightResult = function() {};

/* ------ elight ------ */

Elight.getSearchResults = function(data) {
	
	var container = $('elightResults');
	removeChildren(container);
	
	if(!data || data == null || data.length == 0) {
		Elight.addMessage("Sorry. No data was retrieved.", container);
		$('elightSearchButton').src = elight_search_uri;
		return;
	}	
	
	if(data.length == 1 && data[0].message != null) {
		Elight.addMessage(data[0].message, container);
		$('elightSearchButton').src = elight_search_uri;
		return;
	}
	
	for(var index=0; index<data.length; index++) {
		
		Elight.addResultEntry(data[index], container);
	}
	
	$('elightSearchButton').src = elight_search_uri;
}


Elight.addResultEntry = function(entry, container) {
	
	var result = new ElightResult();
	result.addTitle(entry.title);
	result.addContents(entry.contents);
	result.addUrl(entry.url);
	container.appendChild(result.getResultDOM());
}

Elight.addMessage = function(message, container) {
	var messsage_dom = document.createElement("DIV");
	messsage_dom.setAttribute("class", "elightMessage");
	messsage_dom.appendChild(document.createTextNode(message));
	container.appendChild(messsage_dom);
}

/* ------ elight ---(END)--- */

/* ------ elight result ------ */
ElightResult.prototype.addTitle = function(title) {
	this.title = title;
}

ElightResult.prototype.addContents = function(contents) {
	this.contents = contents;
}

ElightResult.prototype.addUrl = function(url) {
	this.url = url;
}

ElightResult.prototype.getGenericResultObject = function() {
	
	if(ElightResult.genericResultObject && ElightResult.genericResultObject != null) {

		return ElightResult.genericResultObject;
	}
	
	ElightResult.genericResultObject = {};
	ElightResult.genericResultObject.resultDIV = document.createElement('DIV');
	ElightResult.genericResultObject.resultDIV.setAttribute("class", "elightResult");
	
	ElightResult.genericResultObject.titleDIV = document.createElement('DIV');
	ElightResult.genericResultObject.titleDIV.setAttribute("class", "elightTitle");
	
	ElightResult.genericResultObject.contentsDIV = document.createElement('DIV');
	ElightResult.genericResultObject.contentsDIV.setAttribute("class", "elightContents");
	
	ElightResult.genericResultObject.urlDIV = document.createElement('DIV');
	ElightResult.genericResultObject.urlDIV.setAttribute("class", "elightUrl");
	
	return ElightResult.genericResultObject;
}

ElightResult.prototype.getResultDOM = function() {
	
	var gen_res_obj = this.getGenericResultObject();
	var result_dom = gen_res_obj.resultDIV.cloneNode(false);
	var title_dom = gen_res_obj.titleDIV.cloneNode(false);
	var contents_dom = gen_res_obj.contentsDIV.cloneNode(false);
	var url_dom = gen_res_obj.urlDIV.cloneNode(false);
	
	result_dom.appendChild(title_dom);
	result_dom.appendChild(contents_dom);
	result_dom.appendChild(url_dom);
	
	if(this.title && this.title != null) {
		
		if(this.url && this.url != null) {
			
			var link = document.createElement('a');
			link.setAttribute("href", this.url);
			link.appendChild(document.createTextNode(this.title));
			title_dom.appendChild(link);
		
		} else
			title_dom.appendChild(document.createTextNode(this.title));
	}
		
	if(this.contents && this.contents != null)
		insertNodesToContainer(this.contents, contents_dom);
		
	if(this.url && this.url != null)
		url_dom.appendChild(document.createTextNode(this.url));
	
	return result_dom;
}

/* ------ elight result --(END)---- */

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
		
		ElightSearchResults.search($('elightSearchInput').value, Elight.getSearchResults);
			
		e = new Event(e);
		
		elight_results_slide.state = elight_SLIDED_IN;
		elight_results_slide.slideIn();

		e.stop();
	});
});