package SCMS.Objects;

import SCMS.Utils.SCMSEnvironment;
import javafx.scene.control.Button;

import java.sql.*;
import java.time.LocalDateTime;

public class Event {
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection();
    private String eventId;
    private String title;
    private LocalDateTime dateTime;
    private String venue;
    private String type;
    private String description;
    private String clubId;

    private Button button;
    public Event(String title, LocalDateTime dateTime, String venue, String type, String description, String clubId) {

        this.eventId = "E" + String.valueOf(getNoOfEventsFromSqlDB()+1);
        this.title = title;
        this.dateTime = dateTime;
        this.venue = venue;
        this.type = type;
        this.description = description;
        this.clubId = clubId;
        System.out.println(title+ venue+description+ clubId+type);
        this.button = new Button("mark attendance");
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Event(String funcType, String title, LocalDateTime dateTime, String venue){
        this.type = funcType;
        this.title = title;
        this.dateTime = dateTime;
        this.venue = venue;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public Integer getNoOfEventsFromSqlDB() {

        String query = "SELECT COUNT(*) AS record_count FROM Event";

        try (Statement statement = connections.createStatement();
             ResultSet resultSet = statement.executeQuery(query)){
            int recordCount = 0;
            if (resultSet.next()) {
                recordCount = resultSet.getInt("record_count");
                System.out.println("Total number of records in the table: " + recordCount);

            }
            return (recordCount);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
            // Handle the exception here
        }
    }

    public void writeEventsToSqlDB(){
        try {
            String query = "INSERT INTO Event (eventId, title, dateTime, venue, typeOfClubFunction, description, clubId) VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Create a PreparedStatement
            PreparedStatement statement = connections.prepareStatement(query);

            // Set the values for the parameters in the SQL query
            statement.setString(1, this.eventId);
            statement.setString(2, this.title);
            statement.setObject(3, this.dateTime); // LocalDateTime to SQL DATETIME
            statement.setString(4, this.venue);
            statement.setString(5, this.type);
            statement.setString(6, this.description);
            statement.setString(7, this.clubId);


            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }






    }



}
