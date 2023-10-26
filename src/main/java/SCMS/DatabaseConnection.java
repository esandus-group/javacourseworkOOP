package SCMS;

import java.sql.*;

public class DatabaseConnection {

    private Connection connections;
    private Statement statement;
    //mf
    public DatabaseConnection() throws Exception {

        String url = "jdbc:mysql://localhost:3306/school_club_management";
        String username = "root";
        String password = "esandu12345";
        Class.forName("com.mysql.cj.jdbc.Driver");
        connections = DriverManager.getConnection(url,username,password);
        statement = connections.createStatement();
        ResultSet st_IDs = statement.executeQuery("SELECT firstName, lastName FROM ClubAdvisor WHERE id = 'T1'");}
//    public void printtt(ResultSet st_IDs) throws SQLException {
//        while (st_IDs.next()) {
//            String firstName = st_IDs.getString("firstName");
//            String lastName = st_IDs.getString("lastName");
//
//            System.out.println("First Name: " + firstName + ", Last Name: " + lastName);
//        }


    //}

    public void removeStudent() throws SQLException {
//        Scanner input = new Scanner(System.in);
//        int St_ID = input.nextInt();
        try {
            String sql = "SELECT firstName, lastName FROM ClubAdvisor WHERE id = 'T1'";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                System.out.println("First Name: " + firstName + ", Last Name: " + lastName);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("ff");
//
////            int new_ID = input.nextInt();
////            addStudent(new_ID,fName,lName,t_index);
        }
//
//        statement.close();
//        connections.close();
//
//    }
//    public void addStudent(int index, String fName, String lName,int [][] module_teacher) throws SQLException {
//        Scanner input = new Scanner(System.in);
//        try{
//            statement.executeUpdate("INSERT INTO Student_records (St_ID, F_Name, L_Name, T_ID)\n" +
//                    "VALUES (" + index + ",'" + fName + "','" + lName + ");");
//            for (int[][]:i module_teacher) {
//
//            }
//            statement.executeUpdate("INSERT INTO St_M_T");
//        } catch(SQLIntegrityConstraintViolationException e){
//            System.out.println("Following St_ID " +  index + " already exit in the system. Enter a new St_ID : ");
//
//            int new_ID = input.nextInt();
//            addStudent(new_ID,fName,lName,module_teacher);
//        }
//
//        statement.close();
//        connections.close();
//
//    }
//
//    public void removeStudent(int index) throws SQLException {
////        Scanner input = new Scanner(System.in);
////        int St_ID = input.nextInt();
//        try{
//            statement.executeUpdate("DELETE FROM Student_recods WHERE St_ID =" + index);
//        } catch(SQLIntegrityConstraintViolationException e){
//            System.out.println("Following St_ID " +  index + " already exit in the system. Enter a new St_ID : ");
//
////            int new_ID = input.nextInt();
////            addStudent(new_ID,fName,lName,t_index);
//        }
//
//        statement.close();
//        connections.close();
//
//    }
    }}
