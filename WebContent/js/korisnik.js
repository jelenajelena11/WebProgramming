$(document).ready(function(){
	
	$('#podaciDiv').html('');
	
	$('#odjava').click(logout());
	
	$('#podaci').click(prikaziPodatke());
	
	$('#apartmani').click(prikaziApartmane());			//Moguce je da ce eventualno biti greska 
														//zbog modela. Ako bude, izmjeniti modele
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
		
		$('#podaciDiv').html('');
		
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

//************************************************************************************************8
function prikaziApartmane(){
	return function(event){
		event.preventDefault();
		
		$('#podaciDiv').html('');
		
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

//*********************************************
//ISPISUJE APARTMANE
function ispisiApartmane(apartman){
	let divOrigin = $('#podaciDiv');
	let div = $('<div style="border-style: solid; border-width: medium; margin-top: 20px; background-color: rgb(190, 188, 255);"></div>');
	
	let podaci = ispisiPodatkeApartmanaDIV(apartman);
	let slika = ispisiSlikuDIV(apartman);
	let datumVazenja = ispisiTerminDatuma(apartman);
	let lokacija = ispisiLokaciju(apartman);
	let button = ispisiButton(apartman);
	let rezervacija = ispisiRezervacijaZakazivanje(apartman);
	
	div.append(podaci).append(datumVazenja).append(slika).append(lokacija).append(rezervacija).append(button);
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
	let datePocetakVazenja = $('<h3>' + apartman.datePocetakVazenja.dayOfMonth + '-' + apartman.datePocetakVazenja.monthValue + '-' + apartman.datePocetakVazenja.year + '</h3>');
	
	let vaziDo = $('<h3><i> Vazi do: </i></h3>');
	let krajPocetakVazenja = $('<h3>' + apartman.krajPocetakVazenja.dayOfMonth + '-' + apartman.krajPocetakVazenja.monthValue + '-' + apartman.krajPocetakVazenja.year + '</h3>');
	
	let vreme = ispisiTerminPrijave(apartman);
	
	termini.append(vaziOd).append(datePocetakVazenja).append(vaziDo).append(krajPocetakVazenja);
	datum.append(termini).append(vreme);
	return datum;
}

function ispisiTerminPrijave(apartman){
	let vreme = $('<div style="float: right"></div>');
	
	let termini = $('<div></div>');
	
	let prijava = $('<h3><i> Prijava: </i></h3>');
	let vremePrijave = $('<h3>' + apartman.vremeZaPrijavu.hour + ':' + apartman.vremeZaPrijavu.minute + '</h3>');
	
	let odjava = $('<h3><i> Odjava: </i></h3>');
	let vremeOdjave = $('<h3>' + apartman.vremeZaOdjavu.hour + ':' + apartman.vremeZaOdjavu.minute + '</h3>');
	
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

	let button = $('<input type="button" id="rezervisiApartman' + apartman.id + '" style="margin-left: 20px; width: 90%;" value="Rezervisi" />');
	
	button.on('click', function(event){
		console.log('Kliknut apartman: ' + apartman.id);
		
		var x = document.getElementById('rezervacijaDiv' + apartman.id);
		
		if(x.style.display === "none"){
			x.style.display = "block";
			document.getElementById('rezervisiApartman' + apartman.id).value = "Odustani";
		}else{
			x.style.display = "none";
			document.getElementById('rezervisiApartman' + apartman.id).value = "Rezervisi";
		}
	});
	
	return button;
}

function ispisiRezervacijaZakazivanje(apartman){
	let div = $('<div id="rezervacijaDiv' + apartman.id + '" style="margin-left: 20px; margin-right: 20px; border-style: solid;background-color: aqua; padding-left: 20px"></div>');
	
	let formaRezervacije = $('<h3>Popunite sledeca polja za rezervaciju</h3>');
	
	let divDate = $('<div id="' + apartman.id + '"class="input-group date"></div>');
	
	let pTagDate= $('<p> <h4> Izaberite datum rezervacije: </h4> </p>');
	let input = $('<input id="datePickerInput' + apartman.id +'" type="text" class="datepicker" />');
	let pTag = $('<p> <h4> Unesite broj dana koliko zelite rezervisati apartman: </h4> </p>');
	let dani = $('<input id="inputDani' + apartman.id + '" type="text" placeholder="E.g. 4" />');
	let breakTag = $('<br>');
	let pTagPoruka = $('<p> <h5>Unesite proizvoljnu poruku</h5> </p>');
	let inputPoruka = $(' <input id="inputPoruka' + apartman.id + '" type="text" placeholder="Poruka domacinu" /> ');
	let buttonRezervisi = $('<button>Kreiraj Rezervaciju</button>');
	
	buttonRezervisi.on('click', function(event){
		console.log('Kliknut apartman: ' + apartman.id);
		let pocetakIznajmljivanja = document.getElementById('datePickerInput' + apartman.id).value;
		let daniInput = document.getElementById('inputDani' + apartman.id).value;
		let porukaDomacinu = document.getElementById('inputPoruka' + apartman.id).value;
		console.log('Poruka domacinu je: ' + porukaDomacinu);
		
		kreirajRezervaciju(pocetakIznajmljivanja, daniInput, porukaDomacinu, apartman.id);
	});
	
	input.datepicker({
		format: "yyyy-mm-dd"
	});
	divDate.append(pTagDate).append(input).append(pTag).append(dani).append(breakTag).append(pTagPoruka).append(inputPoruka).append(breakTag).append(buttonRezervisi);
	div.append(formaRezervacije).append(divDate);
	div.hide();
	return div;
}
//**************************************************************************************

function kreirajRezervaciju(pocetakIznajmljivanja, daniInput, porukaDomacinu, apartman){
	console.log('Pocetak iznajmljivanja iz kreirajRezervaiju: ' + pocetakIznajmljivanja);
	
	var obj = { "brojNocenja": daniInput, "poruka": porukaDomacinu, "pocetakIznajmljivanja": pocetakIznajmljivanja,
					"apartman": apartman };
	
	$.ajax({
		url: 'rest/rezervacija',
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