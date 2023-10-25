package SCMS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class createClubController {
    @FXML
    private TextField clubName;
    @FXML
    private TextField clubAdvisorID;

    public int clubAdvisorId;

    public ArrayList<ClubAdvisor> allClubAdvisors = new ArrayList<>();
    ClubAdvisor currentClubAdvisor=null;
    public ArrayList<Club> allClubs = new ArrayList<>();
    public int clubId;
    public String clubNam;
    //=========================================================================
    public void onSaveNewClubClick(ActionEvent event) throws Exception{
        clubNam = clubName.getText();                               //getting the information
        clubAdvisorId = Integer.parseInt(clubAdvisorID.getText());

        Iterator<ClubAdvisor> iterators = allClubAdvisors.iterator();//getting the current using advisor
        while (iterators.hasNext()) {
            ClubAdvisor gettingClubAdvisor = iterators.next();
            if (gettingClubAdvisor.getId() == clubAdvisorId) {
                currentClubAdvisor = gettingClubAdvisor;
                break;
            }
        }

        boolean found = true;
        for (Club club:allClubs){                                   //checking if club already exits
            if (Objects.equals(clubNam, club.getName())){
                found = true;
                System.out.println("club with this name, already exits");
            }
        }

        if (found !=true){                                          // if the club doesn't exit
            allClubs.add(currentClubAdvisor.createClub(clubId,clubNam,clubAdvisorId));
        }
    }
    //=========================================================================
}

