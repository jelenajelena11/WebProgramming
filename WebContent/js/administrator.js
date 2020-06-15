$(document).ready(function(){
	
	document.getElementById('korisniciTabelaForma').style.visibility = "hidden"
	document.getElementById('domaciniTabelaForma').style.visibility = "hidden"
	
	$('#odjava').click(logout());
	
	$('#domaciniTableBTN').click(onDomaciniTableBTN());
	
	$('#korisniciTableBTN').click(onKorisniciTableBTN());
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

function onDomaciniTableBTN(){
	return function(event){
		event.preventDefault();
		
		if(!(document.getElementById('korisniciTabelaForma').style.visibility = "hidden")){
			document.getElementById('korisniciTabelaForma').style.visibility = "hidden"
		}
		document.getElementById('domaciniTabelaForma').style.visibility = "visible"
		getDomacini();
	}
}

function onKorisniciTableBTN(){
	return function(event){
		event.preventDefault();
		
		if(!(document.getElementById('domaciniTabelaForma').style.visibility = "hidden")){
			document.getElementById('domaciniTabelaForma').style.visibility = "hidden"
		}
		document.getElementById('korisniciTabelaForma').style.visibility = "visible"
		getKorisnike();
	}
}

function getDomacini(){
	
	$.ajax({
		url : '../PocetniREST/rest/user/domacin',
		type : 'GET',
		contentType: 'application/json',
		success : function(users){
			$('#domaciniTabelaForma tbody').html('');
			for(let user of users){
				ispisiUser(user, 'domacini');
			}
		},
		error : function(){
			alert('Ups, nesto ne valja u dobavljanju!');
		}
	});
}

function getKorisnike(){
	
	$.ajax({
		url : '../PocetniREST/rest/user/korisnik',
		type : 'GET',
		contentType: 'application/json',
		success : function(users){
			$('#korisniciTabelaForma tbody').html('');
			for(let user of users){
				ispisiUser(user, 'korisnici');
			}
		},
		error : function(){
			alert('Ups, nesto ne valja u dobavljanju!');
		}
	});
}
function ispisiUser(user, tabela){
	let tr = $('<tr></tr>');
	let username = $('<td>' + user.userName + '</td>');
	let firstname = $('<td>' + user.firstName + '</td>');
	let lastname = $('<td>' + user.lastName + '</td>');
	let email = $('<td>' + user.email + '</td>');
	let address = $('<td>' + user.address + '</td>');
	
	tr.append(username).append(firstname).append(lastname).append(email).append(address);
	$('#' + tabela + 'TabelaForma tbody').append(tr);
}