package SCMS.Controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClubAdvisorLoginControllerTest {
        @Test
        public void checkingIfTheUserInputIsNull(){
            ClubAdvisorLoginController controller = new ClubAdvisorLoginController();
            assertFalse(controller.isInputNotNull(""));
        }
        @Test
        public void checkingIfTheUserInputIsNullCorrect(){
            ClubAdvisorLoginController controller = new ClubAdvisorLoginController();
            assertTrue(controller.isInputNotNull("T1"));
        }

}
