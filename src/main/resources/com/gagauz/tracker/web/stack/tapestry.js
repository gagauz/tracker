;(function($, T5){
	window.TapestryJQuery = new function() {
		this.updateZoneOnEvent = function(element, event, zone, url) {
			console.log('binding', event, 'on', element);
			$(element).bind(event, function() {
				console.log('event', event, 'triggered');
				$.ajax(url, {
					"success": function(json, status) {
		   				$('#'+zone).html(
		   					typeof(json["content"])!="undefined" ? json["content"] : json["zones"][zone]
		   				);
		   			},
		   			"error": function(xhr, status, error) {
		   				console.log(status, error);
		   			}
				});
			});
		}
	}
})(jQuery, Tapestry);