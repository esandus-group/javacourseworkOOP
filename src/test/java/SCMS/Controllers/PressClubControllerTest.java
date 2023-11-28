package SCMS.Controllers;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class PressClubControllerTest {

    //assigning advisor confirmation======================================
    @Test
    public void checkingIfTheUserHasConfirmed(){
        PressClubController controller = new PressClubController();
        assertFalse(controller.isActionConfirmed(""));
    }
    @Test
    public void checkingIfTheUserHasConfirmedWrongInput(){
        PressClubController controller = new PressClubController();
        assertFalse(controller.isActionConfirmed("hahaha hehe"));
    }
    @Test
    public void checkingIfTheUserHasConfirmedCorrectInput(){
        PressClubController controller = new PressClubController();
        assertTrue(controller.isActionConfirmed("CONFIRM"));
    }
    //assigning advisor Id===========================================
    @Test
    public void newAdvisorIdIsNull() throws SQLException {
        PressClubController controller = new PressClubController();
        assertFalse(controller.isNewAdvisorCorrect(""));
    }
    @Test
    public void newAdvisorIsCorrect() throws SQLException {
        PressClubController controller = new PressClubController();
        assertTrue(controller.isNewAdvisorCorrect("T2"));
    }




}