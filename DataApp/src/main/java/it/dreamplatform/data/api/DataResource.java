package it.dreamplatform.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.dreamplatform.data.bean.DataBean;
import it.dreamplatform.data.bean.DistrictBean;
import it.dreamplatform.data.bean.RankingBean;
import it.dreamplatform.data.controller.DataController;
import it.dreamplatform.data.entity.Data;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * This class contains the api call from the "/data" path.
 */
@Path("/data")
public class DataResource {
    @Inject
    private DataController dataController;

    @Context
    HttpServletRequest request;

    private final Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();

    /**
     * This function is the api used to retrieve the ranking of a district according to all the dataset present at DB, by going at "/data/ranking".
     * @param districtId is the id of the District.
     * @return a response with a JSON format of the ranking retrieved.
     */
    @GET
    @Path("/ranking")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @RolesAllowed("policy_maker")
    public Response createRanking(@QueryParam("districtId") String districtId){
        try {
            List<RankingBean> rankings = dataController.createRanking(districtId);
            return Response.ok().entity(gson.toJson(rankings)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).entity("{\"success\":0}").build();
        }
    }

    /**
     * This function is the api used to retrieve a searched district by going at "/data/district/{districtId}".
     * @param districtId is the id of the District.
     * @return a response with a JSON format of the searched district.
     */
    @GET
    @Path("/district/{districtId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Response retrieveDistrict(@PathParam("districtId") String districtId) {
        try {
            DistrictBean districtBean = dataController.createSingleDistrict(districtId);
            return Response.ok().entity(gson.toJson(districtBean)).build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"success\":0}").build();
        }
    }

    /**
     * This function is the api used to retrieve the data of a searched district according to all the datasets present in the DB.
     * It is accessible by going at "/data/filter/{districtId}".
     * @param districtId is the id of the District.
     * @return a response with a JSON format of the searched dataset.
     */
    @GET
    @Path("/filter/{districtId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Response retrieveDataParsedByDistrict(@PathParam("districtId") String districtId) {
        try {
            List<List<DataBean>> dataBeansOfDistrict = dataController.getDataOfDistrict(districtId);
            return Response.ok().entity(gson.toJson(dataBeansOfDistrict)).build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"success\":0}").build();
        }
    }

    /**
     * This function is the api used to retrieve all the data of a given dataSet using the id of its dataSource, by going at "/data/dataset/{dataSourceId}".
     * @param dataSourceId is the id of the dataSource.
     * @return a response with a JSON format of the searched dataset.
     */
    @GET
    @Path("/dataset/{dataSourceId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Response retrieveDataSetByDataSource(@PathParam("dataSourceId") Long dataSourceId) {
        try {
            List<Data> dataSource = dataController.retrieveDataSetByDataSourceId(dataSourceId);
            return Response.ok().entity(gson.toJson(dataSource)).build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"success\":0}").build();
        }
    }

    /**
     * This function is the api used to retrieve the ranking of a district according to some dataset present at DB, by going at "/ranking/recalculate".
     * @param districtId is the id of the district.
     * @param dataSourcesIds are the id of the different data source.
     * @return a response with a JSON format of the ranking retrieved.
     */
    @GET
    @Path("/ranking/recalculate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    @RolesAllowed("policy_maker")
    public Response recalculateRanking(@QueryParam("districtId") String districtId, @QueryParam("dataSourcesIds") List<Long> dataSourcesIds) {
        try {
            List<RankingBean> rankings = dataController.createRankingForSelectedDataSets(districtId, dataSourcesIds);
            return Response.ok().entity(gson.toJson(rankings)).build();
        } catch (Exception e) {
            return Response.status(400).entity("{\"success\":0}").build();
        }
    }
}
