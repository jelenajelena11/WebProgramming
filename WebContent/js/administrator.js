$(document).ready(function(){

	$('#odjava').click(logout());
	$('#pregledKorisnika').click(prikaziKorisnike());
	$('#pregledDomacina').click(prikaziDomacine());
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

function prikaziKorisnike(){
	
	return function(event){
		event.preventDefault();

		$('#domaciniDIV').html('');
		$('#korisniciDIV').html('');
		
		$.ajax({
			url: 'rest/user/korisnik',
			type: 'GET',
			contentType : 'application/json',
			success : function(users){
				for(let user of users){
					console.log(user);
					ispisiKorisnike(user);
				}
			}
		});
	}
}

function prikaziDomacine(){
	
	return function(event){
		event.preventDefault();

		$('#domaciniDIV').html('');
		$('#korisniciDIV').html('');
		
		$.ajax({
			url: 'rest/user/domacin',
			type: 'GET',
			contentType : 'application/json',
			success : function(users){
				for(let user of users){
					console.log(user);
					ispisiDomacine(user);
				}
			}
		});
	}
}

function ispisiKorisnike(user){
	
	let div = $('#korisniciDIV');
	let div2 = $('<div height:2000px; margin-top: 300px;"></div>');
	//let userName = $('<h4>Username: </h4'+user.getUserName);
	//let ime = $('<h4>Ime: </h4>'+user.getFirstName);
	let naziv = $('<h3>Pregled svih korisnika:</h3>')
	let tabela = $('<table><tr><th> Username: </th> <th>'+user.userName+' </th></tr><tr><th>Ime:</th><th>'+user.firstName+'</th></tr><tr><th> Prezime:</th><th>'+user.lastName+'</th></tr><tr><th>Pol: </th><th>'+user.gender+' </th></tr><tr><th> Lozinka: </th><th>'+user.password+'</th></tr></table>');
	
	div2.append(naziv).append(tabela);
	div.append(div2);
}

function ispisiDomacine(user){
	
	let div = $('#domaciniDIV');
	let div2 = $('<div height:2000px; margin-top: 300px;"></div>');
	//let userName = $('<h4>Username: </h4'+user.getUserName);
	//let ime = $('<h4>Ime: </h4>'+user.getFirstName);
	let naziv = $('<h3>Pregled svih domacina:</h3>')
	let tabela = $('<table><tr><th> Username: </th> <th>'+user.userName+' </th></tr><tr><th>Ime:</th><th>'+user.firstName+'</th></tr><tr><th> Prezime:</th><th>'+user.lastName+'</th></tr><tr><th>Pol: </th><th>'+user.gender+' </th></tr><tr><th> Lozinka: </th><th>'+user.password+'</th></tr></table>');
	
	div2.append(naziv).append(tabela);
	div.append(div2);
}
