package services;

import java.util.ArrayList;
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
		
		for(Rezervacija r : user.getZakazaneRezervacije()) {
			if(r.getId().equals(rezervacijaOdustanak.getId())) {
				r.setStatus(2);
				System.out.println("Novi status je: " + r.getStatus());
				users.saveUsers("");
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
		
		for(Rezervacija r : user.getZakazaneRezervacije()) {
			if(r.getId().equals(rezervacijaOdbij.getId())) {
				r.setStatus(1);
				System.out.println("Novi status je: " + r.getStatus());
				users.saveUsers("");
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
		
		for(Rezervacija r : user.getZakazaneRezervacije()) {
			if(r.getId().equals(rezervacijaPrihvati.getId())) {
				r.setStatus(3);
				System.out.println("Novi status je: " + r.getStatus());
				users.saveUsers("");
			}
		}
		
		Rezervacija rez = rezervacije.getRezervacije().get(rezervacijaPrihvati.getId());
		rez.setStatus(3);
		rezervacije.saveRezervacije("");
		System.out.println("Id prihvacene rez je: " + rez.getStatus());
		
		return Response.status(200).build();
	}
}
