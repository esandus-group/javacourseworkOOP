package SCMS.Objects;

import java.util.ArrayList;
import java.util.Iterator;

public class ClubAdvisor extends Person{
    private ArrayList managingClubs = new ArrayList<>(4);

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

    public boolean addClub(Club newClub){
        if (this.managingClubs.size()<5) {
            this.managingClubs.add(newClub);
            return true;
        }

        return false;
    }

    public Boolean  assignNewAdvisor(ClubAdvisor advisor,Club club){    //10. calling assign advisor method
        ArrayList<Club> clubsOfNewAdvisor = advisor.getManagingClubs();
        if (clubsOfNewAdvisor.size() == 4){
            return false;
        }
        else {
            this.managingClubs.remove(club);
            clubsOfNewAdvisor.add(club);
            return true;
        }
    }
    public void scheduleNewEvent(Club club, Event event){
        club.getClubFunctions().add(event);
    }
    public void removeStudent(String studentId, Club club){

        ArrayList<Student> studentsPresent;
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
