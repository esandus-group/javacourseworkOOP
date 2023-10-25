package SCMS;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ClubAdvisor extends Person{
    Club clubToDelete=null;
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
    public void deleteClub(int clubId){
        boolean found = false;

        Iterator<Club> iterator = managingClubs.iterator();
        while (iterator.hasNext()) {
            Club club = iterator.next();
            if (clubId == club.getClubId()) {
                iterator.remove();
                clubToDelete=club;
                found = true;
                break;
            }
        }
        if (!found == true){
            System.out.println("club not found to delete");
        }
        else{
            managingClubs.remove(clubToDelete);
        }
    }
    public void removeStudent(){

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
