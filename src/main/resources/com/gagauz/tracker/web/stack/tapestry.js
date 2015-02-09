;(function($, T5, $$){
	window.TapestryJQuery = new function() {
			
		this.updateZoneOnEvent = function(element, event, zone, url) {
			console.log('binding', event, 'on', element, zone, url);
			
			zone = $$(zone);
			
			var zoneObject = Tapestry.findZoneManager(zone);
			if (!zoneObject) {
				zoneObject = new Tapestry.ZoneManager({element: zone, parameters:{}});
			}

			$$(element).fire(Tapestry.TRIGGER_ZONE_UPDATE_EVENT);
			$(element).on(event, function(){
				zoneObject.updateFromURL(url, {});
			});
			
			return;
			
			function each(a, f) {
				var i, l = a.length;
				for (i=0; i<l; i++) {
					f(a[i], i, l);
				}
			}
			
			function init(inits) {
				inits && each(inits, function(v) {
					v.zone && v.zone.each(function(v){
						T5.Initializer.zone(v);
					});
					v.linkZone && each(v.linkZone, function(v){
						T5.Initializer.linkZone(v);
					});
					v.evalScript && each(v.evalScript, function(v){
						eval(v);
					});
				})
			}

			$(element).on(event, function() {
				console.log('event', event, 'triggered');
				$.ajax(url, {
					"success": function(json, status) {
		   				$('#'+zone).html(
		   					typeof(json.content)!="undefined" ? json.content : json.zones[zone]
		   				);
		   				if (json.scripts) {
		   					each(json.scripts, function(v, i, l) { 
		   						var s = document.createElement('script');
		   						s.type = 'text/javascript';
		   						s.src = v;
		   						if (i == l - 1) {
    		   						s.onreadystatechange = s.onload = function() {
    		   					        var state = s.readyState;
    		   					        if (!state || /loaded|complete/.test(state)) {
    		   					        	if (json.inits) {
    		   					        		init(json.inits);	
    		   				   				}
    		   					        }
    		   					    };
		   						}
		   						document.body.appendChild(s);
		   					});
		   				} else {
		   					init(json.inits);
		   				}
		   				json.stylesheets && each(json.stylesheets, function(v) { 
	   						var s = document.createElement('link');
	   						s.type = 'text/css';
	   						s.rel = 'stylesheet';
	   						s.href = v.href;
	   						document.head.appendChild(s);
		   				});
		   			},
		   			"error": function(xhr, status, error) {
		   				console.log(status, error);
		   			}
				})
			})
		}
	}
})(jQuery, Tapestry, $);