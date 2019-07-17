package main.java.service;

import main.java.model.Voter;
import main.java.model.Response;

import java.util.Set;

public interface ElectionService {

    public Response addPerson(Voter p);

    public Response deletePerson(int id);

    public Set<Integer> getAllPersons();

    public Response result(String voterName, int voterID);

    public Response vote(String voterName, int voterID);

}
