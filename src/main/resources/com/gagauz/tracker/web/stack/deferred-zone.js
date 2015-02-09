;(function(w, $){
	DeferredZone = function(id, url) {
	    initialize : function(spec) {
	        this.element = $(spec.elementId);
	        this.listenerURI = spec.listenerURI;
	        $(this.element).getStorage().zoneId = spec.zoneId;
	        
	        if (spec.clientEvent) {
	            this.clientEvent = spec.clientEvent;
	            this.element.observe(this.clientEvent, this.updateZone.bindAsEventListener(this));
	        }
	    },
	    
	    updateZone : function() {
	        var zoneManager = Tapestry.findZoneManager(this.element);
	        
	        if (!zoneManager) {
	            return;
	        }

	        var listenerURIWithValue = this.listenerURI;
	        
	        if (this.element.value) {
	            var param = this.element.value;
	            if (param) {
	                listenerURIWithValue = addQueryStringParameter(listenerURIWithValue, 'param', param);
	            }
	        }
	        
	        zoneManager.updateFromURL(listenerURIWithValue);
	    }
	    
	}	
	
})(window, $);