$(document).ready(function(){
	
	$('#odjava').click(logout());
	
})

function logout(){
	return function(event){
		event.preventDefault();
		
		$.ajax({
			url : '../PocetniREST/rest/logout',
			type : 'GET',
			success : function(){
				alert('Vasa sesija je istekla!');
				window.location = './index.html';
			}
		});
	}
}

$(document).ready(function(){
	
	$('#sidebar').click(function(){
		
		document.getElementById("sidebar").classList.toggle("active");
	});
	
})

$(document).ready(function(){
	
	$('#dodajApartman').click(dodaj());
	
})
	
function dodaj(){
	return function(event){
		event.preventDefault();
		
		$.ajax({
			type : 'GET',
			success : function(){
				window.location = './dodajApartman.html';
			}
		});
	}
}


