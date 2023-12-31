package SCMS.Objects;

import java.util.ArrayList;

public class Club {
    private String clubId;
    private String name;
    private ArrayList<Student> studentsPresent = new ArrayList<Student>();

    private ArrayList<Event> clubFunctions = new ArrayList<>();

    public Club(String clubId,String name){
        this.clubId=clubId;
        this.name=name;
    }

    public Club(String clubId,String name,ArrayList<Student> studentsPresent){
        this.clubId=clubId;
        this.name=name;
        this.studentsPresent=studentsPresent;
    }
    public Club(String clubId,String name,ArrayList<Student> studentsPresent,ArrayList<Event> clubFunctions){
        this.clubId=clubId;
        this.name=name;
        this.studentsPresent=studentsPresent;
        this.clubFunctions=clubFunctions;
    }

    public ArrayList<Event> getClubFunctions() {
        return clubFunctions;
    }

    public void setClubFunctions(ArrayList<Event> clubFunctions) {
        this.clubFunctions = clubFunctions;
    }

    public String getClubId() {
        return clubId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Student> getStudentsPresent() {
        return studentsPresent;
    }

    public void setStudentsPresent(ArrayList<Student> studentsPresent) {
        this.studentsPresent = studentsPresent;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Club{" +
                "clubId='" + clubId + '\'' +
                ", name='" + name + '\'' +
                ", studentsPresent=" + studentsPresent +
                ", clubFunctions=" + clubFunctions +
                '}';
    }

}
