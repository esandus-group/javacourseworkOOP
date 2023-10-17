package SCMS;

import java.util.ArrayList;
import java.util.Date;

public class ClubAdvisor extends Person{
    private ArrayList<Club> managingClubs = new ArrayList<Club>();

    private final int advisorId;

    public ClubAdvisor(int id, int advisorId, String firstName, String lastName, Date dateOfBirth, int advisorId1){
        super(id, firstName, lastName, dateOfBirth);
        this.advisorId = advisorId1;
    }

    public ArrayList<Club> getManagingClubs() {
        return managingClubs;
    }

    public int getAdvisorId() { //hello
        return advisorId;
    }
    public void createClub(){
        // get the stuff from the text fields,
        // create the club object
        // make it available for the students to join

    }

    @Override
    public void displayInfo() {
        System.out.println("Club Advisor details:");
        System.out.println("ID: " + this.getId());
        System.out.println("Advisor ID:" +this.getAdvisorId());
        System.out.println("Name: " + this.getFirstName() + " " + this.getLastName());
        System.out.println("Date of Birth: " + this.getDateOfBirth());
        System.out.println("Managed Club: " + managingClubs);
    }

}
