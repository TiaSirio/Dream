package it.dreamplatform.forum.api;

import com.google.gson.Gson;
import it.dreamplatform.forum.bean.TopicBean;
import it.dreamplatform.forum.bean.TopicContentBean;
import it.dreamplatform.forum.controller.TopicController;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * This class contains the api call from the "/topic" path.
 */
@Path("/topic")
public class TopicResource {
   @Inject
    private TopicController topicController;
    private final Gson gson = new Gson();

    /**
     * This function is the api used to retrieve all the Topic by going at "/topic".
     * @return a response with a JSON format of all the topics.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Response getAllTopics() {
        List<TopicBean> topics = topicController.getAllTopics();
        String resp = gson.toJson(topics);
        return Response.ok().entity(resp).build();
    }

    /**
     * This function is the api used to retrieve a specific Topic by going at "/topic/{topicId}".
     * @param topicId is the id of the Topic.
     * @return a response with a JSON format of the searched Topic.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @Path("/{topicId: [0-9]+}")
    public Response getTopicById(@PathParam("topicId") Long topicId){
        TopicContentBean topic = topicController.getDiscussionsByTopicId(topicId);
        return Response.ok().entity(gson.toJson(topic)).build();
    }

}
