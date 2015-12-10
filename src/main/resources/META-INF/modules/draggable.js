(function(){
	define(["jquery"], function($) {
		var draggable, droppable;
		
		draggable = function(selector, opts) {
        	$(selector).draggable(opts);
		};
		
		droppable = function(selector, opts) {
        	$(selector).droppable(opts);
		}
		
		return {
			draggable: draggable,
			droppable: droppable
		};
	});
}).call(this);