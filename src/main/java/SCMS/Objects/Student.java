package SCMS.Objects;

import javafx.scene.control.CheckBox;

import java.util.ArrayList;

public class Student extends Person{


    private ArrayList<Club> clubsJoined = new ArrayList<>();
    private CheckBox checkBox;

    public Student(String id, String firstName, String lastName, String dateOfBirth, String password) {
        super(id, firstName, lastName, dateOfBirth,password);

    }
    public Student(String id, String firstName, String lastName, String dateOfBirth, String password, ArrayList<Club> clubsJoined) {
        super(id, firstName, lastName, dateOfBirth,password);
        this.clubsJoined = clubsJoined;

    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public Student(String id, String firstName, String lastName, String dateOfBirth, String password, CheckBox checkBox) {
        super(id, firstName, lastName, dateOfBirth, password);
        this.checkBox =  checkBox;
    }
    public void joinClub(Club club) {
        clubsJoined.add(club);
        //add student to the club eke arraylist
    }

    public void leaveClub(Club club) {
        clubsJoined.remove(club);
    }

    @Override
    public void displayInfo() {
        System.out.println("Student details:");
        System.out.println("ID: " + this.getId());
        System.out.println("Name: " + this.getFirstName() + " " + this.getLastName());
        System.out.println("Date of Birth: " + this.getDateOfBirth());
        System.out.println("Clubs Joined: " + clubsJoined);
    }
    public ArrayList<Club> getClubsJoined() {
        return clubsJoined;
    }

    public void setClubsJoined(ArrayList<Club> clubsJoined) {
        this.clubsJoined = clubsJoined;
    }
}
