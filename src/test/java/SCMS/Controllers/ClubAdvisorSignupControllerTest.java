package SCMS.Controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClubAdvisorSignupControllerTest {

    @Test
    public void checkingIfTheUserInputIsNull(){
        ClubAdvisorSignupController controller = new ClubAdvisorSignupController();
        assertFalse(controller.isInputNotNull(""));
    }
    @Test
    public void checkingIfTheUserInputIsNullCorrect(){
        ClubAdvisorSignupController controller = new ClubAdvisorSignupController();
        assertTrue(controller.isInputNotNull("T1"));
    }

}