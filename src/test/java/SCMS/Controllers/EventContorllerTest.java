package SCMS.Controllers;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EventContorllerTest {
    //INPUTS BEING EMPTY
    @Test
    public void validateTextFileds_emptyFields_returnsErrorMessage() {
        EventContorller controller = new EventContorller();
        String result = controller.validateTextFileds("", "Venue", "Description", "Event");
        assertEquals("Fields cannot be empty", result);
    }
    @Test
    public void validateTextFileds_validInput_returnsNull() {
        EventContorller controller = new EventContorller();
        String result = controller.validateTextFileds("Title", "Venue", "Description", "Activity");

        assertEquals("",result);
    }
    // WRONG INPUTS BEING ENTERED
    @Test
    public void validateTimeAndTypeOfEvent_validInput_returnsNull() { //in this case all correct
        EventContorller controller = new EventContorller();
        LocalDate date = LocalDate.now();
        String result = controller.validateTimeAndTypeOfEvent(date, "6", "30", "Event");
        assertEquals("",result);
    }
}