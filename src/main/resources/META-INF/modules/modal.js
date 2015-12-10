(function(){
	define(["jquery", "bootstrap/modal"], function($, modal) {
		var showModal = function(id) {
			console.log($('#'+id)[0]);
			$('#'+id).modal('show');
		};
		return {showModal: showModal};
	});
}).call(this);