package main.java.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response {

    private String votes;
    private String personName;
    private boolean isExists;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public boolean isExists() {
        return isExists;
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }
}

