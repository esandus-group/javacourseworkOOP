package SCMS.Utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;

public class SCMSEnvironment {
    private Dotenv env = Dotenv.configure().load();
    private static SCMSEnvironment instance = null;

    public static synchronized SCMSEnvironment getInstance(){
        if (instance == null) {
            SCMSEnvironment newEnvironment = new SCMSEnvironment();
            return newEnvironment;
        }
        return instance;
    }

    public String getSqlConnectionString(){
        return env.get("MYSQL_DB_URL");
    }
    public Connection makeSqlDBConnection(){
        try {
            // Initialize the database connection when the controller is created
//
//            Dotenv env = Dotenv.configure().load();
//            String url = env.get("MYSQL_DB_URL");
//            String username = "root";
//            String password = "";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connections = DriverManager.getConnection(SCMSEnvironment.getInstance().getSqlConnectionString());
            return connections;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    
    }
}
