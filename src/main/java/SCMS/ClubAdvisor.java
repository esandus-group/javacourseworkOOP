package SCMS;

import java.util.ArrayList;
import java.util.Date;

public class ClubAdvisor extends Person{
    private ArrayList<Club> managingClubs = new ArrayList<Club>();


    public ClubAdvisor(int id, String firstName, String lastName, Date dateOfBirth,String password){
        super(id, firstName, lastName, dateOfBirth,password);

    }

    public ArrayList<Club> getManagingClubs() {
        return managingClubs;
    }


    public Club createClub(int clubId,String clubName,int id){
        Club newClub = new Club(clubId,clubName,id);
        managingClubs.add(newClub);
        return newClub;
    }


    @Override
    public void displayInfo() {
        System.out.println("Club Advisor details:");
        System.out.println("ID: " + this.getId());
        System.out.println("Name: " + this.getFirstName() + " " + this.getLastName());
        System.out.println("Date of Birth: " + this.getDateOfBirth());
        System.out.println("Managed Club: " + managingClubs);
    }

}
