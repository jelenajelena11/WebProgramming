package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import dao.RezervacijaDAO;
import dao.UserDAO;
import model.Apartman;
import model.Rezervacija;
import model.User;

@Path("/rezervacija")
public class RezervacijaService {

	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("rezervacijaDAO") == null) {
			ctx.setAttribute("rezervacijaDAO", new RezervacijaDAO());
		}
		if(ctx.getAttribute("apartmanDAO") == null) {
			ctx.setAttribute("apartmanDAO", new ApartmanDAO());
		}
		if(ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO());
		}
	}
	
	//Provjeriti da rezervacija ima sacuvan citav Apartman, ne samo UUID apartmana
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRezervacija(@Context HttpServletRequest request, Rezervacija newRezervacija) {
		
		User user = (User) request.getSession().getAttribute("user");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		RezervacijaDAO rezervacije = (RezervacijaDAO) ctx.getAttribute("rezervacijaDAO");
		
		Apartman found = apartmani.findOneApartman(newRezervacija.getApartman());
		if(found != null) {
			int cenaPoNoci = found.getCenaPoNoci();
			int brojNocenja = newRezervacija.getBrojNocenja();
			newRezervacija.setUkupnaCena(brojNocenja*cenaPoNoci);
			newRezervacija.setStatus(0);							//KREIRANA	
			
			UUID id = UUID.randomUUID();
			newRezervacija.setId(id);
			rezervacije.getRezervacije().put(id, newRezervacija);
			user.getZakazaneRezervacije().add(newRezervacija);
			
			if(found.getRezervacije() == null) {
				found.setRezervacije(new ArrayList<Rezervacija>());
			}
			found.getRezervacije().add(newRezervacija);
			
			users.saveUsers("");
			rezervacije.saveRezervacije("");
			apartmani.saveApartmani("");
			
			return Response.ok(newRezervacija).build();
		}
		
		return Response.status(400).build();
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRezervacije(@Context HttpServletRequest request) {
		ApartmanDAO apartmaniDAO = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		User user = (User) request.getSession().getAttribute("user");
		
		//Povratni podaci;
		List<Apartman> apartmani = new ArrayList<Apartman>();
		
		for(Rezervacija r : user.getZakazaneRezervacije()) {
			if(r.getStatus() == 0) {
				Apartman found = apartmaniDAO.findOneApartman(r.getApartman());
				Apartman ap = this.findOneApartmanByUUID(apartmani, found);
				if(ap == null) {
					Apartman newApartman = new Apartman(found);
					newApartman.getRezervacije().add(r);
					apartmani.add(newApartman);
				}else {
					ap.getRezervacije().add(r);
				}
			}
		}
		return Response.ok(apartmani).build();
	}
	
	@GET
	@Path("/accepted")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAcceptedRezervacije(@Context HttpServletRequest request) {
		//ApartmanDAO apartmaniDAO = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		User user = (User) request.getSession().getAttribute("user");
		
		//Povratni podaci;
		List<Apartman> apartmani = this.getAcceptedRezervacijeFunction(user);
		
		return Response.ok(apartmani).build();
	}
	
	private List<Apartman> getAcceptedRezervacijeFunction(User user) {
		ApartmanDAO apartmaniDAO = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		
		//Povratni podaci;
		List<Apartman> apartmani = new ArrayList<Apartman>();
		
		for(Rezervacija r : user.getZakazaneRezervacije()) {
			if(r.getStatus() == 3) {
				Apartman found = apartmaniDAO.findOneApartman(r.getApartman());
				Apartman ap = this.findOneApartmanByUUID(apartmani, found);
				if(ap == null) {
					Apartman newApartman = new Apartman(found);
					newApartman.getRezervacije().add(r);
					apartmani.add(newApartman);
				}else {
					ap.getRezervacije().add(r);
				}
			}
		}
		return apartmani;
	}

	private Apartman findOneApartmanByUUID(List<Apartman> apartmani, Apartman apartman) {
		for(Apartman a : apartmani) {
			if(a.getId().equals(apartman.getId()))
				return a;
		}
		return null;
	}
	
	@POST
	@Path("/odustanak")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response odustaniRezervacija(@Context HttpServletRequest request, Rezervacija rezervacijaOdustanak) {
		
		User user = (User) request.getSession().getAttribute("user");
		RezervacijaDAO rezervacije = (RezervacijaDAO) ctx.getAttribute("rezervacijaDAO");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		
		for(User u : users.getUsers().values()) {
			for(Rezervacija r : u.getZakazaneRezervacije()) {
				if(r.getId().equals(rezervacijaOdustanak.getId())) {
					r.setStatus(2);
					System.out.println("Novi status je: " + r.getStatus());
					users.saveUsers("");
				}
			}
		}
		for(Apartman a : apartmani.getApartmani().values()) {
			if(a.getRezervacije() != null) {
				for(Rezervacija r : a.getRezervacije()) {
					if(r.getId().equals(rezervacijaOdustanak.getId())) {
						r.setStatus(2);
						apartmani.saveApartmani("");
					}
				}
			}
		}
		
		Rezervacija rez = rezervacije.getRezervacije().get(rezervacijaOdustanak.getId());
		rez.setStatus(2);
		rezervacije.saveRezervacije("");
		
		return Response.status(200).build();
	}
	
	@POST
	@Path("/odbij")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response odbijRezervacija(@Context HttpServletRequest request, Rezervacija rezervacijaOdbij) {
		
		User user = (User) request.getSession().getAttribute("user");
		RezervacijaDAO rezervacije = (RezervacijaDAO) ctx.getAttribute("rezervacijaDAO");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		
		for(User u : users.getUsers().values()) {
			for(Rezervacija r : u.getZakazaneRezervacije()) {
				if(r.getId().equals(rezervacijaOdbij.getId())) {
					r.setStatus(1);
					System.out.println("Novi status je: " + r.getStatus());
					users.saveUsers("");
				}
			}
		}
		for(Apartman a : apartmani.getApartmani().values()) {
			if(a.getRezervacije() != null) {
				for(Rezervacija r : a.getRezervacije()) {
					if(r.getId().equals(rezervacijaOdbij.getId())) {
						r.setStatus(1);
						apartmani.saveApartmani("");
					}
				}
			}
		}
		
		Rezervacija rez = rezervacije.getRezervacije().get(rezervacijaOdbij.getId());
		rez.setStatus(1);
		rezervacije.saveRezervacije("");
		
		return Response.status(200).build();
	}
	
	@POST
	@Path("/prihvati")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response prihvatiRezervacija(@Context HttpServletRequest request, Rezervacija rezervacijaPrihvati) {
		
		User user = (User) request.getSession().getAttribute("user");
		RezervacijaDAO rezervacije = (RezervacijaDAO) ctx.getAttribute("rezervacijaDAO");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		
		for(User u : users.getUsers().values()) {
			for(Rezervacija r : u.getZakazaneRezervacije()) {
				if(r.getId().equals(rezervacijaPrihvati.getId())) {
					r.setStatus(3);
					users.saveUsers("");
				}
			}
		}
		for(Apartman a : apartmani.getApartmani().values()) {
			if(a.getRezervacije() != null) {
				for(Rezervacija r : a.getRezervacije()) {
					if(r.getId().equals(rezervacijaPrihvati.getId())) {
						r.setStatus(3);
						apartmani.saveApartmani("");
					}
				}
			}
		}
		
		Rezervacija rez = rezervacije.getRezervacije().get(rezervacijaPrihvati.getId());
		rez.setStatus(3);
		rezervacije.saveRezervacije("");
		System.out.println("Id prihvacene rez je: " + rez.getStatus());
		
		return Response.status(200).build();
	}
	
	@POST
	@Path("/zavrsi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response zavrsiRezervacija(@Context HttpServletRequest request, Rezervacija rezervacijaZavrsi) {
		
		User user = (User) request.getSession().getAttribute("user");
		RezervacijaDAO rezervacije = (RezervacijaDAO) ctx.getAttribute("rezervacijaDAO");
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		
		for(User u : users.getUsers().values()) {
			for(Rezervacija r : u.getZakazaneRezervacije()) {
				if(r.getId().equals(rezervacijaZavrsi.getId())) {
					r.setStatus(4);
					users.saveUsers("");
				}
			}
		}
		for(Apartman a : apartmani.getApartmani().values()) {
			if(a.getRezervacije() != null) {
				for(Rezervacija r : a.getRezervacije()) {
					if(r.getId().equals(rezervacijaZavrsi.getId())) {
						r.setStatus(4);
						apartmani.saveApartmani("");
					}
				}
			}
		}
		
		Rezervacija rez = rezervacije.getRezervacije().get(rezervacijaZavrsi.getId());
		rez.setStatus(4);
		rezervacije.saveRezervacije("");
		System.out.println("Id zavrsene rez je: " + rez.getStatus());
		
		return Response.status(200).build();
	}
	
	@POST					//gost, za apartmane
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRezervacijeSearch(@Context HttpServletRequest request, String search) {
		ApartmanDAO apartmaniDAO = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		User user = (User) request.getSession().getAttribute("user");
		
		System.out.println("Search za rezervaciju je: " + search);
		
		String[] searchArray = search.split(",");
		String mesto = ((searchArray[0]).split(":"))[1];
		String cena = ((searchArray[1]).split(":"))[1];
		String dolazak = ((searchArray[2]).split(":"))[1];
		String odlazak = ((searchArray[3]).split(":"))[1];
		String brojSoba = ((searchArray[4]).split(":"))[1];
		String brojGostiju = ((searchArray[5]).split(":"))[1];
		String sortiraj = ((searchArray[6]).split(":"))[1];
		sortiraj = sortiraj.replace("\"", "");
		String sortirajRez = ((searchArray[7]).split(":"))[1];
		sortirajRez = sortirajRez.replace("\"", "");
		sortirajRez = sortirajRez.replace("}", "");
		
		//Povratni podaci;
		List<Apartman> apartmani = new ArrayList<Apartman>();
		
		for(Rezervacija r : user.getZakazaneRezervacije()) {
			Apartman found = apartmaniDAO.findOneApartman(r.getApartman());
			Apartman ap = this.findOneApartmanByUUID(apartmani, found);
			if(ap == null) {									//Da li se vec nalazi u listi apartmani
				Apartman newApartman = new Apartman(found);
				newApartman.getRezervacije().add(r);
				apartmani.add(newApartman);
			}else {
				ap.getRezervacije().add(r);
			}
		}
		
		List<Apartman> pretrazeni = new ArrayList<Apartman>();
		for(Apartman a : apartmani) {
			if(this.isMestoValid(a.getLokacija().getAdresa().getMesto(), mesto)
					&& this.isCenaValid(a.getCenaPoNoci(), cena) 
					&& this.isDolazakValid(a.getDatePocetakVazenja(), dolazak)
					&&  this.isOdlazakValid(a.getKrajPocetakVazenja(), odlazak)
					&& this.isBrojSobaValid(a.getBrojSoba(), brojSoba) 
					&& this.isBrojGostijuValid(a.getBrojGostiju(), brojGostiju)) {		
				pretrazeni.add(a);			
			}
		}
		System.out.println("Pretrazeni apartmani su: " );
		for(Apartman a : pretrazeni) {
			System.out.println(a.getRezervacije());
		}
		
		if(sortiraj.equals("rastuce")) {
			Collections.sort(pretrazeni, new CompareApartmanRezervacijaRastuce());
		}else if(sortiraj.equals("opadajuce")) {
			Collections.sort(pretrazeni, new CompareApartmanRezervacijaOpadajuce());
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
		
		return Response.ok(pretrazeni).build();
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
			System.out.println("isBrojGostijuValid: " + brojGostiju + " = " + cenaSearch);
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
				return true;
			}
			
			int tipSearch = Integer.parseInt(toSearch);
			if(tip == tipSearch) {
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
	
	@POST							//Rezervacije administrator
	@Path("/admin/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRezervacijeAdminSearch(@Context HttpServletRequest request, String search) {
		ApartmanDAO apartmaniDAO = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		RezervacijaDAO rezervacije = (RezervacijaDAO) ctx.getAttribute("rezervacijaDAO");
		
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
		List<Apartman> apartmani = new ArrayList<Apartman>();
		
		for(Rezervacija r : rezervacije.getRezervacije().values()) {
			Apartman found = apartmaniDAO.findOneApartman(r.getApartman());
			Apartman ap = this.findOneApartmanByUUID(apartmani, found);
			if(ap == null) {									//Da li se vec nalazi u listi apartmani
				Apartman newApartman = new Apartman(found);
				newApartman.getRezervacije().add(r);
				apartmani.add(newApartman);
			}else {
				ap.getRezervacije().add(r);
			}
		}
		
		List<Apartman> pretrazeni = new ArrayList<Apartman>();
		for(Apartman a : apartmani) {
			if(this.isMestoValid(a.getLokacija().getAdresa().getMesto(), mesto)
					&& this.isCenaValid(a.getCenaPoNoci(), cena) 
					&& this.isDolazakValid(a.getDatePocetakVazenja(), dolazak)
					&&  this.isOdlazakValid(a.getKrajPocetakVazenja(), odlazak)
					&& this.isBrojSobaValid(a.getBrojSoba(), brojSoba) 
					&& this.isBrojGostijuValid(a.getBrojGostiju(), brojGostiju)
					&& this.isTipValid(a.getTipSobe(), tip)
					&& this.isStatusValid(a.getStatus(), status)) {		
				pretrazeni.add(a);			
			}
		}
		System.out.println("Pretrazeni apartmani su: " );
		for(Apartman a : pretrazeni) {
			System.out.println(a.getRezervacije());
		}
		
		if(sortiraj.equals("rastuce")) {
			Collections.sort(pretrazeni, new CompareApartmanRezervacijaRastuce());
		}else if(sortiraj.equals("opadajuce")) {
			Collections.sort(pretrazeni, new CompareApartmanRezervacijaOpadajuce());
		}
		
		if(sortirajRez.equals("rastuceRez")) {
			for(Apartman a : pretrazeni) {
				if(a.getRezervacije() != null) {
					List<Rezervacija> rezervacijee = a.getRezervacije();
					Collections.sort(rezervacijee, new CompareRezervacijeRastuce());
				}
			}
		}else if(sortirajRez.equals("opadajuceRez")){
			for(Apartman a : pretrazeni) {
				if(a.getRezervacije() != null) {
					List<Rezervacija> rezervacijee = a.getRezervacije();
					Collections.sort(rezervacijee, new CompareRezervacijeOpadajuce());
				}
			}
		}
		
		List<Apartman> ok = new ArrayList<Apartman>();
		for(Apartman a : pretrazeni) {
			List<Rezervacija> rezervacijee = a.getRezervacije();
			List<Rezervacija> okRezervacije = new ArrayList<Rezervacija>();
			if(rezervacije != null) {
				for(Rezervacija r : rezervacijee) {
					if(this.isStatusRezervacijeValid(r.getStatus(), sortirajStatusRez)) {
						okRezervacije.add(new Rezervacija(r));
					}
				}a.setRezervacije(okRezervacije);
			}
		}
		
		return Response.ok(pretrazeni).build();
	}
}

class CompareRezervacijeRastuce implements Comparator<Rezervacija> {
	
	@Override
	public int compare(Rezervacija o1, Rezervacija o2) {
		
		return (o1.getUkupnaCena() - o2.getUkupnaCena());
	}
}

class CompareRezervacijeOpadajuce implements Comparator<Rezervacija> {
	
	@Override
	public int compare(Rezervacija o1, Rezervacija o2) {
		
		return (o2.getUkupnaCena() - o1.getUkupnaCena());
	}
}

class CompareApartmanRezervacijaRastuce implements Comparator<Apartman> {
	
	@Override
	public int compare(Apartman o1, Apartman o2) {
		
		return (o1.getCenaPoNoci() - o2.getCenaPoNoci());
	}
}

class CompareApartmanRezervacijaOpadajuce implements Comparator<Apartman> {
	
	@Override
	public int compare(Apartman o1, Apartman o2) {
		
		return (o2.getCenaPoNoci() - o1.getCenaPoNoci());
	}
}
