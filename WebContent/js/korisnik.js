$(document).ready(function(){
	
	document.getElementById('searchDiv').style.visibility = "hidden";
	document.getElementById('sortirajRezervacije').style.visibility = "hidden";
	
	$('#podaciDiv').html('');
	
	$('#odjava').click(logout());
	
	$('#podaci').click(prikaziPodatke());
	
	$('#apartmani').click(prikaziApartmane());			//Moguce je da ce eventualno biti greska 
														//zbog modela. Ako bude, izmjeniti modele
	
	$('#pendingRezervacije').click(pendingRezervacije());
	
	$('#prihvaceneRezervacije').click(acceptedRezervacije());
	
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

function prikaziPodatke(){
	
	return function(event){
		event.preventDefault();
		
		$('#podaciDiv').html('');
		document.getElementById('sortirajRezervacije').style.visibility = "hidden";
		
		$.ajax({
			url: 'rest/user/korisnik',
			type: 'GET',
			contentType : 'application/json',
			success : function(users){
				for(let user of users){
					console.log(user);
					ispisiMojePodatke(user);
				}
				
				document.getElementById('searchDiv').style.visibility = "hidden";
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
		document.getElementById('sortirajRezervacije').style.visibility = "hidden";
		
		$.ajax({
			url: 'rest/apartman',
			type: 'GET',
			contentType : 'application/json',
			success : function(result){
				for(let res of result){		
					console.log(res);		
					ispisiApartmane(res);
				}
				document.getElementById('searchDiv').style.visibility = "visible";
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
	let komentari = ispisiButtonKomentariApartman(apartman);
	let sviKomentari = prikazKomentara(apartman);
	
	div.append(podaci).append(datumVazenja).append(slika).append(lokacija).append(rezervacija).append(button).append(sviKomentari).append(komentari);
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
			if(koment.odobren){
				let gostKomentar = dobaviGostaKomentara(koment.gost);
				let a = $('<div style="border-style: solid;"><p>Gost: ' + gostKomentar.userName + '</p><p>Komentar: ' + koment.text + '</p><p>Ocena: ' + koment.ocena + '</p></div>');
				div.append(a);
			}
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
		format: "yyyy-mm-dd",
		autoclose: true
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
//***********************************************************************************************

//Prikaz rezervacija gosta
function pendingRezervacije(){

	return function(event){
		event.preventDefault();
		
		console.log('Usao u prendingRezervacije function.');
		$('#podaciDiv').html('');
		document.getElementById('sortirajRezervacije').style.visibility = "visible";
		
		$.ajax({
			url: 'rest/rezervacija',
			type: 'GET',
			contentType : 'application/json',
			success : function(response){
				for(let res of response){
					console.log(res);
					ispisiApartmanRezervacija(res);
				}
				document.getElementById('searchDiv').style.visibility = "visible";
			},
			error : function(response){
				console.log('Ups, nesto je poslo po zlu prilikom dobavljanja vremena');
			}
		});
	}
}

function ispisiApartmanRezervacija(apartman){
	let original = $('#podaciDiv');
	//let originDiv = $('#originDiv');
	let divRez = $('<div style="border-style: solid; border-width: medium; margin-top: 20px; background-color: rgb(190, 188, 255);"></div>');
	
	
	let podaciRez = ispisiPodatkeApartmanRezervacijaDIV(apartman);
	let slikaRez = ispisiSlikuDIV(apartman);
	let datumVazenjaRez = ispisiTerminDatuma(apartman);
	
	let lokacijaRez = ispisiLokaciju(apartman)
	let ispisPodaciRez = ispisiPodatkeRezervacije(apartman);
	
	divRez.append(podaciRez).append(datumVazenjaRez).append(slikaRez).append(lokacijaRez).append(ispisPodaciRez);
	
	let ispisuButtonKomentar;
	let komentari;
	if(provjeriValidnostZaKomentar(apartman)){
		ispisuButtonKomentar = ispisiButtonKomentar(apartman);
		komentari = ispisiUnosKomentar(apartman);
		divRez.append(komentari).append(ispisuButtonKomentar);
	}
	
	
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

	divRezervacija.append(datumRezervacija).append(brojNocenja).append(ukupnaCena);
	
	let buttonOdustani
	if(rezervacija.status == 0 || rezervacija.status == 3){
		buttonOdustani = $('<button>Odustani od rezervacije</button>');
		buttonOdustani.on('click', function(event){
			odustaniOdRezervacije(rezervacija);
		});
		divRezervacija.append(datumRezervacija).append(brojNocenja).append(ukupnaCena).append(buttonOdustani);
	}
	if(rezervacija.status == 4){
		buttonOdustani = $('<button>Ostavi Komentar</button>');
		buttonOdustani.on('click', function(event){
			ispisiUnosKomentar(rezervacija);
		});
	}
	
	
	return divRezervacija;
}

function provjeriValidnostZaKomentar(apartman){
	for(let reservation of apartman.rezervacije){
		if(reservation.status == 4){
			return true;
		}
	}
	return false;
}

function ispisiButtonKomentar(apartman){
	let button = $('<input type="button" id="komentarDivButton' + apartman.id + '" style="margin-left: 20px; width: 90%;" value="Unesi Komentar" />');
	
	button.on('click', function(event){
		console.log('Kliknut apartman: ' + apartman.id);
		
		var xKom = document.getElementById('komentarDiv' + apartman.id);
		
		if(xKom.style.display === "none"){
			xKom.style.display = "block";
			document.getElementById('komentarDivButton' + apartman.id).value = "Odustani";
		}else{
			xKom.style.display = "none";
			document.getElementById('komentarDivButton' + apartman.id).value = "Unesi Komentar";
		}
	});
	
	return button;
}

function ispisiUnosKomentar(apartman){
	let div = $('<div id="komentarDiv' + apartman.id + '" style="margin-left: 20px; margin-right: 20px; border-style: solid;background-color: aqua; padding-left: 20px"></div>');
	
	let formaKomentar = $('<h4>Ostavi Komentar:</h4>');
	let pTagKomentar = $('<p>Unesi poruku komentara:</p>');
	let inputKomentar = $('<input id="inputKomentar' + apartman.id + '" type="text" placeholder="Tekst poruke" />');
	let pTagOcena = $('<p>Unesi ocenu:</p>');
	let ocenaRadioOne = $('<input type="radio" name="ocena' + apartman.id + '" id="ocena1' + apartman.id + '">1<br>');
	let ocenaRadioTwo = $('<input type="radio" name="ocena' + apartman.id + '" id="ocena2' + apartman.id + '">2<br>');
	let ocenaRadioThree = $('<input type="radio" name="ocena' + apartman.id + '" id="ocena3' + apartman.id + '">3<br>');
	let ocenaRadioFour = $('<input type="radio" name="ocena' + apartman.id + '" id="ocena4' + apartman.id + '">4<br>');
	let ocenaRadioFive = $('<input type="radio" name="ocena' + apartman.id + '" id="ocena5' + apartman.id + '">5<br>');
	let buttonOceni = $('<button id="buttonOceni' + apartman.id + '">Oceni</button>');
	
	buttonOceni.on('click', function(event){
		let unesenaKomentar = document.getElementById('inputKomentar' + apartman.id).value;
		
		let ocenaOneElem = document.getElementById('ocena1' + apartman.id).checked;
		let ocenaTwoElem = document.getElementById('ocena2' + apartman.id).checked;
		let ocenaThreeElem = document.getElementById('ocena3' + apartman.id).checked;
		let ocenaFourElem = document.getElementById('ocena4' + apartman.id).checked;
		let ocenaFiveElem = document.getElementById('ocena5' + apartman.id).checked;
		let unetaOcena
		if(ocenaOneElem){
			unetaOcena = "1";
		}else if(ocenaTwoElem){
			unetaOcena = "2";
		}else if(ocenaThreeElem){
			unetaOcena = "3";
		}else if(ocenaFourElem){
			unetaOcena = "4";
		}else if(ocenaFiveElem){
			unetaOcena = "5";
		}else{
			unetaOcena = "";
		}
		
		posaljiOcenu(unesenaKomentar, unetaOcena, apartman.id);
	});
	
	
	div.append(formaKomentar).append(pTagKomentar).append(inputKomentar).append(pTagOcena).append(ocenaRadioOne).append(ocenaRadioTwo).append(ocenaRadioThree).append(ocenaRadioFour).append(ocenaRadioFive).append(buttonOceni);
	div.hide();
	return div;
}
function posaljiOcenu(unesenaKomentar, unetaOcena, id){
	var obj = {"text" : unesenaKomentar, "ocena": unetaOcena, "apartman": id};
	
	$.ajax({
		url: 'rest/apartman/komentar',
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

function odustaniOdRezervacije(rezervacija){
	
	var obj = { "id": rezervacija.id};
	
	$.ajax({
		url: 'rest/rezervacija/odustanak',
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

//*********************************************************************************************************
//Pretrage

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
		
		var sortirajRezervacije = document.getElementById('sortirajRezervacije').style.visibility;
		if(sortirajRezervacije == "hidden"){
			pretraziApartmane(mesto, cena, dolazak, odlazak, brojSoba, brojGostiju, sortiraj);
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
			
			pretraziApartmaneRezervacijaSortiraj(mesto, cena, dolazak, odlazak, brojSoba, brojGostiju, sortiraj, sortirajRez)
		}
	}
}

function pretraziApartmaneRezervacijaSortiraj(mesto, cena, dolazak, odlazak, brojSoba, brojGostiju, sortiraj, sortirajRez){
	$('#podaciDiv').html('');
	
	
	
	var obj = {"mesto": mesto, "cena": cena, "dolazak": dolazak, "odlazak": odlazak, "brojSoba": brojSoba,
						"brojGostiju": brojGostiju, "sortiraj": sortiraj, "sortirajRez" : sortirajRez};
	
	$.ajax({
		url: 'rest/rezervacija/search',
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

function pretraziApartmane(mesto, cena, dolazak, odlazak, brojSoba, brojGostiju, sortiraj){
	$('#podaciDiv').html('');
	
	
	
	var obj = {"mesto": mesto, "cena": cena, "dolazak": dolazak, "odlazak": odlazak, "brojSoba": brojSoba,
						"brojGostiju": brojGostiju, "sortiraj": sortiraj};
	
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

//***********************************************************************************************

//Prikaz prihvacenih rezervacija gosta

function acceptedRezervacije(){

	return function(event){
		event.preventDefault();
		
		console.log('Usao u prendingRezervacije function.');
		$('#podaciDiv').html('');
		document.getElementById('sortirajRezervacije').style.visibility = "hidden";
		
		$.ajax({
			url: 'rest/rezervacija/accepted',
			type: 'GET',
			contentType : 'application/json',
			success : function(response){
				for(let res of response){
					console.log(res);
					ispisiApartmanRezervacija(res);
				}
				document.getElementById('searchDiv').style.visibility = "hidden";
			},
			error : function(response){
				console.log('Ups, nesto je poslo po zlu prilikom dobavljanja vremena');
			}
		});
	}
}