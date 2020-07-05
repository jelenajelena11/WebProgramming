package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import model.Adresa;
import model.Apartman;
import model.Komentar;
import model.User;

@Path("/apartman")
public class ApartmanService {

	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("apartmanDAO") == null) {
			ctx.setAttribute("apartmanDAO", new ApartmanDAO());
		}
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApartmani(@Context HttpServletRequest request) {
		
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		List<Apartman> aktivni = new ArrayList<Apartman>();
		
		for(Apartman a : apartmani.getApartmani().values()) {
			if(a.getStatus() == 0) {		//Dobavlja samo aktivne apartmane
				aktivni.add(a);
			}
		}
		
		return Response.ok(aktivni).build();
		//return Response.status(200).build();
	}
	
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApartmaniSearch(@Context HttpServletRequest request, String search) {
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
		sortiraj = sortiraj.replace("\"", "");
		sortiraj = sortiraj.replace("}", "");
		
		if(loggedIn.getUloga() == 0) {
			for(Apartman a : apartmani.getApartmani().values()){
				if(a.getStatus() == 0 && this.isMestoValid(a.getLokacija().getAdresa().getMesto(), mesto)
						&& this.isCenaValid(a.getCenaPoNoci(), cena) 
						&& this.isDolazakValid(a.getDatePocetakVazenja(), dolazak)
						&&  this.isOdlazakValid(a.getKrajPocetakVazenja(), odlazak)
						&& this.isBrojSobaValid(a.getBrojSoba(), brojSoba) 
						&& this.isBrojGostijuValid(a.getBrojGostiju(), brojGostiju)) {		
									//Dobavlja samo aktivne apartmane
									
					aktivni.add(a);			
				}
			}
		}else {
			String sortirajTip = ((searchArray[7]).split(":"))[1];
			String sortirajStatus = ((searchArray[8]).split(":"))[1];
			
			for(Apartman a : apartmani.getApartmani().values()) {
				if(this.isMestoValid(a.getLokacija().getAdresa().getMesto(), mesto)
						&& this.isCenaValid(a.getCenaPoNoci(), cena) 
						&& this.isDolazakValid(a.getDatePocetakVazenja(), dolazak)
						&&  this.isOdlazakValid(a.getKrajPocetakVazenja(), odlazak)
						&& this.isBrojSobaValid(a.getBrojSoba(), brojSoba) 
						&& this.isBrojGostijuValid(a.getBrojGostiju(), brojGostiju)
						&& this.isTipValid(a.getTipSobe(), sortirajTip)
						&& this.isStatusValid(a.getStatus(), sortirajStatus)) {	
									
					aktivni.add(a);			
				}
			}
		}
		
		if(sortiraj.equals("rastuce")) {
			Collections.sort(aktivni, new CompareApartmanRastuce());
		}else if(sortiraj.equals("opadajuce")) {
			Collections.sort(aktivni, new CompareApartmanOpadajuce());
		}
		
		System.out.println("Pronadjeni apartmani: " + aktivni);
		return Response.ok(aktivni).build();
	}
	
	@POST
	@Path("/komentar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createKomentar(@Context HttpServletRequest request, Komentar komentar) {
		
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		Apartman a = apartmani.findOneApartman(komentar.getApartman());
		if(a.getKomentari() != null) {
			a.getKomentari().add(komentar);
		}else {
			List<Komentar> komentari = new ArrayList<Komentar>();
			komentari.add(komentar);
			a.setKomentari(komentari);
		}
		
		System.out.println("Uneseni komentar je: " + komentar);
		System.out.println("Apartman sa komentarom je: " + a);
		
		return Response.ok().build();
	}
	@POST
	@Path("/komentar/odobri")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response odobriKomentar(@Context HttpServletRequest request, Komentar komentar) {
		
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		Apartman a = apartmani.findOneApartman(komentar.getApartman());
		
		for(Komentar k : a.getKomentari()) {
			if(k.getId().equals(komentar.getId())) {
				k.setOdobren(true);
			}
		}
		
		System.out.println("Uneseni komentar odobri je: " + komentar);
		System.out.println("Apartman sa komentarom odobri je: " + a);
		
		return Response.ok().build();
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
}

class CompareApartmanRastuce implements Comparator<Apartman> {
	
	@Override
	public int compare(Apartman o1, Apartman o2) {
		
		return (o1.getCenaPoNoci() - o2.getCenaPoNoci());
	}
}

class CompareApartmanOpadajuce implements Comparator<Apartman> {
	
	@Override
	public int compare(Apartman o1, Apartman o2) {
		
		return (o2.getCenaPoNoci() - o1.getCenaPoNoci());
	}
}