package services;

import java.util.ArrayList;
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
}
