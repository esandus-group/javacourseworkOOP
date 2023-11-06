package SCMS.Objects;

import java.util.ArrayList;
import java.util.Iterator;

public class ClubAdvisor extends Person{
    Club clubToDelete=null;
    private ArrayList<Club> managingClubs = new ArrayList<Club>();

    public ClubAdvisor(String id, String firstName, String lastName, String dateOfBirth,String password){
        super(id, firstName, lastName, dateOfBirth,password);
    }
    public ClubAdvisor(String id, String firstName, String lastName, String dateOfBirth,String password,ArrayList managingClubs){
        super(id, firstName, lastName, dateOfBirth,password);
        this.managingClubs = managingClubs;
    }
    public ArrayList<Club> getManagingClubs() {
        return managingClubs;
    }

    public Club createClub(String clubId,String clubName,String id){
        Club newClub = new Club(clubId,clubName,id);
        this.managingClubs.add(newClub);
        return newClub;
    }
    public void deleteClub(String clubId){
        boolean found = false;

        Iterator<Club> iterator = managingClubs.iterator();
        while (iterator.hasNext()) {
            Club club = iterator.next();
            if (clubId == club.getClubId()) {
                iterator.remove();
                break;
            }
        }

    }

    public void removeStudent(String studentId, Club club){

        ArrayList<Student> studentsPresent = new ArrayList<>();
        studentsPresent = club.getStudentsPresent();
        Iterator<Student> iterator = studentsPresent.iterator();
        while (iterator.hasNext()) {
            Student student= iterator.next();
            if (studentId == student.getId()) {
                iterator.remove();
                club.setStudentsPresent(studentsPresent);
                break;
            }
        }
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
