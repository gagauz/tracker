(function(){
	define(["jquery", "bootstrap/modal"], function($, modal) {
		var showModal = function(id) {
			var $self = $('#'+id), currentModal = window["currentModal"];
			console.log('showModal', currentModal);
			window["currentModal"] = {
					current: $self,
					lastModal: (currentModal ? currentModal.current : null) 
			};
			if (currentModal && currentModal.current) {
				console.log('hide', currentModal);
				currentModal.current.modal('hide');
			}
			
			$self.on('hidden.bs.modal', function() {
				console.log('hidden.bs.modal');
				var lastModal = window["currentModal"].lastModal;
				if (lastModal) {
					lastModal.modal('show');
				}
			}).modal('show');
		};
		return {showModal: showModal};
	});
}).call(this);