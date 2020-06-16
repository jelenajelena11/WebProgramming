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

import dao.UserDAO;
import model.Apartman;
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
		List<Apartman> aktivni = new ArrayList<Apartman>();
		
		for(Apartman a : loggedIn.getApartmaniZaIznajmljivanje()) {
			if(a.getStatus() == 0) {									//DOBAVLJA SAMO AKTIVNE APARTMANE
				aktivni.add(a);		
			}
		}
		
		return Response.ok(aktivni).build();
	}
	
	
}