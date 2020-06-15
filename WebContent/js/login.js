$(document).ready(function(){
	
	$('#loginForm').submit( (event)=>{
		event.preventDefault();
		
		//localStorage.clear();
		var userName = $('#loginUserName').val();
		let password = $('#loginPassword').val();
		//localStorage.setItem("userName", userName);
		//console.log(localStorage.getItem("userName"));
		
		var obj = {"userName":userName, "password" : password};
		console.log(obj);
		
		$.ajax({
        	contentType: 'application/json',
            url: '../PocetniREST/rest/login',
            type : 'POST',
            data: JSON.stringify(obj),
            success: function(response){
            	if(response==null){
            		console.log('NULL');
            		alert('Pogresno Korisnicko Ime ili Lozinka.');
            		//document.getElementById("messageLogin").className = '';
            	}else{
            		console.log('Success in login!');
            		
            		if(response.uloga == 0){
            			window.location = './korisnik.html';
            		}else if(response.uloga == 1){
            			window.location = './administrator.html';
            		}else if(response.uloga == 2){
            			window.location = './agent.html';
            		}
            		
            		/*
            		if(response.role=="admin"){
            			localStorage.setItem("admin", "true");
            		}
            		localStorage.setItem("blokiran", response.blokiran);
            		 */
            		
            		//window.location.href = "findApartment.html";
            	}

              }
          });
	  });
	
	
//	$("#btnRegister").click(function(){
//    	
//    	var obj = { "userName":$('#userName').val(), "firstName" : $('#firstName').val(), "lastName" : $('#lastName').val(), "gender" : $('#gender').val(), "password" : $('#password').val(), "confirmPassword" : $('#confirmPassword').val()};
//    	var userName = $('#userName').val();
//    	
//    	$.ajax({
//    		type:'GET',
//    		url:'../PocetniREST/rest/users/allUsers',
//    		success:function(users){
//    			$.each(users, function(i, user){
//    				if(user.userName==userName){
//    					document.getElementById("message").classList.remove("hidden");
//    				}				
//    			});
//    		}
//    	});
//    	
//        console.log(obj);
//        $.ajax({
//        	contentType: 'application/json',
//            url: '../PocetniREST/rest/users/registerUser',
//            type : 'POST',
//            data: JSON.stringify(obj),
//            success: function(response){
//            	if(response==null){
//            		console.log('NULL');
//            		document.getElementById("message").className = '';
//            	}
//
//            }
//        });     
//      }); 
})