jQuery.fn.tinyEditor = function(opts) {
	var $ = this;
    new TINY.editor.edit('editor', {
    	id:$[0].id,
    	width: "100%",
    	cssclass:'te',
    	controlclass:'tecontrol',
    	rowclass:'teheader',
    	dividerclass:'tedivider',
    	controls:[
    	      'bold','italic','underline','strikethrough','|','subscript','superscript','|','orderedlist','unorderedlist','|','outdent','indent','|','leftalign','centeralign','rightalign','blockjustify','|','unformat','|','image','link','unlink'
    	      ],
        footer:true,
        fonts:['Verdana','Arial','Georgia','Trebuchet MS'],
        xhtml:true,
        cssfile:'/static/js/jquery/tinyeditor/style.css',
        bodyid:'editor',
        footerclass:'tefooter',
        toggle:{text:'source',activetext:'wysiwyg',cssclass:'toggle'},
        resize:{cssclass:'resize'}
	});
}