package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
			ctx.setAttribute("userDAO", new UserDAO());
		}
		if(ctx.getAttribute("apartmanDAO") == null) {
			ctx.setAttribute("apartmanDAO", new ApartmanDAO());
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
	@Path("/korisnik")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getUsersKorisnik(@Context HttpServletRequest request) {
		UserDAO users = (UserDAO) ctx.getAttribute("userDAO");
		ArrayList<User> usersArray = new ArrayList<User>();
		for(User u : users.getUsers().values()) {
			if(u.getUloga() == 0) {
				usersArray.add(u);
			}
		}
		
		return usersArray;
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
			if( (a.getStatus() == 0) && (a.getDomacin().getId().equals(loggedIn.getId())) ) { //DOBAVLJA SAMO AKTIVNE APARTMANE
				aktivni.add(a);		
			}
		}
		
		return Response.ok(aktivni).build();
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
			if(a.getDomacin().getId().equals(loggedIn.getId())) {
				System.out.println("Nasao apartman kome je vlasnik loggedIn");
				if(a.getStatus() == 0) {									//DOBAVLJA SAMO AKTIVNE APARTMANE
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
		}
		System.out.println(aktivniPending);
		return Response.ok(aktivniPending).build();
	}
	
	
	
}