/**
 * 
 */
$(document).ready(function(){
	
	$('#vrPrijave').datepicker({
		format: "yyyy-mm-dd"
	});
	$('#vrOdjave').datepicker({
		format: "yyyy-mm-dd"
	});
	$('#apartmentForm').submit( (event)=>{
		event.preventDefault();
		
		var obj = {"tipSobe":$('#tipSobe').val(), "brojSoba" : $('#brojSoba').val(), "brojGostiju" : $('#brojGostiju').val(), "datePocetakVazenja" : $('#datePocetakVazenja').val(), 
				"krajPocetakVazenja" : $('#krajPocetakVazenja').val(), "domacin" : $('#domacin').val(),
				"cenaPoNoci" : $('#cenaPoNoci').val(), "vremeZaPrijavu" : $('#vremeZaPrijavu').val(), 
				"vremeZaOdjavu" : $('#vremeZaOdjavu').val(), "status" : $('#status').val()};
		   
		console.log(JSON.stringify(obj));
		$.ajax({
	    	contentType: 'application/json',
	        url: 'rest/apartman/dodaj',
	        type : 'POST',
	        data: JSON.stringify(obj),
	        success: function(response){
	        	alert('Uspesno dodat apartman.');
	        	console.log(response);
	        	
	        }
	    });
	});
})