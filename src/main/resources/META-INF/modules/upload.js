(function(){
	define(["jquery", "bootstrap/modal"], function($, modal) {
		var closeModal = function(id) {
			$('#'+id, window.parent.document).find('.modal-header').text('Success');
			$('#'+id, window.parent.document).modal('toggle');
		};
		return {closeModal: closeModal};
	});
}).call(this);