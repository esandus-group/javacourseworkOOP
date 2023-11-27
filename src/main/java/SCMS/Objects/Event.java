package SCMS.Objects;

import SCMS.Utils.SCMSEnvironment;
import javafx.scene.control.Button;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

// Event class represents an event in the system
public class Event {
    // Establish a connection to the SQL database
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection();
    // Event properties
    private String eventId;
    private String title;
    private LocalDateTime dateTime;
    private String venue;
    private String type;
    private String description;
    private ArrayList<Student> studentsWhoJoined = new ArrayList<>();
    private Button button;

    // Constructor for creating an event without a button
    public Event(String title, LocalDateTime dateTime, String venue, String type, String description) {
        this.eventId = "E" + String.valueOf(getNoOfEventsFromSqlDB() + 1);
        this.title = title;
        this.dateTime = dateTime;
        this.venue = venue;
        this.type = type;
        this.description = description;
    }

    // Constructor for creating an event with a button
    public Event(String title, LocalDateTime dateTime, String venue, String type, String description, Button button) {
        this.eventId = "E" + String.valueOf(getNoOfEventsFromSqlDB() + 1);
        this.title = title;
        this.dateTime = dateTime;
        this.venue = venue;
        this.type = type;
        this.description = description;
        this.button = button;
        this.button.setText("mark stuff");
    }

    // Constructor for creating an event with a specified eventId and button
    public Event(String eventId, String title, LocalDateTime dateTime, String venue, String type, String description, Button button) {
        this.eventId = eventId;
        this.title = title;
        this.dateTime = dateTime;
        this.venue = venue;
        this.type = type;
        this.description = description;
        this.button = button;
        this.button.setText("mark stuff");
    }

    // Constructor for creating an event with specified eventId, button, and studentsWhoJoined
    public Event(String eventId, String title, LocalDateTime dateTime, String venue, String type, String description, Button button, ArrayList<Student> studentsWhoJoined) {
        this.eventId = eventId;
        this.title = title;
        this.dateTime = dateTime;
        this.venue = venue;
        this.type = type;
        this.description = description;
        this.button = button;
        this.button.setText("mark stuff");
        this.studentsWhoJoined = studentsWhoJoined;
    }

    // Getter for studentsWhoJoined
    public ArrayList<Student> getStudentsWhoJoined() {
        return studentsWhoJoined;
    }

    // Setter for studentsWhoJoined
    public void setStudentsWhoJoined(ArrayList<Student> studentsWhoJoined) {
        this.studentsWhoJoined = studentsWhoJoined;
    }

    // Getter for the button
    public Button getButton() {
        return button;
    }

    // Setter for the button
    public void setButton(Button button) {
        this.button = button;
    }

    // Constructor for creating an event with minimal details
    public Event(String funcType, String title, LocalDateTime dateTime, String venue) {
        this.type = funcType;
        this.title = title;
        this.dateTime = dateTime;
        this.venue = venue;
    }

    // Getter for eventId
    public String getEventId() {
        return eventId;
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for dateTime
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // Getter for venue
    public String getVenue() {
        return venue;
    }

    // Setter for venue
    public void setVenue(String venue) {
        this.venue = venue;
    }

    // Getter for type
    public String getType() {
        return type;
    }

    // Setter for type
    public void setType(String type) {
        this.type = type;
    }

    // Method to get the number of events from the SQL database
    public Integer getNoOfEventsFromSqlDB() {
        String query = "SELECT COUNT(*) AS record_count FROM Event";
        try (Statement statement = connections.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            int recordCount = 0;
            if (resultSet.next()) {
                recordCount = resultSet.getInt("record_count");
                System.out.println("Total number of records in the table: " + recordCount);
            }
            return recordCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to write events to the SQL database
    public void writeEventsToSqlDB(String clubId) {
        try {
            String query = "INSERT INTO Event (eventId, title, dateTime, venue, typeOfClubFunction, description, clubId) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connections.prepareStatement(query);
            statement.setString(1, this.eventId);
            statement.setString(2, this.title);
            statement.setObject(3, this.dateTime);
            statement.setString(4, this.venue);
            statement.setString(5, this.type);
            statement.setString(6, this.description);
            statement.setString(7, clubId);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
