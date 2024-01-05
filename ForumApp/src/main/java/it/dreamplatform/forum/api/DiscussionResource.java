package it.dreamplatform.forum.api;

import com.google.gson.Gson;
import it.dreamplatform.forum.bean.DiscussionBean;
import it.dreamplatform.forum.bean.DiscussionContentBean;
import it.dreamplatform.forum.bean.PublicUserBean;
import it.dreamplatform.forum.bean.UserBean;
import it.dreamplatform.forum.controller.DiscussionController;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * This class contains the api call from the "/discussion" path.
 */
@Path("/discussion")
public class DiscussionResource {
    @Inject
    private DiscussionController discussionController;
    private final Gson gson = new Gson();

    @Context
    HttpServletRequest request;

    /**
     * This function is the api used to retrieve a specific Discussion by going at "/discussion/{discussionId}".
     * @param discussionId is the id of the Discussion.
     * @return a response with a JSON format of the searched Discussion.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/{discussionId: [0-9]+}")
    public Response getPostsByDiscussionId(@PathParam("discussionId") Long discussionId){
        DiscussionContentBean posts = discussionController.getPostsByDiscussionId(discussionId);
        return Response.ok().entity(gson.toJson(posts)).build();
    }

    /**
     * This function is the api used to retrieve all the Discussions published by a Policy maker, by going at "/discussion/policyMaker/{policyMakerId}".
     * @param policyMakerId is the id of the Policy maker.
     * @return a response with a JSON format of the searched List of Discussion.
     */
    @GET
    @Path("/policyMaker/{policyMakerId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @RolesAllowed("policy_maker")
    public Response getDiscussionsByPolicyMaker(@PathParam("policyMakerId") String policyMakerId){
        try {
            List<DiscussionBean> discussions = discussionController.getDiscussionsByPolicyMaker(policyMakerId);
            return Response.ok().entity(gson.toJson(discussions)).build();
        } catch (Exception e) {
            return Response.status(204).entity("[]").build();
        }
    }

    /**
     * This function is the api used to retrieve all the Discussions published by a Policy maker, by going at "/discussion/my".
     * @return a response with a JSON format of the searched List of Discussion.
     */
    @GET
    @Path("/my")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @RolesAllowed("policy_maker")
    public Response getMyDiscussions(){
        try {
            List<DiscussionBean> discussions = discussionController.getDiscussionsByPolicyMaker(((UserBean) request.getSession().getAttribute("user")).getPolicyMakerID());
            return Response.ok().entity(gson.toJson(discussions)).build();
        } catch (Exception e) {
            return Response.status(204).entity("[]").build();
        }
    }

    /**
     * This function is the api used to retrieve the data of a Discussion that is going to be created. It can be used by going at "/discussion/create".
     * @param discussion is the DiscussionContentBean.
     * @return a response with a JSON format about the success of the operation.
     */
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @RolesAllowed("policy_maker")
    public Response createDiscussion(DiscussionContentBean discussion){
        try {
            discussionController.createDiscussion(discussion);
            return Response.ok().entity("{\"success\":1}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"success\":0}").build();
        }
    }

    /**
     * This function is the api used to start the procedure of deleting a Discussion, by going at "/discussion/delete/{discussionId}".
     * @param discussionId is the Discussion.
     * @return a response with a JSON format about the success of the operation.
     */
    @POST
    @Path("/delete/{discussionId:[0-9]+}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @RolesAllowed("policy_maker")
    public Response deleteDiscussion(@PathParam("discussionId") Long discussionId){
        try {
            discussionController.deleteDiscussion(discussionId);
            return Response.ok().entity("{\"success\":1}").build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"success\":0}").build();
        }
    }

    /**
     * This function is the api used to retrieve a List of the most commented Discussions, by going at "/discussion/explore".
     * @return a response with a JSON format of a List of the most commented Discussions.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/explore")
    public Response getDiscussionExplore(){
        List<DiscussionBean> posts = discussionController.getDiscussionExplore(10);
        return Response.ok().entity(gson.toJson(posts)).build();
    }

    /**
     * This function is the api used to retrieve the List of User that has replied to a given discussion, by going at "/discussion/{discussionId}".
     * @param discussionId is the id of the Discussion we are interested in.
     * @return a response with a JSON format of a List of User.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/followers/{discussionId:[0-9]+}")
    public Response getDiscussionFollowers(@PathParam("discussionId") Long discussionId){
        List<PublicUserBean> posts = discussionController.getDiscussionFollowers(discussionId);
        return Response.ok().entity(gson.toJson(posts)).build();
    }
}
