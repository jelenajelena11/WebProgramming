$(document).ready(function(){
	
	$('#odjava').click(logout());
	
/*	$('a[href = "#odjava"]').click(function(){

		$.ajax({
			url : '../PocetniREST/rest/logout',
			type : 'GET',
			success : function(){
				alert('Vasa sesija je istekla!');
				window.location = './index.html';
			}
		});
	});*/
	
	$('#podaci').click(prikaziPodatke());
	
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

function prikaziPodatke(){
	
	return function(event){
		event.preventDefault();
		
		$.ajax({
			url: 'rest/user/korisnik',
			type: 'GET',
			contentType : 'application/json',
			success : function(users){
				for(let user of users){
					console.log(user);
					ispisiMojePodatke(user);
				}
			}
		});
	}
	
}

function ispisiMojePodatke(user){
	
	let div = $('#podaciDiv');
	let div2 = $('<div height:2000px; margin-top: 300px;"></div>');
	//let userName = $('<h4>Username: </h4'+user.getUserName);
	//let ime = $('<h4>Ime: </h4>'+user.getFirstName);
	let tabela = $('<table><tr><th> Username: </th> <th>'+user.userName+' </th></tr><tr><th>Ime:</th><th>'+user.firstName+'</th></tr><tr><th> Prezime:</th><th>'+user.lastName+'</th></tr><tr><th>Pol: </th><th>'+user.gender+' </th></tr><tr><th> Lozinka: </th><th>'+user.password+'</th></tr></table>');
	let button = $('<button id="izmeniButton" style="background-color: #e7e7e7;"><a href="izmeni.html">Izmeni podatke</a></button>');
	
	div2.append(tabela).append(button);
	div.append(div2);
	
	
}