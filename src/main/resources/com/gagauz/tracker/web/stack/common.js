jQuery.fn.tinyEditor = function(opts) {
	if (this.length && !this[0].TINY) {
   		this[0].TINY = new TINY('editor', {
        	id:this[0].id,
        	width: (this.parent().width()-4)+"px",
        	height: this.parent().height()+"px",
        	cssclass:'te',
        	controlclass:'tecontrol',
        	rowclass:'teheader',
        	dividerclass:'tedivider',
        	controls:[
        	      'bold','italic','underline','strikethrough','|','orderedlist','unorderedlist','|','outdent','indent','|','leftalign','centeralign','rightalign','blockjustify','|','unformat','|','image','link','unlink'
        	      ],
            footer:false,
            fonts:['Verdana','Arial','Georgia','Trebuchet MS'],
            xhtml:true,
            cssfile:'/layout/layout.css',
            bodyclass:'tiny',
            footerclass:'tefooter',
            toggle:{text:'source',activetext:'wysiwyg',cssclass:'toggle'},
            resize:{cssclass:'resize'}
    	});
	}
}