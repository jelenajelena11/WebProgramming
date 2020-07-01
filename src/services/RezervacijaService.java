package services;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
	}
	
	//Provjeriti da rezervacija ima sacuvan citav Apartman, ne samo UUID apartmana
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRezervacija(@Context HttpServletRequest request, Rezervacija newRezervacija) {
		User user = (User) request.getSession().getAttribute("user");
		ApartmanDAO apartmani = (ApartmanDAO) ctx.getAttribute("apartmanDAO");
		RezervacijaDAO rezervacije = (RezervacijaDAO) ctx.getAttribute("rezervacijaDAO");
		
		Apartman found = apartmani.findOneApartman(newRezervacija.getApartman());
		if(found != null) {
			int cenaPoNoci = found.getCenaPoNoci();
			int brojNocenja = newRezervacija.getBrojNocenja();
			newRezervacija.setUkupnaCena(brojNocenja*cenaPoNoci);
			
			UUID id = UUID.randomUUID();
			newRezervacija.setId(id);
			rezervacije.getRezervacije().put(id, newRezervacija);
			user.getZakazaneRezervacije().add(newRezervacija);
			System.out.println(user);
			System.out.println(rezervacije.getRezervacije().values());
			
			return Response.ok(newRezervacija).build();
		}
		
		return Response.status(400).build();
		
	}
}
