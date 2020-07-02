$(document).ready(function(){
	
	$('#odjava').click(logout());
	$('#dodajApartman').click(dodaj());
	$('#sidebar').click(function(){
		
		document.getElementById("sidebar").classList.toggle("active");
	});
	
	$('#apartmani').click(prikaziApartmane());
	$('#pregledKorisnika').click(prikaziKorisnike());
	
	$('#noveRezervacije').click(prikaziPendingRezervacije());
	
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

function prikaziKorisnike(){
	
	return function(event){
		event.preventDefault();
		
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

//************************************************************************************************8
function prikaziApartmane(){
	return function(event){
		event.preventDefault();
		
		$.ajax({
			url: 'rest/user/apartman',
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
//*********************************************
//ISPISUJE APARTMANE
function ispisiApartmane(apartman){
	let divOrigin = $('#domaciniDIV');
	let div = $('<div style="border-style: solid; border-width: medium; margin-top: 20px; background-color: rgb(190, 188, 255);"></div>');
	
	let podaci = ispisiPodatkeApartmanaDIV(apartman);
	let slika = ispisiSlikuDIV(apartman);
	let datumVazenja = ispisiTerminDatuma(apartman);
	let lokacija = ispisiLokaciju(apartman);
	let button = ispisiButton(apartman);
	
	div.append(podaci).append(datumVazenja).append(slika).append(lokacija).append(button);
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
//ISPISUJE BUTTON I DEFINISE EVENT HANDLER
function ispisiButton(apartman){

	let button = $('<button id="izmeniApartman' + apartman.id + '"> Izmeni </button>');
	
	button.on('click', function(event){
		alert('Kliknut apartman: ' + apartman.id);
		console.log('Kliknut apartman: ' + apartman.id);
		console.log('event je: ' + event)
	});
	
	return button;
}
//**************************************************************************************
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

//*******************************************************************************************************
//Ispisuje Pending Rezervacije za apartmane ulogovanog domacina

function prikaziPendingRezervacije(){
	
	return function(event){
		event.preventDefault();
		
		console.log('Usao u prendingRezervacije function domacina.');
		$('#domaciniDIV').html('');
		
		$.ajax({
			url: 'rest/user/apartman/rezervacija/kreiran',
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
	let original = $('#domaciniDIV');
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

	let buttonodbijRezervaciju = $('<button>Odbij Rezervaciju</button>');
	buttonodbijRezervaciju.on('click', function(event){
		odbijRezervaciju(rezervacija);
	});
	
	let buttonPrihvatiRezervaciju = $('<button>Prihvati Rezervaciju</button>');
	buttonPrihvatiRezervaciju.on('click', function(event){
		prihvatiRezervaciju(rezervacija);
	});
	
	divRezervacija.append(datumRezervacija).append(brojNocenja).append(ukupnaCena).append(buttonodbijRezervaciju).append(buttonPrihvatiRezervaciju);
	
	return divRezervacija;
}

function odbijRezervaciju(rezervacija){
	
	var obj = { "id": rezervacija.id};
	
	$.ajax({
		url: 'rest/rezervacija/odbij',
		type: 'POST',
		contentType : 'application/json',
		data: JSON.stringify(obj),
		success : function(response){
			console.log('Odgovor servisa je:' + response);
		},
		error : function(response){
			console.log('Ups, nesto je poslo po zlu prilikom dobavljanja vremena');
		}
	});
}

function prihvatiRezervaciju(rezervacija){
var obj = { "id": rezervacija.id};
	
	$.ajax({
		url: 'rest/rezervacija/prihvati',
		type: 'POST',
		contentType : 'application/json',
		data: JSON.stringify(obj),
		success : function(response){
			console.log('Odgovor servisa je:' + response);
		},
		error : function(response){
			console.log('Ups, nesto je poslo po zlu prilikom dobavljanja vremena');
		}
	});
}