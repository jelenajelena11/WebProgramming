package services;

import java.time.LocalDate;
import java.util.ArrayList;
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
		System.out.println("Mesto: " + mesto);
		System.out.println("cena " + cena);
		System.out.println("dolazak " + dolazak);
		System.out.println("odlazak " + odlazak );
		System.out.println("brojSoba " + brojSoba);
		System.out.println("brojGostiju " + brojGostiju);
		
		for(Apartman a : apartmani.getApartmani().values()) {
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
		System.out.println("Pronadjeni apartmani: " + aktivni);
		return Response.ok(aktivni).build();
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
			toSearch = toSearch.replace("}", "");
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
}
