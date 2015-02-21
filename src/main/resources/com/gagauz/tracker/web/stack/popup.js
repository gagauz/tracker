(function($, T5){
	
	function observeClick() {
		window.currentPopup && window.currentPopup.hidePopup();
    }
	
	function observeEscapePress(e) {
    	if (e.keyCode == 27 || (e.DOM_VK_ESCAPE == 27 && e.which == 0)) {
    		observeClick();
    	}
    }
	
	var Popup = function($this, specs) {
		if (this.popup) {
			return this;
		}

		var _this = this, $popupwrap = $('#popup-wrapper'), $overlay = $('#popup-overlay'), $sitewrap = $('#site-wrapper');
		
		_this._id_ = $this.attr('id');
		
		if (!window.popupArray) {
	    	window.popupArray = [];
	    	$(window).on('keydown', observeEscapePress);
	    }
		
		if (!$popupwrap.length) {
			$popupwrap = $('<div id="popup-wrapper" style="display:none;position:fixed;top:0;left:0;width:100%;height:100%;min-height:100%;overflow:auto;z-index:999;">');
			$('body').append($popupwrap);
		}
		
    	if (!$overlay.length) {
    		var $overlay = $('<div id="popup-overlay" style="display:none;position:fixed;top:0;left:0;width:100%;height:100%;z-index:1000;">');
    		$popupwrap.append($overlay);
    	}

	    specs = $.extend({
	        popupCloseButton : true,
	        popupClone : false,
	        popupShadowed : true
	    }, specs, $this.data());
	    
	    _this.visible = $this.is(":visible");
    	_this.placeholder = specs.popupClone ? false : $('<div style="display:none;">');
    	
    	_this.wrapper = $('<div class="popup-window" style="display:block;opacity:0;z-index:1001;position:absolute;">');
    	_this.wrapper.click(function(e) {
    		e && e.stopPropagation();
    	});
    	if (specs.popupClass) {
    		_this.wrapper.addClass(specs.popupClass);
    	}
    	
    	_this.closeBtn = $('<a>', {"href": "javascript:void(0)", "class": "popup-close"}).text("Закрыть");
	    
	    
	    _this.showPopup = function() {
	    	if (_this.wrapper.is(':visible')) {
	    		_this.setSelfPosition();
	    		return;
	    	}
	    	var content;
	    	if (specs.popupUrl) {
	    		content = $('<iframe scrolling="auto" style="width: 900px; height: 554px;" noresize="noresize" src="'+specs.popupUrl+'" />');
	    		content.load(_this.setSelfPosition);
	    	} else if (specs.popupClone) {
	    		content = $this.clone(true).off();
	    	} else {
	    		content = $this.before(_this.placeholder).detach();
	    	}
	    	    	
	    	if (specs.popupCloseButton) {
	    		_this.closeBtn.click(_this.hidePopup);
	    		_this.wrapper.append(_this.closeBtn);
	    	}
	    	
	    	_this.wrapper.append(content.show());
	    	
	    	$popupwrap.append(_this.wrapper);
	    	
	    	$(window).bind('resize', _this.setSelfPosition);
	    	
	    	$this.bind('resize', _this.setSelfPosition);
	    	
	    	_this.push();
			_this.wrapper.css('opacity', 0).show();
			_this.setSelfPosition();
			_this.wrapper.animate({'opacity':1}, 300, function() {
				$this.trigger('popupShow', specs);
			});
	    }
	    
	    _this.hidePopup = function(e) {
	    	if (e)
	    		e.stopPropagation();
	    	_this.wrapper.fadeOut(300, function() {
	    		_this.closeBtn.unbind('click');
	    		$this.unbind('resize', _this.setSelfPosition);
	    		$(window).unbind('resize', _this.setSelfPosition);
	    		$this.trigger('popupHide');
	    		if (_this.placeholder) {
	    			_this.placeholder.after($this.detach());
	    			_this.visible || $this.hide();
	    			_this.placeholder.remove()
	    		}
	    		_this.closeBtn.remove();
	    		_this.wrapper.empty().remove();
	    	});
	    	_this.pop();
	    }
	    
	    _this.push = function() {
	    	if (window.currentPopup) {
	    		window.currentPopup.wrapper.fadeOut(300);
	    		_this.parent = window.currentPopup;
	    		_this.parent.child = _this;
	    	} else if (specs.popupShadowed) {
		    	$popupwrap.show();
	    		$overlay.click(observeClick).fadeIn(200, function() {
	    			$overlay.css("filter", "progid:DXImageTransform.Microsoft.gradient(startColorstr=#66000000,endColorstr=#66000000)");
	    		});
	    	}
	    	window.currentPopup = _this;
	    }
	    
	    _this.pop = function() {
	    	if (_this.child) {
	    		_this.child.parent = undefined;
	    	}
	    	if (_this.parent) {
	    		window.currentPopup = _this.parent;
	    		_this.parent.wrapper.fadeIn(300);
	    		_this.parent = undefined;
	    	} else if (window.currentPopup == _this) {
	    	    try{
	    	    	delete window.currentPopup;
	    	    } catch(e){
	    	    	window["currentPopup"] = undefined; 
	    	    }
	    	}
	    	if (!window.currentPopup) {
        		$overlay.unbind('click', observeClick).css({
        			"filter": "progid:DXImageTransform.Microsoft.Alpha(Opacity=40)"}).fadeOut(300, function() {
        			$overlay.css("filter", "progid:DXImageTransform.Microsoft.gradient(startColorstr=#66000000,endColorstr=#66000000)");
        			$popupwrap.hide();
        		});
	    	}
	    }
	    
	    _this.setSelfPosition = function() {
	    	var wh = _this.wrapper.height(), ww = _this.wrapper.width();
	    	if (specs.popupFullscreen) {
	    		ww = $popupwrap.width();
	    		wh = $popupwrap.height();
	    	}
	    	
	    	if (specs.popupFullscreen) {
	    		_this.wrapper.css({"position": "fixed", "top": "0", "margin-top": 0, "width": "100%", "height": "100%"});
    			$overlay.css({"position": "fixed", "width": "100%", "height": "100%"});
	    	} else { 
    	    	if (wh > $popupwrap.height()) {
        			_this.wrapper.css({"position": "absolute", "top": "10px", "margin-top": 0});
        			$overlay.css({"position": "absolute", "height": Math.max($popupwrap.height(), wh+80)+"px"});
        		} else {
        			_this.wrapper.css({"position": "fixed", "top": "50%", "margin-top": Math.round(wh/-2)+"px"});
        			$overlay.css({"position": "fixed", "height": "100%"});
        		}
        		if (ww > $popupwrap.width()) {
        			_this.wrapper.css({"left": "5px", "margin-left": 0});
        		} else {
        			_this.wrapper.css({"left": "50%", "margin-left": Math.round(ww/-2)+"px"});
        		}
	    	}
    		
    		return _this;
    	}
	    
	    $this[0].popup = _this;
	    
	    if (specs.popupTimeout) {
	    	setTimeout(_this.showPopup, specs.popupTimeout);
	    } else if (specs.popupShow) {
	    	_this.showPopup();
	    }
	    
	    return _this;
	}
	
	window.Popup = Popup;
	
    $.fn.showPopup = function(options) {
    	if (this.length == 0)
    		return;
    	if (this[0].popup) {
			this[0].popup.showPopup();
			return this;
    	}
    	new Popup(this, options).showPopup();
    	return this;
    }

	$.fn.hidePopup = function(options) {
		if (this.length == 0)
    		return;
	    if(this[0].popup) {
	    	setTimeout(this[0].popup.hidePopup, 10);
	    }
	}
	T5.ElementEffect.popup = function(e) {
//	$.effects.popup = function(e) {
		$(e).showPopup();
		//console.log('trigger', T5.ZONE_UPDATED_EVENT, ' on ', $(e));
		$(e).trigger(T5.ZONE_UPDATED_EVENT);
	}
})(jQuery, Tapestry);