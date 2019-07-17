package main.java.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="person")
public class Voter {
    private String name;
    private int age;
    private int voterID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVoterID() {
        return voterID;
    }

    public void setVoterID(int voterID) {
        this.voterID = voterID;
    }

    @Override
    public String toString(){
        return voterID +"::"+name+"::"+age;
    }

}
