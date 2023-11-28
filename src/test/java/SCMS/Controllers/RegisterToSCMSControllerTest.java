package SCMS.Controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterToSCMSControllerTest {
    @Test
    public void IfStudentIdNull() throws Exception{
        RegisterToSCMSController con = new RegisterToSCMSController();
        boolean result = con.isStudentIdValid(null);
        assertFalse(result);
    }
    @Test
    public void IfStudentDOBNull() throws Exception{
        RegisterToSCMSController con = new RegisterToSCMSController();
        boolean result = con.isStudentDOBValid("");
        assertFalse(result);
    }
    @Test
    public void IfStudentFNameNull() throws Exception{
        RegisterToSCMSController con = new RegisterToSCMSController();
        boolean result = con.isStudentFNameValid("");
        assertFalse(result);
    }
    @Test
    public void IfStudentLNameNull() throws Exception{
        RegisterToSCMSController con = new RegisterToSCMSController();
        boolean result = con.isStudentLNameValid("");
        assertFalse(result);
    }
    @Test
    public void IfStudentIPasswordNull() throws Exception{
        RegisterToSCMSController con = new RegisterToSCMSController();
        boolean result = con.isStudentPasswordValid("");
        assertFalse(result);
    }
}