package main.java.service;


import main.java.model.Voter;
import main.java.model.Response;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Path("/election")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class ElectionServiceImpl implements ElectionService {

    private static Map<Integer, Integer> persons = new HashMap<>();

    @Override
    @POST
    @Path("/add")
    public Response addPerson(Voter p) {
        Response response = new Response();
        if (persons.get(p.getVoterID()) != null) {
            response.setVotes("Voter Already Exists");
            return response;
        }
        persons.put(p.getVoterID(), 0);
        response.setVotes("Voter created successfully");
        return response;
    }

    @Override
    @GET
    @Path("/{id}/delete")
    public Response deletePerson(@PathParam("id") int id) {
        Response response = new Response();
        if (persons.get(id) == null) {
            response.setVotes("Voter Doesn't Exists");
            return response;
        }
        persons.remove(id);
        response.setVotes("Voter deleted successfully");
        return response;
    }

    @Override
    @GET
    @Path("/getAll")
    public Set<Integer> getAllPersons() {
        Set<Integer> ids = persons.keySet();
        return ids;
    }

    @Override
    @GET
    @Path(("/{voterName}/{voterID}/result"))
    public Response result(String voterName, int voterID) {
        Response response = new Response();
        response.setPersonName(voterName);
        if (persons.containsKey(voterID)) {
            response.setVotes(persons.get(voterID).toString());
            response.setExists(true);
        } else {
            response.setExists(false);
        }

        return response;
    }

    @Override
    @POST
    @Path(("/{voterName}/{voterID}/vote"))
    public Response vote(String voterName, int voterID) {
        int votes;
        Response response = new Response();
        if (persons.containsKey(voterID)) {
            votes = persons.get(voterID);
            votes++;
            persons.put(voterID, votes);
            response.setExists(true);
            response.setPersonName(voterName);
        } else {
            response.setVotes("0");
            response.setExists(false);
        }

        return response;
    }

}
