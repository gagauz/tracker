(function() {
  define(["t5/core/dom", "t5/core/ajax", "underscore", "jquery", "t5/core/utils", "t5/core/typeahead"], function(dom, ajax, _, $, arg) {
    var exports, extendURL, init;
    extendURL = arg.extendURL;
    init = function(spec) {
      var $field, $field2, dataset, engine;
      $field = $(document.getElementById(spec.id));
      $field2 = $(document.getElementById(spec.id2));
      engine = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.whitespace,
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        limit: spec.limit,
        remote: {
          url: spec.url,
          replace: function(uri, query) {
            return extendURL(uri, {
              "t:input": query
            });
          },
          filter: function(response) {
            return response.matches;
          }
        }
      });
      engine.initialize();
      
      var onselect = function(e, raw){
      	$field2.val(raw[spec.displayId]);
      };

      $field.on('typeahead:selected', onselect)
      .typeahead({
	        minLength: spec.minChars,
	        highlight: true
	      },{
	      	name: spec.id,
	        display: spec.displayName,
	      	source: engine.ttAdapter()
	      });
      
      $field.prev(".tt-hint").removeAttr("data-validation data-optionality data-required-message");
    };
    return exports = init;
  });

}).call(this);