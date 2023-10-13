package SCMS;

import java.util.ArrayList;
import java.util.Date;

public class Student extends Person{

    private final int admissionNo;
    private ArrayList<String> clubsJoined = new ArrayList<>();

    public Student(int id,int admissionNo, String firstName, String lastName, Date dateOfBirth) {
        super(id, firstName, lastName, dateOfBirth);
        this.admissionNo=admissionNo;
    }

    public void joinClub(String clubName) {
        clubsJoined.add(clubName);
        //add student to the club eke arraylist
    }

    public void leaveClub(String clubName) {
        clubsJoined.remove(clubName);
    }

    @Override
    public void displayInfo() {
        System.out.println("Student details:");
        System.out.println("ID: " + this.getId());
        System.out.println("Name: " + this.getFirstName() + " " + this.getLastName());
        System.out.println("Date of Birth: " + this.getDateOfBirth());
        System.out.println("Clubs Joined: " + clubsJoined);
    }

    public ArrayList<String> getClubsJoined() {
        return clubsJoined;
    }

    public int getAdmissionNo() {
        return admissionNo;
    }
}
