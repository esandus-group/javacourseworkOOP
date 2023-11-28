package SCMS.Controllers;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
class MainLoginPageControllerTest {
    @Test
    public void isStudentValid() throws Exception {
        MainLoginPageController con = new MainLoginPageController();
        boolean result = con.isStudentValid("S1", "student1password");
        assertTrue(result);
    }
    @Test
    public void studentNotValid() throws Exception {
        MainLoginPageController con = new MainLoginPageController();
        boolean result = con.isStudentValid("S2", "student1password");
        assertFalse(result);
    }
    @Test
    public void studentValidNull() throws Exception {
        MainLoginPageController con = new MainLoginPageController();
        boolean result = con.isStudentValid("", "");
        assertFalse(result);
    }
}