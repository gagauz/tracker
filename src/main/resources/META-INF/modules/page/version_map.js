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
    				
    				if (ui.draggable.data("target-id") != $(this).data("target-id")) {
    					var array = (new Function("return [" + $(this).data("allow-to") + "];"))();
    					
    					console.log(array);
						console.log($(this).data("target-id"));
						console.log(array.indexOf($(this).data("target-id")));
    					
    					if (array.indexOf($(this).data("target-id")) == -1) {
    						alert('x');
    						return;
    					}
    					$(ui.draggable).detach().appendTo(this);
    					$.post(url, {
    							ticket: ui.draggable.data("ticket-id"),
    							target: $(this).data("target-id")
    						},
    						function() {
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