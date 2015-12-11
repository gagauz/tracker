(function() {
	define(['jquery', 'draggable'], function($, d){
		var init = function(url) {
    		d.draggable(".drag", {
    			handle : ".drag-handle",
    			appendTo: "div.version-user-map",
    			snap: ".ticket-col",
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
    				if (ui.draggable.data("user-id") != $(this).data("user-id")) {
    					$(ui.draggable).detach().appendTo(this);
    					$.post(url, {
    							ticket: ui.draggable.data("ticket-id"),
    							user: $(this).data("user-id")
    						},
    						function() {
    							window.location = window.location;
    						} 
    					);
    				}
    			}
    	    });
		};
		return {init: init}
    });
}).call(this);