package SCMS;

import java.util.ArrayList;

public class Club {
    private int clubId;
    private String name;
    private ArrayList<Student> studentsPresent = new ArrayList<Student>();

    private String idOfAdvisor;

    public Club(int clubId,String name,String idOfAdvisor){
        this.clubId=clubId;
        this.name=name;
        this.idOfAdvisor=idOfAdvisor;
    }

    public int getClubId() {
        return clubId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Student> getStudentsPresent() {
        return studentsPresent;
    }

    public String getIdOfAdvisor() {
        return idOfAdvisor;
    }

    public void setClubId(int clubId) {
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
