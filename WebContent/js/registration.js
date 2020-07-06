$(document).ready(function(){
	
	$('#registerForm').submit( (event)=>{
		event.preventDefault();
		
		var username = $('#userName').val();
		var firstname = $('#firstName').val();
		var lastname = $('#lastName').val();
		var gender = $('#gender').val();
		var password = $('#password').val();
		var confirmPassword = $('#confirmPassword').val();
		
		if(username == "" || username == undefined || username == null){
			document.getElementById("alertUsername").innerHTML = "Niste uneli korisnicko ime";
		}else if(firstname == "" || firstname == undefined || firstname == null){
			document.getElementById("alertUsername").innerHTML = "";
			document.getElementById("alertFirstname").innerHTML = "Niste uneli Ime";
		}else if(lastname == "" || lastname == undefined || lastname == null){
			document.getElementById("alertUsername").innerHTML = "";
			document.getElementById("alertFirstname").innerHTML = "";
			document.getElementById("alertLastname").innerHTML = "Niste uneli prezime";
		}else if(gender == "" || gender == undefined || gender == null){
			document.getElementById("alertUsername").innerHTML = "";
			document.getElementById("alertFirstname").innerHTML = "";
			document.getElementById("alertLastname").innerHTML = "";
			document.getElementById("alertGender").innerHTML = "Niste uneli pol";
		}else if(password == "" || password == undefined || password == null){
			document.getElementById("alertUsername").innerHTML = "";
			document.getElementById("alertFirstname").innerHTML = "";
			document.getElementById("alertLastname").innerHTML = "";
			document.getElementById("alertGender").innerHTML = "";
			document.getElementById("alertPassword").innerHTML = "Niste uneli lozinku";
		}else if(confirmPassword == "" || confirmPassword == undefined || confirmPassword == null){
			document.getElementById("alertUsername").innerHTML = "";
			document.getElementById("alertFirstname").innerHTML = "";
			document.getElementById("alertLastname").innerHTML = "";
			document.getElementById("alertGender").innerHTML = "";
			document.getElementById("alertPassword").innerHTML = "";
			document.getElementById("alertPassword").innerHTML = "Niste uneli potvrdu lozinke";
		}else if(confirmPassword != password){
			document.getElementById("alertPassword").innerHTML = "Potvrda lozinke ne odgovara";
		}else {
			var obj = { "userName":$('#userName').val(), "firstName" : $('#firstName').val(),
					"lastName" : $('#lastName').val(), "gender" : $('#gender').val(),
					"password" : $('#password').val()};
			   
			console.log(JSON.stringify(obj));
			$.ajax({
		    	contentType: 'application/json',
		        url: 'rest/register',
		        type : 'POST',
		        data: JSON.stringify(obj),
		        success: function(response){
		        	alert('Uspesno ste se registrovali');
		        	window.location = './login.html';
		        }
		    });
		}
		
	});
})
