package SCMS.Objects;

import java.util.ArrayList;

public class Club {
    private String clubId;
    private String name;
    private ArrayList<Student> studentsPresent = new ArrayList<Student>();

    private String idOfAdvisor;

    public Club(String clubId,String name,String idOfAdvisor){
        this.clubId=clubId;
        this.name=name;
        this.idOfAdvisor=idOfAdvisor;
    }
    public Club(String clubId,String name,String idOfAdvisor,ArrayList<Student> studentsPresent){
        this.clubId=clubId;
        this.name=name;
        this.idOfAdvisor=idOfAdvisor;
        this.studentsPresent=studentsPresent;
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

    public String getIdOfAdvisor() {
        return idOfAdvisor;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdOfAdvisor(String idOfAdvisor) {
        this.idOfAdvisor = idOfAdvisor;
    }

    @Override
    public String toString(){
        return "club ID: "+this.getClubId()+",Name: "+this.getName()+" ,Id of Club advisor: "+this.getIdOfAdvisor();
    }

}
