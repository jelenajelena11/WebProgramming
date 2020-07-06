$(document).ready(function(){

	document.getElementById('searchDiv').style.visibility = "hidden";
	document.getElementById('sortirajRezervacije').style.visibility = "hidden";
	document.getElementById('statusRezervacije').style.visibility = "hidden";
	
	$('#odjava').click(logout());
	$('#pregledKorisnika').click(prikaziKorisnike());
	$('#pregledDomacina').click(prikaziDomacine());
	
	$('#apartmani').click(prikaziApartmane());
	$('#rezervacije').click(prikaziRezervacije());
	
	$('#pretrazi').click(provjeriPretragu());
	
	//*************************************************************
	//Datepicker
	$('#inputDolazak').datepicker({
		format: "yyyy-mm-dd"
	});
	$('#inputOdlazak').datepicker({
		format: "yyyy-mm-dd"
	});
	//**************************************************************
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
		$('#podaciDiv').html('');
		document.getElementById('searchDiv').style.visibility = "hidden";
		document.getElementById('sortirajRezervacije').style.visibility = "hidden";
		document.getElementById('statusRezervacije').style.visibility = "hidden";
		
		$.ajax({
			url: 'rest/user/korisnik',
			type: 'GET',
			contentType : 'application/json',
			success : function(users){
				//for(let user of users){
					ispisiKorisnike(users);
				//}
			}
		});
	}
}

function prikaziDomacine(){
	
	return function(event){
		event.preventDefault();

		$('#domaciniDIV').html('');
		$('#korisniciDIV').html('');
		$('#podaciDiv').html('');
		document.getElementById('searchDiv').style.visibility = "hidden";
		document.getElementById('sortirajRezervacije').style.visibility = "hidden";
		document.getElementById('statusRezervacije').style.visibility = "hidden";
		
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

function ispisiKorisnike(users){
	
//	let div = $('#korisniciDIV');
//	let div2 = $('<div height:2000px; margin-top: 300px;"></div>');
//	//let userName = $('<h4>Username: </h4'+user.getUserName);
//	//let ime = $('<h4>Ime: </h4>'+user.getFirstName);
//	let naziv = $('<h3>Pregled svih korisnika:</h3>')
//	let tabela = $('<table><tr><th> Username: </th> <th>'+user.userName+' </th></tr><tr><th>Ime:</th><th>'+user.firstName+'</th></tr><tr><th> Prezime:</th><th>'+user.lastName+'</th></tr><tr><th>Pol: </th><th>'+user.gender+' </th></tr><tr><th> Lozinka: </th><th>'+user.password+'</th></tr></table>');
//	
//	div2.append(naziv).append(tabela);
//	div.append(div2);
	let div = $('#korisniciDIV');
	let div2 = $('<div height:2000px; margin-top: 300px;"></div>');
	//let userName = $('<h4>Username: </h4'+user.getUserName);
	//let ime = $('<h4>Ime: </h4>'+user.getFirstName);
	let pretraga = ispisiPretragu();
	let naziv = $('<h3>Pregled svih korisnika:</h3>')
	//let tabela = $('<table><tr><th> Username: </th> <th>'+user.userName+' </th></tr><tr><th>Ime:</th><th>'+user.firstName+'</th></tr><tr><th> Prezime:</th><th>'+user.lastName+'</th></tr><tr><th>Pol: </th><th>'+user.gender+' </th></tr><tr><th> Lozinka: </th><th>'+user.password+'</th></tr></table>');
	let tabela = $('<table border="1"><th>Korisnicko Ime</th><th>Ime</th><th>Prezime</th><th>Email</th><th>Adresa</th> </table>');
	for(let user of users){
		let tr = $('<tr></tr>');
		let username = $('<td>' + user.userName + '</td>');
		let ime = $('<td>' + user.firstName + '</td>');
		let prezime = $('<td>' + user.lastName + '</td>');
		let email = $('<td>' + user.email + '</td>');
		let adresa = $('<td>' + user.address + '</td>');
		tr.append(username).append(ime).append(prezime).append(email).append(adresa);
		tabela.append(tr);
	}
	
	div2.append(naziv).append(pretraga).append(tabela);
	div.append(div2);
}

function ispisiPretragu(){
	var div = $('<div><div>');
	let pUsername = $('<p>Unesi korisnicko ime:</p>');
	let inputUsername = $('<input id="korisnickoIme" type="text" placeholder="korisnicko ime"/>');
	
	let pImee = $('<p>Unesi ime:</p>');
	let inputIme = $('<input id="ime" type="text" placeholder="ime"/>');
	let pPrezimee = $('<p>Unesi prezime:</p>');
	let inputPrezime = $('<input id="prezime" type="text" placeholder="prezime"/>');
	
	let pPol = $('<p>Unesi pol:</p>');
	let inputMusko = $('<input type="radio" name="pol" id="m">M<br>');
	let inputZensko = $('<input type="radio" name="pol" id="f">F<br>');
	
	let pUloga = $('<p>Unesi ulogu:</p>');
	let inputGost = $('<input type="radio" name="uloga" id="gost">Gost<br>');
	let inputDomacin = $('<input type="radio" name="uloga" id="domacin">Domacin<br>');
	
	let button = $('<br><button>Pretrazi</button>');
	
	button.on('click', function(event){
		var usernameText = document.getElementById('korisnickoIme').value;
		var imeText = document.getElementById('ime').value;
		var prezimeText = document.getElementById('prezime').value;
		var mElem = document.getElementById('m').checked;
		var fElem = document.getElementById('f').checked;
		var pol = "";
		if(mElem){
			pol = "M"
		}else if(fElem){
			pol = "F"
		}else{
			pol = "";
		}
		
		var gostElem = document.getElementById('gost').checked;
		var domacinElem = document.getElementById('domacin').checked;
		var uloga = "";
		if(gostElem){
			uloga = "0"
		}else if(domacinElem){
			uloga = "2"
		}else{
			uloga = "";
		}
		
		pretraziGoste(usernameText, imeText, prezimeText, pol, uloga);
	});
	
	div.append(pUsername).append(inputUsername).append(pImee).append(inputIme).append(pPrezimee).append(inputPrezime).append(pPol).append(inputMusko).append(inputZensko).append(pUloga).append(inputGost).append(inputDomacin).append(button);
	return div;
}

function pretraziGoste(usernameText, imeText, prezimeText, pol, uloga){
	$('#korisniciDIV').html('');
	
	var obj = {"username": usernameText, "ime": imeText, "prezime": prezimeText, "pol" : pol,
						"uloga": uloga};
	
	$.ajax({
		url: 'rest/user/administrator/korisnik/search',
		type: 'POST',
		contentType : 'application/json',
		data: JSON.stringify(obj),
		success : function(response){
			//for(let res of response){
			ispisiKorisnike(response);
			//}
		},
		error : function(response){
			console.log('Ups, nesto je poslo po zlu prilikom dobavljanja vremena');
		}
	});
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

//******************************************************************************************
//Prikaz apartmana
function prikaziApartmane(){
	return function(event){
		event.preventDefault();
		
		$('#domaciniDIV').html('');
		$('#korisniciDIV').html('');
		$('#podaciDiv').html('');
		document.getElementById('searchDiv').style.visibility = "visible";
		document.getElementById('sortirajRezervacije').style.visibility = "hidden";
		document.getElementById('statusRezervacije').style.visibility = "hidden";
		
		$.ajax({
			url: 'rest/apartman',
			type: 'GET',
			contentType : 'application/json',
			success : function(result){
				for(let res of result){
					console.log(res);
					ispisiApartmane(res);
				}
			},
			error : function(){
				alert('Ups, nije vratio apartmane');
			}
		});
	}
}

//Sam ispisi
function ispisiApartmane(apartman){
	let divOrigin = $('#podaciDiv');
	let div = $('<div style="border-style: solid; border-width: medium; margin-top: 20px; background-color: rgb(190, 188, 255);"></div>');
	
	let podaci = ispisiPodatkeApartmanaDIV(apartman);
	let slika = ispisiSlikuDIV(apartman);
	let datumVazenja = ispisiTerminDatuma(apartman);
	let lokacija = ispisiLokaciju(apartman);
	let editovanje = editovanjeApartmana(apartman);
	let button = ispisiButton(apartman);
	let obrisi = ispisiButtonObrisi(apartman);
	let aktivnost = ispisiButtonAktivnost(apartman);
	let komentari = ispisiButtonKomentariApartman(apartman);
	let sviKomentari = prikazKomentara(apartman);
	//let button = ispisiButton(apartman);
	//let rezervacija = ispisiRezervacijaZakazivanje(apartman);
	
	div.append(podaci).append(datumVazenja).append(slika).append(lokacija).append(obrisi).append(aktivnost).append(editovanje).append(button).append(sviKomentari).append(komentari);//.append(rezervacija).append(button);
	divOrigin.append(div);
}

function ispisiPodatkeApartmanaDIV(apartman){

	let podaci = $('<div style="margin-left: 20px; margin-right: 20px;background-color: aqua;"></div>');
	
	let tipSobeApartmana = provjeriApartman(apartman);
	let tipSobe = $('<h2> <i> Apartman: </i>' + tipSobeApartmana + '</h2>');
	let brojSoba = $('<h2> <i> Broj Soba: </i>' + apartman.brojSoba +'</h2>');
	let brojGostiju = $('<h2> <i> Broj Gostiju: </i>' + apartman.brojGostiju +'</h2>');
	let cenaPoNoci = $('<h2> <i> Cena po noci: </i>' + apartman.cenaPoNoci +' <b> <i>$</i> </b></h2>');
	
	podaci.append(tipSobe).append(brojSoba).append(brojGostiju).append(cenaPoNoci);
	return podaci;
}

function ispisiTerminDatuma(apartman){
	
	let datum = $('<div style="margin-left: 20px; margin-right: 20px; border-top: solid;background-color: aqua;"></div>');
	
	let termini = $('<div style="float: left"></div>');
	
	let vaziOd = $('<h3><i> Vazi od: </i></h3>');
	let datePocetakVazenja = $('<h3>' + apartman.datePocetakVazenja + '</h3>');
	
	let vaziDo = $('<h3><i> Vazi do: </i></h3>');
	let krajPocetakVazenja = $('<h3>' + apartman.krajPocetakVazenja + '</h3>');
	
	let vreme = ispisiTerminPrijave(apartman);
	
	termini.append(vaziOd).append(datePocetakVazenja).append(vaziDo).append(krajPocetakVazenja);
	datum.append(termini).append(vreme);
	return datum;
}

function ispisiTerminPrijave(apartman){
	let vreme = $('<div style="float: right"></div>');
	
	let termini = $('<div></div>');
	
	let prijava = $('<h3><i> Prijava: </i></h3>');
	let vremePrijave = $('<h3>' + apartman.vremeZaPrijavu + '</h3>');
	
	let odjava = $('<h3><i> Odjava: </i></h3>');
	let vremeOdjave = $('<h3>' + apartman.vremeZaOdjavu + '</h3>');
	
	termini.append(prijava).append(vremePrijave).append(odjava).append(vremeOdjave);
	vreme.append(termini);
	return vreme;
	
}

function ispisiSlikuDIV(apartman){
	let slika = $('<div></div>');
	
	let img = $('<img src="' + apartman.slika + '" alt="" style="max-width: 100%; max-height: 100%"/>');
	slika.append(img);
	
	return slika;
	
}

function provjeriApartman(apartman){
	if(apartman.tipSobe == 0){
		return 'Ceo Apartman';
	}else{
		return 'Soba';
	}
}

function ispisiLokaciju(apartman){
	let lokacija = $('<div style="margin-left: 20px; margin-right: 20px; border-top: solid;background-color: aqua; padding-left: 20px"></div>');
	let lokac = $('<h6><i><i> ' + apartman.lokacija.width + ' LAT; ' + apartman.lokacija.length + ' LONG' +  ' </i></h6>');
	
	let adresa = $('<h4><i> ' + apartman.lokacija.adresa.ulica + ' ' + apartman.lokacija.adresa.broj + ' </i></h4>');
	
	let mesto = $('<h5><i> ' + apartman.lokacija.adresa.mesto + ' ' + apartman.lokacija.adresa.postanskiBroj + ' </i></h5>');
	
	lokacija.append(adresa).append(mesto).append(lokac);
	
	return lokacija;
}

function ispisiButtonObrisi(apartman){

	let button;
	if(!apartman.obrisan){
		button = $('<button id="obrisiApartman' + apartman.id + '" style="margin-left: 20px; width: 90%;"> Obrisi </button>');
		
		button.on('click', function(event){
			obrisi(apartman.id);
		});
	}
	
	return button;
}
function obrisi(id){
	var obj = {"id": id};
	
	$.ajax({
		url: 'rest/apartman',
		type: 'DELETE',
		contentType : 'application/json',
		data: JSON.stringify(obj),
		success : function(response){
			alert('Uspesno ste obrisali apartman');
		},
		error : function(response){
			alert('Ups, ne mozete trenutno obrisati apartman.');
		}
	});
	
}

function ispisiButton(apartman){

	//let button = $('<button id="izmeniApartman' + apartman.id + '" style="margin-left: 20px; width: 90%;"> Izmeni </button>');
	let button = $('<input type="button" id="izmeniApartman' + apartman.id + '" style="margin-left: 20px; width: 90%;" value="Izmeni" />');
	
	button.on('click', function(event){
		var xIzmeni = document.getElementById('editDiv' + apartman.id);
		
		if(xIzmeni.style.display === "none"){
			xIzmeni.style.display = "block";
			document.getElementById('izmeniApartman' + apartman.id).value = "Zatvori";
		}else{
			xIzmeni.style.display = "none";
			document.getElementById('izmeniApartman' + apartman.id).value = "Izmeni";
		}
	});
	
	return button;
}

function editovanjeApartmana(apartman){
	let div = $('<div id="editDiv' + apartman.id + '" style="margin-left: 20px; margin-right: 20px; border-style: solid;background-color: aqua; padding-left: 20px"></div>');

	let brGostiju = $('<p>Broj gostiju:</p>');
	let inputBrGostiju = $('<input type="text" id="inputBrGostiju' + apartman.id + '" value="' + apartman.brojGostiju + '"/>');

	let brSoba = $('<p>Broj soba:</p>');
	let inputBrSoba = $('<input type="text" id="inputBrSoba' + apartman.id + '" value="' + apartman.brojSoba + '"/>');
	
	let cenaPoNoci = $('<p>Cena po noci:</p>');
	let inputcenaPoNoci = $('<input type="text" id="inputcenaPoNoci' + apartman.id + '" value="' + apartman.cenaPoNoci + '"/>');

	let divDate = $('<div id="edit' + apartman.id + '"class="input-group date"></div>');
	
	let datePocetakVazenja = $('<p>Pocetak vazenja:</p>');
	let inputdatePocetakVazenja = $('<input type="text" id="inputdatePocetakVazenja' + apartman.id + '" value="' + apartman.datePocetakVazenja + '" class="datepicker" />');
	inputdatePocetakVazenja.datepicker({
		format: "yyyy-mm-dd",
		autoclose: true
	});
	
	let krajPocetakVazenja = $('<p>Kraj vazenja:</p>');
	let inputkrajPocetakVazenja = $('<input type="text" id="inputkrajPocetakVazenja' + apartman.id + '" value="' + apartman.krajPocetakVazenja + '" class="datepicker" />');
	inputkrajPocetakVazenja.datepicker({
		format: "yyyy-mm-dd",
		autoclose: true
	});
	
	let buttonIzmeni = $('<button>Izmeni</button>');
	buttonIzmeni.on('click', function(event){
		let inputBrGostijuEdit = document.getElementById('inputBrGostiju' + apartman.id).value;
		let inputBrSobaEdit = document.getElementById('inputBrSoba' + apartman.id).value;
		let inputcenaPoNociEdit = document.getElementById('inputcenaPoNoci' + apartman.id).value;
		let inputdatePocetakVazenjaEdit = document.getElementById('inputdatePocetakVazenja' + apartman.id).value;
		let inputkrajPocetakVazenjaEdit = document.getElementById('inputkrajPocetakVazenja' + apartman.id).value;
		
		if(inputBrGostijuEdit == null || inputBrGostijuEdit == undefined || inputBrGostijuEdit == ""){
			alert('Broj gostiju ne smije biti prazan');
		}else if(inputBrSobaEdit == null || inputBrSobaEdit == undefined || inputBrSobaEdit == ""){
			alert('Broj soba ne smije biti prazan');
		}else if(inputcenaPoNociEdit == null || inputcenaPoNociEdit == undefined || inputcenaPoNociEdit == ""){
			alert('Cena po noci ne smije biti prazna');
		}else if(inputdatePocetakVazenjaEdit == null || inputdatePocetakVazenjaEdit == undefined || inputdatePocetakVazenjaEdit == ""){
			alert('Pocetak vazenja apartmana ne smije biti prazan');
		}else if(inputkrajPocetakVazenjaEdit == null || inputkrajPocetakVazenjaEdit == undefined || inputkrajPocetakVazenjaEdit == ""){
			alert('Kraj vazenja apartmana ne smije biti prazan');
		}else{
			
			posaljiIzmene(apartman.id, inputBrGostijuEdit, inputBrSobaEdit, inputcenaPoNociEdit, inputdatePocetakVazenjaEdit, inputkrajPocetakVazenjaEdit);
		}
		
	});
	
	
	divDate.append(datePocetakVazenja).append(inputdatePocetakVazenja).append(krajPocetakVazenja).append(inputkrajPocetakVazenja);
	
	div.append(brGostiju).append(inputBrGostiju).append(brSoba).append(inputBrSoba).append(cenaPoNoci).append(inputcenaPoNoci).append(divDate).append(buttonIzmeni);
	div.hide();
	return div;
}

function posaljiIzmene(id, inputBrGostijuEdit, inputBrSobaEdit, inputcenaPoNociEdit, inputdatePocetakVazenjaEdit, inputkrajPocetakVazenjaEdit){
	var obj = {"brojSoba": inputBrGostijuEdit, "brojGostiju": inputBrSobaEdit, "datePocetakVazenja": inputdatePocetakVazenjaEdit,
			"krajPocetakVazenja": inputkrajPocetakVazenjaEdit, "cenaPoNoci": inputcenaPoNociEdit,
			"id": id};
	
	$.ajax({
		url: 'rest/apartman/edit',
		type: 'POST',
		contentType : 'application/json',
		data: JSON.stringify(obj),
		success : function(response){
			alert('Uspesno izmenjen apartman');
		},
		error : function(response){
			alert('Ups, doslo je do neke greske prilikom izmene apartmana');
		}
	});
}

function ispisiButtonAktivnost(apartman){

	let button;
	if(apartman.status == 0){
		button = $('<button id="neaktivanApartman' + apartman.id + '" style="margin-left: 20px; width: 90%;"> Promeni u neaktivan </button>');
		
		button.on('click', function(event){
			promeniNaNeaktivan(apartman);
		});
	}else{
		button = $('<button id="aktivanApartman' + apartman.id + '" style="margin-left: 20px; width: 90%;"> Promeni u aktivan </button>');
		
		button.on('click', function(event){
			promeniNaAktivan(apartman);
		});
	}
	
	return button;
}
function promeniNaNeaktivan(apartman){
	var obj = {"id": apartman.id};
	
	$.ajax({
		url: 'rest/apartman/status/neaktivan',
		type: 'POST',
		contentType : 'application/json',
		data: JSON.stringify(obj),
		success : function(response){
			alert('Apartman je sada neaktivan');
		},
		error : function(response){
			alert('Ups, ne mozete promeniti aktivnost apartmana');
		}
	});
}

function promeniNaAktivan(apartman){
	var obj = {"id": apartman.id};
	
	$.ajax({
		url: 'rest/apartman/status/aktivan',
		type: 'POST',
		contentType : 'application/json',
		data: JSON.stringify(obj),
		success : function(response){
			alert('Apartman je sada aktivan');
		},
		error : function(response){
			alert('Ups, ne mozete promeniti aktivnost apartmana');
		}
	});
}

function ispisiButtonKomentariApartman(apartman){
	let button = $('<input type="button" id="komentariDivButton' + apartman.id + '" style="margin-left: 20px; width: 90%;" value="Pogledaj Komentare" />');
	
	button.on('click', function(event){
		console.log('Kliknut apartman: ' + apartman.id);
		
		var xKom = document.getElementById('komentariDiv' + apartman.id);
		
		if(xKom.style.display === "none"){
			xKom.style.display = "block";
			document.getElementById('komentariDivButton' + apartman.id).value = "Zatvori";
		}else{
			xKom.style.display = "none";
			document.getElementById('komentariDivButton' + apartman.id).value = "Pogledaj Komentare";
		}
	});
	
	return button;
}
function prikazKomentara(apartman){
	let div = $('<div id="komentariDiv' + apartman.id + '" style="margin-left: 20px; margin-right: 20px; border-style: solid;background-color: aqua; padding-left: 20px"></div>');

	
	if(apartman.komentari != null){
		for(let koment of apartman.komentari){
			let gostKomentar = dobaviGostaKomentara(koment.gost);
			let a = $('<div style="border-style: solid;"><p>Gost: ' + gostKomentar.userName + '</p><p>Komentar: ' + koment.text + '</p><p>Ocena: ' + koment.ocena + '</p></div>');
			div.append(a);
		}
	}
	
	//div.append(komentarDiv);
	div.hide();
	return div;
}

function dobaviGostaKomentara(gost){
	var obj = {"gost": gost};
	var gost
	
	$.ajax({
		url: 'rest/apartman/komentar/gost',
		type: 'POST',
		contentType : 'application/json',
		data: JSON.stringify(obj),
		success : function(response){
			gost = response;
		},
		error : function(response){
			console.log('Ups, nesto je poslo po zlu prilikom dobavljanja vremena');
		}
	});
	return gost;
}

//**********************************************************************************************
//*******************************************************************************
//Prikaz rezervacija

function prikaziRezervacije(){
	
	return function(event){
		event.preventDefault();
		
		console.log('Usao u prendingRezervacije function domacina.');
		$('#domaciniDIV').html('');
		$('#korisniciDIV').html('');
		$('#podaciDiv').html('');
		document.getElementById('searchDiv').style.visibility = "visible";
		document.getElementById('sortirajRezervacije').style.visibility = "visible";
		document.getElementById('statusRezervacije').style.visibility = "visible";
		
		$.ajax({
			url: 'rest/user/administrator/apartman/rezervacija',
			type: 'GET',
			contentType : 'application/json',
			success : function(response){
				console.log(response);
				console.log('Rezervacije za domacina su:')
				for(let res of response){
					console.log(res);
					ispisiApartmanRezervacija(res);
				}
			},
			error : function(response){
				console.log('Ups, nesto je poslo po zlu prilikom dobavljanja vremena');
			}
		});
	}
}

function ispisiApartmanRezervacija(apartman){
	//$('#podaciDiv').html('');
	let original = $('#podaciDiv');
	let divRez = $('<div style="border-style: solid; border-width: medium; margin-top: 20px; background-color: rgb(190, 188, 255);"></div>');
	
	let podaciRez = ispisiPodatkeApartmanRezervacijaDIV(apartman);
	let slikaRez = ispisiSlikuDIV(apartman);
	let datumVazenjaRez = ispisiTerminDatuma(apartman);
	
	let lokacijaRez = ispisiLokaciju(apartman);
	//let button = ispisiButton(apartman);
	//let rezervacija = ispisiRezervacijaZakazivanje(apartman);
	let ispisPodaciRez = ispisiPodatkeRezervacije(apartman);
	
	divRez.append(podaciRez).append(datumVazenjaRez).append(slikaRez).append(lokacijaRez).append(ispisPodaciRez);//.append(rezervacija).append(button);
	
	original.append(divRez);
}

function ispisiPodatkeApartmanRezervacijaDIV(apartman){

	let podaci = $('<div style="margin-left: 20px; margin-right: 20px;background-color: aqua;"></div>');
	
	let tipSobeApartmana = provjeriApartman(apartman);
	let tipSobe = $('<h2> <i> Apartman: </i>' + tipSobeApartmana + '</h2>');
	let brojSoba = $('<h2> <i> Broj Soba: </i>' + apartman.brojSoba +'</h2>');
	let cenaPoNoci = $('<h2> <i> Cena po noci: </i>' + apartman.cenaPoNoci +' <b> <i>$</i> </b></h2>');
	
	podaci.append(tipSobe).append(brojSoba).append(cenaPoNoci);
	return podaci;
}

function ispisiPodatkeRezervacije(apartman){
	let divRez = $('<div style="margin-left: 20px; margin-right: 20px; border-top: solid;background-color: aqua; padding-left: 20px"></div>');
	
	for(let rezervacija of apartman.rezervacije){
		let rezResult = ispisiRezervaciju(rezervacija);
		divRez.append(rezResult);
	}
	
	return divRez;
}

function ispisiRezervaciju(rezervacija){
	let divRezervacija = $('<div style="border-bottom: solid;"> </div>');
	
	let datumRezervacija = $('<h4> <i>Datum rezervacije: </i>' + rezervacija.pocetakIznajmljivanja + '</h4>');
	let brojNocenja = $('<h4> <i>Broj nocenja: </i>' + rezervacija.brojNocenja + '</h4>');
	let ukupnaCena = $('<h4> <i>Ukupna cena: <i>' + rezervacija.ukupnaCena + ' <b> <i>$</i> </b></h4>');

//	let buttonodbijRezervaciju = $('<button>Odbij Rezervaciju</button>');
//	buttonodbijRezervaciju.on('click', function(event){
//		odbijRezervaciju(rezervacija);
//	});
//	
//	let buttonPrihvatiRezervaciju = $('<button>Prihvati Rezervaciju</button>');
//	buttonPrihvatiRezervaciju.on('click', function(event){
//		prihvatiRezervaciju(rezervacija);
//	});
//	
//	let buttonZavrsiRezervaciju = $('<button>Zavrsi Rezervaciju</button>');
//	buttonZavrsiRezervaciju.on('click', function(event){
//		zavrsiRezervaciju(rezervacija);
//	});
	
	divRezervacija.append(datumRezervacija).append(brojNocenja).append(ukupnaCena);//.append(buttonodbijRezervaciju).append(buttonPrihvatiRezervaciju);
	
//	if(rezervacija.status == 0){
//		divRezervacija.append(datumRezervacija).append(brojNocenja).append(ukupnaCena).append(buttonPrihvatiRezervaciju).append(buttonodbijRezervaciju);
//	}else if(rezervacija.status == 3){
//		divRezervacija.append(datumRezervacija).append(brojNocenja).append(ukupnaCena).append(buttonodbijRezervaciju);
//	}
	
//	var godina = rezervacija.pocetakIznajmljivanja.split('-');
//	var dateRezervacije = new Date(godina[0], godina[1], godina[2]);
//	dateRezervacije.setDate(dateRezervacije.getDate() + rezervacija.brojNocenja);
//	var now = new Date();
//	now.setMonth(now.getMonth() + 1);
//	console.log('Sada je datum: ');
//	console.log(dateRezervacije.getFullYear() + '-' + (dateRezervacije.getMonth() ) + '-' + dateRezervacije.getDate());
//
//	if(now > dateRezervacije){
//		console.log('Datum je prosao!');
//		divRezervacija.append(buttonZavrsiRezervaciju);
//	}
	
	return divRezervacija;
}

//***************************************************************************************
//SEARCH
function provjeriPretragu(){
	return function(event){
		event.preventDefault();
		
		$('#podaciDiv').html('');
		
		var mesto;
		var cena;
		
		var mestoElem = document.getElementById('inputMesto').value;
		if(mestoElem == undefined || mestoElem == null){
			mesto = "";
		}else{
			mesto = mestoElem;
		}
		var cena;
		var cenaElem = document.getElementById('inputCena').value;
		if(cenaElem == undefined || cenaElem == null){
			cena = "";
		}else{
			cena = cenaElem; 
		}
		
		var dolazak;
		var dolazakElem = document.getElementById('inputDolazak').value;
		if(dolazakElem == undefined || dolazakElem == null){
			dolazak = "";
		}else{
			dolazak = dolazakElem; 
		}
		
		var odlazak;
		var odlazakElem = document.getElementById('inputOdlazak').value;
		if(odlazakElem == undefined || odlazakElem == null){
			odlazak = "";
		}else{
			odlazak = odlazakElem; 
		}
		
		var brojSoba;
		var brojSobaElem = document.getElementById('inputBrojSoba').value;
		if(brojSobaElem == undefined || brojSobaElem == null){
			brojSoba = "";
		}else{
			brojSoba = brojSobaElem; 
		}
		
		var brojGostiju;
		var brojGostijuElem = document.getElementById('inputBrojGostiju').value;
		if(brojGostijuElem == undefined || brojGostijuElem == null){
			brojGostiju = "";
		}else{
			brojGostiju = brojGostijuElem; 
		}
		
		var rastuceElem = document.getElementById('rastuce').checked;
		var opadajuceElem = document.getElementById('opadajuce').checked;
		var sortiraj;
		if(rastuceElem == true){
			sortiraj = "rastuce";
		}else if(opadajuceElem){
			sortiraj = "opadajuce";
		}else{
			sortiraj = "";
		}
		console.log(sortiraj);
		
		var ceoElem = document.getElementById('ceo').checked;
		var sobaElem = document.getElementById('soba').checked;
		var sortirajTip;
		if(ceoElem == true){
			sortirajTip = "0";
		}else if(sobaElem){
			sortirajTip = "1";
		}else{
			sortirajTip = "";
		}
		
		var aktivanElem = document.getElementById('aktivan').checked;
		var neaktivanElem = document.getElementById('neaktivan').checked;
		var sortirajStatus;
		if(aktivanElem == true){
			sortirajStatus = "0";
		}else if(neaktivanElem){
			sortirajStatus = "1";
		}else{
			sortirajStatus = "";
		}
		
		var sortirajRezervacije = document.getElementById('sortirajRezervacije').style.visibility;
		if(sortirajRezervacije == "hidden"){
			pretraziApartmane(mesto, cena, dolazak, odlazak, brojSoba, brojGostiju, sortiraj, sortirajTip, sortirajStatus);
		}else{
			
			var rastuceRezElem = document.getElementById('rastuceRezervacija').checked;
			var opadajuceRezElem = document.getElementById('opadajuceRezervacija').checked;
			var sortirajRez;
			if(rastuceRezElem == true){
				sortirajRez = "rastuceRez";
			}else if(opadajuceRezElem){
				sortirajRez = "opadajuceRez";
			}else{
				sortirajRez = "";
			}
			
			var statusRezervacijeKreirane = document.getElementById('kreiraneRezervacije').checked;
			var statusRezervacijeOdbijene = document.getElementById('odbijeneRezervacije').checked;
			var statusRezervacijeOdustale = document.getElementById('odustaleRezervacije').checked;
			var statusRezervacijePrihvacene = document.getElementById('prihvaceneRezervacije').checked;
			var statusRezervacijeZavrsene = document.getElementById('zavrseneRezervacije').checked;
			var statusRez;
			if(statusRezervacijeKreirane == true){
				statusRez = "0";
			}else if(statusRezervacijeOdbijene){
				statusRez = "1";
			}else if(statusRezervacijeOdustale){
				statusRez = "2";
			}else if(statusRezervacijePrihvacene){
				statusRez = "3";
			}else if(statusRezervacijeZavrsene){
				statusRez = "4";
			}else{
				statusRez = "";
			}
			
			
			pretraziApartmaneRezervacija(mesto, cena, dolazak, odlazak, brojSoba, brojGostiju, sortiraj, sortirajTip, sortirajStatus,
					sortirajRez, statusRez);
		}
		
	}
}

function pretraziApartmane(mesto, cena, dolazak, odlazak, brojSoba, brojGostiju, sortiraj, sortirajTip, sortirajStatus){
	$('#domaciniDIV').html('');
	
	
	
	var obj = {"mesto": mesto, "cena": cena, "dolazak": dolazak, "odlazak": odlazak, "brojSoba": brojSoba,
			"brojGostiju": brojGostiju, "sortiraj": sortiraj, "sortirajTip" : sortirajTip, "sortirajStatus":sortirajStatus};
	
	
	$.ajax({
		url: 'rest/apartman/search',
		type: 'POST',
		contentType : 'application/json',
		data: JSON.stringify(obj),
		success : function(response){
			for(let res of response){
				console.log(res);
				ispisiApartmane(res);
			}
		},
		error : function(response){
			console.log('Ups, nesto je poslo po zlu prilikom dobavljanja vremena');
		}
	});
}

function pretraziApartmaneRezervacija(mesto, cena, dolazak, odlazak, brojSoba, brojGostiju, sortiraj, sortirajTip, sortirajStatus, sortirajRez, statusRez){
	$('#domaciniDIV').html('');
	
	
	
	var obj = {"mesto": mesto, "cena": cena, "dolazak": dolazak, "odlazak": odlazak, "brojSoba": brojSoba,
			"brojGostiju": brojGostiju, "sortiraj": sortiraj, "sortirajTip" : sortirajTip, "sortirajStatus":sortirajStatus,
			"sortirajRez": sortirajRez, "statusRez": statusRez};
	
	
	$.ajax({
		url: 'rest/rezervacija/admin/search',
		type: 'POST',
		contentType : 'application/json',
		data: JSON.stringify(obj),
		success : function(response){
			for(let res of response){
				console.log(res);
				ispisiApartmanRezervacija(res);
			}
		},
		error : function(response){
			console.log('Ups, nesto je poslo po zlu prilikom dobavljanja vremena');
		}
	});
}