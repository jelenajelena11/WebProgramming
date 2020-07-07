package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.ApartmanDAO;
import dao.UserDAO;
import model.Apartman;
import model.Rezervacija;
import model.User;

@Path("/user")
public class UserService {

	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO(ctx.getRealPath("")));
		}
		if(ctx.getAttribute("apartmanDAO") == null) {
			ctx.setAttribute("apartmanDAO", new ApartmanDAO(ctx.getRealPath("")));
		}
	}
	
	@GET
	@Path("/domacin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getUsersDomacin(@Context HttpServletRequest request) {
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		ArrayList<User> usersArray = new ArrayList<User>();
		for(User u : users.getUsers().values()) {
			if(u.getUloga() == 2) {
				usersArray.add(u);
			}
		}
		
		return usersArray;
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@Context HttpServletRequest request) {
		User loggedIn = (User) request.getSession().getAttribute("user");
		
		return Response.ok(loggedIn).build();
	}
	
	@POST
	@Path("/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userEdit(@Context HttpServletRequest request, User u) {
		User loggedIn = (User) request.getSession().getAttribute("user");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
	
		System.out.println("Podaci su: " + u);
		User user = users.findUser(loggedIn.getId());
		user.setFirstName(u.getFirstName());
		user.setLastName(u.getLastName());
		user.setPassword(u.getPassword());
		users.saveUsers("");
		
		
		System.out.println("Novi user je: " + loggedIn);
		return Response.ok(loggedIn).build();
	}
	
	@GET
	@Path("/korisnik")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersKorisnik(@Context HttpServletRequest request) {
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		List<User> usersArray = this.getAllUsers(users);
		
		return Response.ok(usersArray).build();
	}
	
	@POST
	@Path("/administrator/korisnik/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersKorisnikSearch(@Context HttpServletRequest request, String search) {
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		
		String[] searchArray = search.split(",");
		String username = ((searchArray[0]).split(":"))[1];
		String ime = ((searchArray[1]).split(":"))[1];
		String prezime = ((searchArray[2]).split(":"))[1];
		String pol = ((searchArray[3]).split(":"))[1];
		String uloga = ((searchArray[4]).split(":"))[1];
		
		List<User> usersArray = this.getAllUsers(users);
		
		List<User> ok = new ArrayList<User>();
		for(User u : usersArray) {
			if(this.isUsernameValid(u.getUserName(), username)
					&& this.isImeValid(u.getFirstName(), ime)
					&& this.isPrezimeValid(u.getLastName(), prezime)
					&& this.isPolValid(u.getGender(), pol)
					&& this.isUlogaValid(u.getUloga(), uloga)) {
				ok.add(u);
			}
		}
		
		return Response.ok(ok).build();
	}
	
	private List<User> getAllUsers(UserDAO users){
		ArrayList<User> usersArray = new ArrayList<User>();
		for(User u : users.getUsers().values()) {
			if(u.getUloga() == 0 || u.getUloga() == 2) {
				usersArray.add(u);
			}
		}
		
		return usersArray;
	}
	
	@GET
	@Path("/domacin/gost")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getUsersDomacina(@Context HttpServletRequest request) {
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User loggedIn = (User) request.getSession().getAttribute("user");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");

		
		List<User> unique = this.getGostiDomacina(users, loggedIn, apartmani);
		
		return unique;
	}
	
	@POST
	@Path("/domacin/gost/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersDomacinaSearch(@Context HttpServletRequest request, String search) {
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		User loggedIn = (User) request.getSession().getAttribute("user");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		
		System.out.println("Search je: " + search);
		String[] searchArray = search.split(",");
		String username = ((searchArray[0]).split(":"))[1];
		String ime = ((searchArray[1]).split(":"))[1];
		String prezime = ((searchArray[2]).split(":"))[1];
		String pol = ((searchArray[3]).split(":"))[1];

		
		List<User> unique = this.getGostiDomacina(users, loggedIn, apartmani);
		List<User> ok = new ArrayList<User>();
		for(User u : unique) {
			if(this.isUsernameValid(u.getUserName(), username)
					&& this.isImeValid(u.getFirstName(), ime)
					&& this.isPrezimeValid(u.getLastName(), prezime)) {
				ok.add(u);
			}
		}
		System.out.println(ok);
		return Response.ok(ok).build();
	}
	private boolean isUsernameValid(String username, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			if(username.contains(toSearch)) {
				return true;
			}
			return false;
		}
		return true;
	}
	private boolean isImeValid(String ime, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			if(ime.contains(toSearch)) {
				return true;
			}
			return false;
		}
		return true;
	}
	private boolean isPrezimeValid(String prezime, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			if(prezime.contains(toSearch)) {
				return true;
			}
			return false;
		}
		return true;
	}
	private boolean isPolValid(String pol, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			//toSearch = toSearch.replace("}", "");
			if(pol.contains(toSearch)) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	private boolean isUlogaValid(int uloga, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			toSearch = toSearch.replace("}", "");
			System.out.println("uloga search: " + toSearch);
			if(toSearch.equals("")) {
				return true;
			}
			int ulogaSearch = Integer.parseInt(toSearch);
			if(uloga == ulogaSearch) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	private List<User> getGostiDomacina(UserDAO users, User loggedIn, ApartmanDAO apartmani){
		HashMap<UUID, User> usersArray = new HashMap<UUID, User>();
		ArrayList<Apartman> aktivni = new ArrayList<Apartman>();
		
		for(Apartman a : apartmani.getApartmani().values()) {
			if( (a.getStatus() == 0) && (a.getDomacin().getId().equals(loggedIn.getId())) ) { //DOBAVLJA SAMO AKTIVNE APARTMANE
				aktivni.add(a);		
			}
		}
		for(Apartman a : aktivni) {
			if(a.getRezervacije() != null) {
				for(Rezervacija r : a.getRezervacije()) {
					User gostRezervacija = users.findUser(r.getGost());
					if(gostRezervacija != null) {
						usersArray.put(gostRezervacija.getId(), gostRezervacija);
					}
				}
			}
		}
		
		List<User> unique = new ArrayList<User>();
		for(User u : usersArray.values()) {
			unique.add(u);
		}
		System.out.println("Svi korisnici su: " + usersArray);
		System.out.println("Jedinstveni korisnici su: " + unique);
		
		return unique;
	}
	
	@GET
	@Path("/apartman")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDomacinApartman(@Context HttpServletRequest request) {
		User loggedIn = (User) request.getSession().getAttribute("user");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		List<Apartman> aktivni = new ArrayList<Apartman>();
		
		for(Apartman a : apartmani.getApartmani().values()) {
			if( (a.getStatus() == 0) && (!a.isObrisan()) && (a.getDomacin().getId().equals(loggedIn.getId())) ) { //DOBAVLJA SAMO AKTIVNE APARTMANE
				aktivni.add(a);		
			}
		}
		
		return Response.ok(aktivni).build();
	}
	
	@POST
	@Path("/apartman/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDomacinApartmanSearch(@Context HttpServletRequest request, String search) {
		User loggedIn = (User) request.getSession().getAttribute("user");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		List<Apartman> aktivni = new ArrayList<Apartman>();
		
		System.out.println("Search je: " + search);
		String[] searchArray = search.split(",");
		String mesto = ((searchArray[0]).split(":"))[1];
		String cena = ((searchArray[1]).split(":"))[1];
		String dolazak = ((searchArray[2]).split(":"))[1];
		String odlazak = ((searchArray[3]).split(":"))[1];
		String brojSoba = ((searchArray[4]).split(":"))[1];
		String brojGostiju = ((searchArray[5]).split(":"))[1];
		String sortiraj = ((searchArray[6]).split(":"))[1];
		String tip = ((searchArray[7]).split(":"))[1];
		String status = ((searchArray[8]).split(":"))[1];
		sortiraj = sortiraj.replace("\"", "");
		
		for(Apartman a : apartmani.getApartmani().values()) {
			if(this.doesBelongToUser(a.getId(), loggedIn)) {
				if( (!a.isObrisan()) && this.isMestoValid(a.getLokacija().getAdresa().getMesto(), mesto)
						&& this.isCenaValid(a.getCenaPoNoci(), cena) 
						&& this.isDolazakValid(a.getDatePocetakVazenja(), dolazak)
						&&  this.isOdlazakValid(a.getKrajPocetakVazenja(), odlazak)
						&& this.isBrojSobaValid(a.getBrojSoba(), brojSoba) 
						&& this.isBrojGostijuValid(a.getBrojGostiju(), brojGostiju)
						&& this.isTipValid(a.getTipSobe(), tip)
						&& this.isStatusValid(a.getStatus(), status)) {		
							
					aktivni.add(a);			
				}
			}
		}
		
		if(sortiraj.equals("rastuce")) {
			Collections.sort(aktivni, new CompareApartmanRastuceDomacin());
		}else if(sortiraj.equals("opadajuce")) {
			Collections.sort(aktivni, new CompareApartmanOpadajuceDomacin());
		}
		System.out.println("Sortirani: " + aktivni);
		for(Apartman a : aktivni) {
			System.out.println(a.getCenaPoNoci());
		}
		return Response.ok(aktivni).build();
	}
	
	private boolean doesBelongToUser(UUID idApartmana, User user) {
		List<UUID> apartmani = user.getApartmaniZaIznajmljivanje();
		System.out.println("Svi apartmani korisnika: " + apartmani);
		for(UUID id : apartmani) {
			if(id.equals(idApartmana)) {
				return true;
			}
		}
		return false;
	}
	
	@GET
	@Path("/apartman/rezervacija/kreiran")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDomacinApartmanRezervacije(@Context HttpServletRequest request) {
		User loggedIn = (User) request.getSession().getAttribute("user");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		
		List<Apartman> aktivniPending = new ArrayList<Apartman>();
		
		for(Apartman a : apartmani.getApartmani().values()) {
			System.out.println("Domacin je: " + a.getDomacin());
			System.out.println("Ulogovan je: " + loggedIn);
			if(a.getDomacin().getId().equals(loggedIn.getId()) && (!a.isObrisan())) {
				System.out.println("Nasao apartman kome je vlasnik loggedIn");
				Apartman newApartman = new Apartman(a);
				System.out.println("Apartman je: ");
				System.out.println(a);
				if(a.getRezervacije() != null) {
					for(Rezervacija r : a.getRezervacije()) {
						if(r.getStatus() == 0) {						//DOBAVLJA SAMO KREIRANE REZERVACIJE
							newApartman.getRezervacije().add(r);
						}
					}
					aktivniPending.add(newApartman);
				}
			}
		}
		System.out.println(aktivniPending);
		return Response.ok(aktivniPending).build();
	}
	
	private boolean isMestoValid(String mesto, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			if(mesto.contains(toSearch)) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	
	private boolean isCenaValid(int cena, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			System.out.println("Cena search: " + toSearch);
			if(toSearch.equals("")) {
				return true;
			}
			int cenaSearch = Integer.parseInt(toSearch);
			if(cena == cenaSearch) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	private boolean isDolazakValid(String dolazak, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			
			if(toSearch.equals("")) {
				return true;
			}
			
			String[] dolazakArray = dolazak.split("-");
			String[] dolazakSearch = toSearch.split("-");
			
			LocalDate dateDolazak = LocalDate.of(Integer.parseInt(dolazakArray[0]), Integer.parseInt(dolazakArray[1]), Integer.parseInt(dolazakArray[2]));
			LocalDate dateDolazakSearch = LocalDate.of(Integer.parseInt(dolazakSearch[0]), Integer.parseInt(dolazakSearch[1]), Integer.parseInt(dolazakSearch[2]));
			
			if(dateDolazak.isBefore(dateDolazakSearch)) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	private boolean isOdlazakValid(String odlazak, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			
			if(toSearch.equals("")) {
				return true;
			}
			
			String[] odlazakArray = odlazak.split("-");
			String[] odlazakSearch = toSearch.split("-");
			
			LocalDate dateOdlazak = LocalDate.of(Integer.parseInt(odlazakArray[0]), Integer.parseInt(odlazakArray[1]), Integer.parseInt(odlazakArray[2]));
			LocalDate dateOdlazakSearch = LocalDate.of(Integer.parseInt(odlazakSearch[0]), Integer.parseInt(odlazakSearch[1]), Integer.parseInt(odlazakSearch[2]));
			
			if(dateOdlazak.isAfter(dateOdlazakSearch)) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	private boolean isBrojSobaValid(int brojSoba, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			
			if(toSearch.equals("")) {
				return true;
			}
			
			int cenaSearch = Integer.parseInt(toSearch);
			System.out.println("CenaSearch " + cenaSearch);
			if(brojSoba == cenaSearch) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	private boolean isBrojGostijuValid(int brojGostiju, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			//toSearch = toSearch.replace("}", "");
			System.out.println("BrojGostijuValid: " + toSearch);
			if(toSearch.equals("")) {
				return true;
			}
			
			int cenaSearch = Integer.parseInt(toSearch);
			if(brojGostiju == cenaSearch) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	private boolean isTipValid(int tip, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			System.out.println("tipValid: " + toSearch);
			if(toSearch.equals("")) {
				System.out.println("TipValid je prazan");
				return true;
			}
			
			int tipSearch = Integer.parseInt(toSearch);
			System.out.println("Tip search broj je: " + tipSearch);
			if(tip == tipSearch) {
				System.out.println(tip + " = " + tipSearch);
				return true;
			}
			return false;
		}
		return true;
	}
	
	private boolean isStatusValid(int status, String toSearch) {
		if(!toSearch.equals("")) {
			toSearch = toSearch.replace("\"", "");
			toSearch = toSearch.replace("}", "");
			System.out.println("statusValid: " + toSearch);
			if(toSearch.equals("")) {
				return true;
			}
			
			int statusSearch = Integer.parseInt(toSearch);
			if(status == statusSearch) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	private boolean isStatusRezervacijeValid(int status, String toSearch) {
		if(!toSearch.equals("")) {
			//toSearch = toSearch.replace("\"", "");
			//toSearch = toSearch.replace("}", "");
			System.out.println("statusRezervacijeValid: " + toSearch);
			if(toSearch.equals("")) {
				return true;
			}
			
			int statusSearch = Integer.parseInt(toSearch);
			if(status == statusSearch) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	@POST
	@Path("/apartman/rezervacija/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDomacinApartmanRezervacijeSearch(@Context HttpServletRequest request, String search) {
		User loggedIn = (User) request.getSession().getAttribute("user");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		
		System.out.println("Search je: " + search);
		String[] searchArray = search.split(",");
		String mesto = ((searchArray[0]).split(":"))[1];
		String cena = ((searchArray[1]).split(":"))[1];
		String dolazak = ((searchArray[2]).split(":"))[1];
		String odlazak = ((searchArray[3]).split(":"))[1];
		String brojSoba = ((searchArray[4]).split(":"))[1];
		String brojGostiju = ((searchArray[5]).split(":"))[1];
		String sortiraj = ((searchArray[6]).split(":"))[1];
		String tip = ((searchArray[7]).split(":"))[1];
		String status = ((searchArray[8]).split(":"))[1];
		sortiraj = sortiraj.replace("\"", "");
		String sortirajRez = ((searchArray[9]).split(":"))[1];
		sortirajRez = sortirajRez.replace("\"", "");
		String sortirajStatusRez = ((searchArray[10]).split(":"))[1];
		sortirajStatusRez = sortirajStatusRez.replace("\"", "");
		sortirajStatusRez = sortirajStatusRez.replace("}", "");
		
		//Povratni podaci;
		List<Apartman> aktivniPending = new ArrayList<Apartman>();
		
		for(Apartman a : apartmani.getApartmani().values()) {
			System.out.println("Domacin je: " + a.getDomacin());
			System.out.println("Ulogovan je: " + loggedIn);
			if(a.getDomacin().getId().equals(loggedIn.getId()) && (!a.isObrisan())) {
				System.out.println("Nasao apartman kome je vlasnik loggedIn");
				Apartman newApartman = new Apartman(a);
				System.out.println("Apartman je: ");
				System.out.println(a);
				if(a.getRezervacije() != null) {
					for(Rezervacija r : a.getRezervacije()) {
							newApartman.getRezervacije().add(r);
					}
					aktivniPending.add(newApartman);
				}
			}
		}
		
		List<Apartman> pretrazeni = new ArrayList<Apartman>();
		System.out.println("Provjera za rezervacijaSearch apartmana");
		for(Apartman a : aktivniPending) {
			if(this.isMestoValid(a.getLokacija().getAdresa().getMesto(), mesto)
					&& this.isCenaValid(a.getCenaPoNoci(), cena) 
					&& this.isDolazakValid(a.getDatePocetakVazenja(), dolazak)
					&&  this.isOdlazakValid(a.getKrajPocetakVazenja(), odlazak)
					&& this.isBrojSobaValid(a.getBrojSoba(), brojSoba) 
					&& this.isBrojGostijuValid(a.getBrojGostiju(), brojGostiju)
					&& this.isTipValid(a.getTipSobe(), tip)
					&& this.isStatusValid(a.getStatus(), status)) {		
				System.out.println("Nasao jedno podudaranje apartmana: " + a);
				pretrazeni.add(a);			
			}
		}
		System.out.println("Pretrazeni apartmani su: " );
		for(Apartman a : pretrazeni) {
			System.out.println(a.getRezervacije());
		}
		
		if(sortiraj.equals("rastuce")) {
			Collections.sort(pretrazeni, new CompareApartmanRastuceDomacin());
		}else if(sortiraj.equals("opadajuce")) {
			Collections.sort(pretrazeni, new CompareApartmanOpadajuceDomacin());
		}
		
		if(sortirajRez.equals("rastuceRez")) {
			for(Apartman a : pretrazeni) {
				if(a.getRezervacije() != null) {
					List<Rezervacija> rezervacije = a.getRezervacije();
					Collections.sort(rezervacije, new CompareRezervacijeRastuce());
				}
			}
		}else if(sortirajRez.equals("opadajuceRez")){
			for(Apartman a : pretrazeni) {
				if(a.getRezervacije() != null) {
					List<Rezervacija> rezervacije = a.getRezervacije();
					Collections.sort(rezervacije, new CompareRezervacijeOpadajuce());
				}
			}
		}
		
		List<Apartman> ok = new ArrayList<Apartman>();
		for(Apartman a : pretrazeni) {
			List<Rezervacija> rezervacije = a.getRezervacije();
			List<Rezervacija> okRezervacije = new ArrayList<Rezervacija>();
			if(rezervacije != null) {
				for(Rezervacija r : rezervacije) {
					if(this.isStatusRezervacijeValid(r.getStatus(), sortirajStatusRez)) {
						okRezervacije.add(new Rezervacija(r));
					}
				}a.setRezervacije(okRezervacije);
			}
		}
		System.out.println("***********************************************");
		for(Apartman a : pretrazeni) {
			System.out.println(a);
		}
		
		return Response.ok(pretrazeni).build();
	}
	
	
	//Rezervacije administrator (inicijalno prikazuje sve rezervacije)
	@GET
	@Path("/administrator/apartman/rezervacija")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAdministratorApartmanRezervacije(@Context HttpServletRequest request) {
		User loggedIn = (User) request.getSession().getAttribute("user");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		
		List<Apartman> aktivniPending = new ArrayList<Apartman>();
		
		for(Apartman a : apartmani.getApartmani().values()) {
			if((!a.isObrisan())) {
				Apartman newApartman = new Apartman(a);
				if(a.getRezervacije() != null) {
					for(Rezervacija r : a.getRezervacije()) {
							newApartman.getRezervacije().add(r);
					}
					aktivniPending.add(newApartman);
				}
			}
		}
		System.out.println(aktivniPending);
		return Response.ok(aktivniPending).build();
	}
	
}

class CompareApartmanRastuceDomacin implements Comparator<Apartman> {
	
	@Override
	public int compare(Apartman o1, Apartman o2) {
		
		return (o1.getCenaPoNoci() - o2.getCenaPoNoci());
	}
}

class CompareApartmanOpadajuceDomacin implements Comparator<Apartman> {
	
	@Override
	public int compare(Apartman o1, Apartman o2) {
		
		return (o2.getCenaPoNoci() - o1.getCenaPoNoci());
	}
}