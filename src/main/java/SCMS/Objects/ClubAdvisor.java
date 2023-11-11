package SCMS.Objects;

import java.util.ArrayList;
import java.util.Iterator;

public class ClubAdvisor extends Person{
    private ArrayList<Club> managingClubs = new ArrayList<Club>(4);

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

    public void addClub(Club newClub){
        this.managingClubs.add(newClub);
    }

    public Boolean  assignNewAdvisor(ClubAdvisor advisor,Club club){
        this.managingClubs.remove(club);
        ArrayList clubsOfNewAdvisor = advisor.getManagingClubs();
        if (clubsOfNewAdvisor.size() == 4){
            return false;
        }
        else {
            clubsOfNewAdvisor.add(club);
            return true;
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
