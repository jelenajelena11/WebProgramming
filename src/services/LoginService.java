package services;

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

import dao.UserDAO;
import model.User;

@Path("")
public class LoginService {

	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("userDAO") == null) {
			ctx.setAttribute("userDAO", new UserDAO());
		}
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User login(User user, @Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		User u = userDAO.findUser(user);
		
		if(u == null) {
			return null;
		}
		ctx.setAttribute("userDAO", userDAO);
		request.getSession().setAttribute("user", u);
		return u;
	}
	
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response odjava(@Context HttpServletRequest request) {
		
		request.getSession().invalidate();
		
		return Response.status(200).build();
	}
}
