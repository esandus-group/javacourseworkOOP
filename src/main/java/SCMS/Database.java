package SCMS;

import java.util.ArrayList;

public class Database {
    public ArrayList<Club> allClubs = new ArrayList<>();

    public ArrayList<Club> allAdvisors = new ArrayList<>();

    public ArrayList<Club> getAllAdvisors() {
        return allAdvisors;
    }

    public ArrayList<Club> getAllClubs() {
        return allClubs;
    }
    public Database(){
        this.allClubs=null;
    }


}
