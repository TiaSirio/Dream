package it.dreamplatform.forum.api;

import com.google.gson.Gson;
import it.dreamplatform.forum.bean.PublicUserBean;
import it.dreamplatform.forum.bean.UserBean;
import it.dreamplatform.forum.controller.UserController;
import it.dreamplatform.forum.services.UserService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * This class contains the api call from the "/user" path.
 */
@Path("/user")
public class UserResource {
    @EJB(name = "it.dreamplatform.forum.services/UserService")
    private UserService userService;

    @Inject
    private UserController userController;
    private final Gson gson = new Gson();

    @Context
    HttpServletRequest request;

    /**
     * This function is the api used to retrieve the User in session, by going at "/user".
     * @return a response with a JSON format of all the topics.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @RolesAllowed({"user", "policy_maker"})
    public Response getUser() {
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        return Response.ok().entity(gson.toJson(user)).build();
    }

    /**
     * This function is the api used to retrieve a specific User, by going at "/user/{uid}".
     * @param uid is the id of the User.
     * @return a response with a JSON format of the searched User.
     */
    @GET
    @Path("/{uid: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @RolesAllowed("policy_maker")
    public Response getUserById(@PathParam("uid") Long uid){
        UserBean user = userController.getUserById(uid);
        if (user == null){
            return Response.status(204).entity("{}").build();
        }
        return Response.ok().entity(gson.toJson(user)).build();
    }

    /**
     * This function is the api used to retrieve a specific User by its email, by going at "/user/{mail}".
     * @param mail is the mail of the User.
     * @return a response with a JSON format of the searched User.
     */
    @GET
    @Path("/{mail}")
    @Produces("application/json")
    @RolesAllowed("policy_maker")
    public Response getUserByEmail(@PathParam("mail") String mail){
        UserBean user = userController.searchUser(mail);
        if(user == null){
            return Response.status(204).entity("{}").build();
        }
        return Response.ok().entity(gson.toJson(user)).build();
    }

    /**
     * This function is the api used to retrieve the List of the most active User in the forum, by going at "/user/active".
     * @return a response with a JSON format of the List of searched Users.
     */
    @GET
    @Path("/active")
    @Produces("application/json")
    public Response getMostActiveUsers(){
        List<PublicUserBean> user = userController.getMostActiveUsers(10);
        return Response.ok().entity(gson.toJson(user)).build();
    }

    @GET
    @Path("/wstoken")
    @Produces("application/json")
    @RolesAllowed({"user", "policy_maker", "admin"})
    public String getWebServerToken() {
        return "NO WS TOKEN AVAILABLE";
    }
}
