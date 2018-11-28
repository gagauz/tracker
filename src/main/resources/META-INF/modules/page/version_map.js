(function() {
	define(['jquery', 'draggable'], function($, d){
		var init = function(url) {
    		d.draggable(".drag", {
    			containment : '.drag-container',
    			handle : ".drag-handle",
    			appendTo: "div.version-map",
    			snap: false,
    			helper: "clone",
    			start: function(){
    	        	$(this).hide()
    		    },
    		    stop: function(){
    		        $(this).show()
    		    }
    		});
    		d.droppable(".ticket-col", {
    			hoverClass: "ui-state-hover",
    			drop: function(event, ui) {
    				
    				if (ui.draggable.data("col-id") != $(this).data("col-id")) {
    					var array = (new Function("return [" + ui.draggable.data("target-col-ids") + "];"))();
    					
    					if (array.indexOf($(this).data("col-id")) == -1) {
    						return;
    					}
    					var $this = this;
    					
    					$.post(url, {
    							ticket: ui.draggable.data("ticket-id"),
    							target: $(this).data("col-id")
    						},
    						function() {
    							$(ui.draggable).detach().appendTo($this);
    							//window.location = window.location;
    						} 
    					);
    				}
    			}
    	    });
		};
		return {init: init}
    });
}).call(this);