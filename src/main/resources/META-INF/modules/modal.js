(function(){
	define(["jquery", "bootstrap/modal"], function($, modal) {
		var showModal = function(id) {
			var $self = $('#'+id), 
				currentModal = window["currentModal"],
				show = function() {
					$self.modal('show');
				};
			
			
			console.log('showModal', currentModal);
			
			window['currentModal'] = $self;
			
			$self.on('hidden.bs.modal', function() {
				console.log('hidden.bs.modal');
				if (currentModal) {
					currentModal.current.modal('show');
				}
			});
			
			if (currentModal) {
				console.log('hide', currentModal);
				currentModal.on('hidden.bs.modal', show).modal('hide');
			} else {
				show();
			}
		};
		return {showModal: showModal};
	});
}).call(this);