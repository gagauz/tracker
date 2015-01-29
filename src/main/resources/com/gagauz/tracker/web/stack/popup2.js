(function($, T5){
    var totalpop = 0,
    	progress = false;
    
    window.Popup = function($this, options) {
    	var _this = this;
    	$this.addClass('popup').removeClass('hide');
    	totalpop++;
    	
    	var opts = $.extend({
    			url    : false,
    			popupClose  : true,
    			popupClone  : false,
    			opacity: 0.4,
    			overlay: true,
    			duration: 200
    		}, $this.data(), options),
    		visible = $this.is(":visible"),
    		currentpop = totalpop;
    	
    	if (opts.overlay)
    		_this.overlay =  $('<div class="kvvoverlay" style="width:100%;padding:0;border:0;margin:0;top:0;left:0;display:none;"/>');
    	else
    		_this.overlay = $('<div style="display:none" class="dummy">')
    	
    	_this.temporary=$('<div style="display:none" class="dummy">');
    	_this.wrapper = $('<div class="kvvp" style="display:none;" />');
    
    	if(opts.popupClose)
    		_this.wrapper.append($('<i>',{"class":"icon close", "title":"Закрыть"})); 
    	if(opts.url) {
    		var iframe = $('<iframe scrolling="auto" style="width: 976px; height: 554px;" noresize="noresize" src="'+opts.url+'">');
    		iframe.load(_this.setSelfPosition);
    		_this.wrapper.append(iframe);
    	}
        if( opts.hasOwnProperty("popupClass"))
            _this.wrapper.addClass(opts["popupClass"]);
        _this.wrapper.addClass("left");
        
        $this[0].popup = _this;
    		
    	_this.showModal = function() {
    		
    		if (_this.wrapper.is(":visible")) {
    			_this.setSelfPosition();
    			return;
    		}
    		
    	    visible || $this.show();
    	    
    		if (opts.popupClone) {
    			$this = $this.clone(true);
	    	} else {
	    		$this = $this.before(_this.temporary).detach();
	    	}
    		
    		$this.addClass("left");
    		//Make it visible to define sizes
    		_this.wrapper.stop().css("z-index", 1002+currentpop*3);
    		_this.overlay.stop().css({"z-index": 1001+currentpop*3, background: "black", opacity: opts.opacity});
    		
    		_this.wrapper.append($this);
    		
    		_this.body()
    			.append(_this.overlay)
    			.append(_this.wrapper);
    		
    		$(window)
    			.bind('scroll resize', _this.setSelfPosition)
        		.bind('keydown', _this.observeEscapePress);
    		_this.wrapper.bind('resize', _this.setSelfPosition);
    		
    		$('.icon.close', _this.wrapper).bind('click', _this.removeModal);
    		
    		_this.setSelfPosition();
    		
    		if (opts.popupInstantly) {
    			opts.duration = 0;
    		}
    		
    		_this.overlay.fadeIn(opts.duration, function() {
    			_this.overlay.bind('click', _this.removeModal);
    		});
    		_this.wrapper.css('opacity', 0).show().animate({'opacity':1}, opts.duration, function() {
    			$this.triggerHandler('popupShow');
    		});
    	}
    	
    	_this.removeModal = function() {

    		_this.overlay.stop().fadeOut(300, function() {
    			_this.overlay.remove();
    		});
    		_this.wrapper.stop().fadeOut(300, function() {
    			hideTT();
    			$(window).unbind('scroll resize', _this.setSelfPosition)
    				.unbind('keydown', _this.observeEscapePress);
    			_this.wrapper.unbind('resize', _this.setSelfPosition);
    			
    			$('.icon.close', _this.wrapper).unbind('click', _this.removeModal);
    			_this.overlay.unbind('click', _this.removeModal);
    			$this.unbind('close', _this.removeModal).removeClass('popup');
    			$this.removeClass("left");
    			
    			if (!opts.popupClone) {
    				_this.temporary.after($this.detach()).remove();
    			}
    			_this.wrapper.remove();
    			visible || $this.hide();
    			$this.triggerHandler('popupHide');
    		});
    		
    	}
    
    	_this.observeEscapePress = function (e) {
    		if(e.keyCode == 27 || (e.DOM_VK_ESCAPE == 27 && e.which==0)){
    			if (currentpop>=totalpop) {
    				_this.removeModal();
    			}
    		}
    	}
    	_this.setSelfPosition = function () {
    		var wh = _this.wrapper.height(), ww = _this.wrapper.width(), top = $(window).scrollTop(), bh = $(document).height();
    		if (top + wh > bh - 80) {
    			top = bh - 80 - wh;
    		}
    		if (wh + 80 > $(window).height()) {
    		    _this.wrapper.css({"position": "absolute", "top": top+15+"px", "margin-top": 0});
    		    _this.overlay.css({"position": "absolute", "height": Math.max(_this.body().outerHeight(), wh+80)+"px"});
    		} else {
    			_this.wrapper.css({"position": "fixed", "top": "50%", "margin-top": Math.round(wh/-2)+"px"});
    			_this.overlay.css({"position": "fixed", "height": "100%"});
    		}
    		if (ww + 80 > $(window).width()) {
    			_this.wrapper.css({"left": 5, "margin-left": 0});
    		} else {
    			_this.wrapper.css({"left": "50%", "margin-left": Math.round(ww/-2)+"px"});
    		}
    		return _this;
    	}
    
    	if (opts.popupTimeout) {
        	setTimeout(function() {
        		_this.show()
        	}, opts.popupTimeout);
        } else if (opts.popupShow) {
        	_this.showModal();
        }
    	
    	return _this;
    }
    
    window.Popup.prototype.body = function(){return $('body');}
    
    $.fn.showPopup = function(options) {
    	if (this.length == 0)
    		return this;
    	if (!this[0].popup) {
    		new Popup(this, options);
    	}
    	
    	this[0].popup.showModal();
    	
    	return this;
    }
    
    $.fn.hidePopup = function(options) {
    	if (this.length == 0)
    		return this;
    	
    	if (!this[0].popup)
    		new Popup(this, options);
    	
    	this[0].popup.removeModal();
    	return this;
    }
})(jQuery, Tapestry);