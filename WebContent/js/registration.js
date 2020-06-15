$(document).ready(function(){
	
	$('#registerForm').submit( (event)=>{
		event.preventDefault();
		
		var obj = { "userName":$('#userName').val(), "firstName" : $('#firstName').val(), "lastName" : $('#lastName').val(), "gender" : $('#gender').val(), "password" : $('#password').val()};
		   
		console.log(JSON.stringify(obj));
		$.ajax({
	    	contentType: 'application/json',
	        url: 'rest/register',
	        type : 'POST',
	        data: JSON.stringify(obj),
	        success: function(response){
	        	alert('Uspesno ste se registrovali');
	        	console.log(response);
//	        	if(response==null){
//	        		alert('Ups, ima neka greska2');
//	        		document.getElementById("message").className = '';
//	        	}
	        }
	    });
	});
})
