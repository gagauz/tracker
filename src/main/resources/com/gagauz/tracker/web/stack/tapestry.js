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
		   				if (json["scripts"]) {
		   					for (var i=json["scripts"].length-1; i>-1; i--) {
		   						var s = document.createElement('script');
		   						s.type = 'text/javascript';
		   						s.src = json["scripts"][i];
		   						if (i == 0) {
    		   						s.onreadystatechange = s.onload = function() {
    		   					        var state = s.readyState;
    		   					        if (!state || /loaded|complete/.test(state)) {
    		   					        	if (json["inits"]) {
    		   				   					for (var i = json["inits"].length-1; i > -1; i--) {
    		   				   						if (json["inits"][i]["evalScript"]) {
    		   				   							for (r in json["inits"][i]["evalScript"]) {
    		   				   								eval(json["inits"][i]["evalScript"][r]);
    		   											}
    		   				   						}
    		   				   					}
    		   				   				}
    		   					        }
    		   					    };
		   						}
		   						document.body.appendChild(s);
		   					}
		   				}
		   				
		   				if (json["stylesheets"]) {
		   					for (var i=json["stylesheets"].length-1; i>-1; i--) {
		   						var s = document.createElement('link');
		   						s.type = 'text/css';
		   						s.rel = 'stylesheet';
		   						s.href = json["stylesheets"][i].href;
		   						document.head.appendChild(s);
		   					}
		   				}
		   			},
		   			"error": function(xhr, status, error) {
		   				console.log(status, error);
		   			}
				});
			});
		}
		
		this.updateZoneOnEvent1 = function(eventName, element, zoneId, url) {
	        element = $(element)
	        var $zoneElement = zoneId == '^' ? $element.closest('.t-zone') : $('#' + zoneId);

	        if (!$zoneElement.length) {
	            Tapestry.error(
	                "Could not find zone element '#{zoneId}' to update on #{eventName} of element '#{elementId}'.",
	                {
	                    zoneId: zoneId,
	                    eventName: eventName,
	                    elementId: element.id
	                });
	            return;
	        }

	        if (element[0].tagName == "FORM") {

	            // Create the FEM if necessary.
	            element[0].addClassName(T5.PREVENT_SUBMISSION);

	            /*
				 * After the form is validated and prepared, this code will
				 * process the form submission via an Ajax call. The original
				 * submit event will have been cancelled.
				 */

	            element.on(T5.FORM_PROCESS_SUBMIT_EVENT, function() {
                    var zoneManager = T5.findZoneManager(element);
                    
                    if (!zoneManager)
                        return;
                    
                    var successHandler = function (transport) {
                        zoneManager.processReply(transport.responseJSON);
                    };
                    
                    element.sendAjaxRequest(url, {
                        parameters: {
                            "t:zoneid": zoneId
                        },
                        onSuccess: successHandler
                    });
	            });

	            return;
	        }

	        /* Otherwise, assume it's just an ordinary link or input field. */

	        element.on(eventName, function(event) {
	            element.trigger(T5.TRIGGER_ZONE_UPDATE_EVENT);
	        });

	        element.on(T5.TRIGGER_ZONE_UPDATE_EVENT, function() {

	            var zoneObject = T5.findZoneManager(element);

	            if (!zoneObject)
	                return;

	            /*
				 * A hack related to allowing a Select to perform an Ajax update
				 * of the page.
				 */

	            var parameters = {};

	            if (element.tagName == "SELECT" && element.value) {
	                parameters["t:selectvalue"] = element.value;
	            }

	            zoneObject.updateFromURL(url, parameters);
	        });
	    }
	}
})(jQuery, Tapestry);