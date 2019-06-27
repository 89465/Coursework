import org.sqlite.SQLiteConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

    public static Connection db = null;

    public static void main(String[] args) {
        openDatabase("database1.db");

        printDatabase();

        closeDatabase();
    }

    // Add a new user to the database
    private static void addUser(String username, String DOB) {
        try {

            PreparedStatement ps = db.prepareStatement("INSERT INTO Usernames (username, DOB) VALUES (?, ?)");

            ps.setString(1, username);
            ps.setString(2, DOB);

            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    // Print the entire database to console
    private static void printDatabase() {
        try {

            PreparedStatement ps = db.prepareStatement("SELECT UserID, Username, DOB FROM Usernames");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int userID = results.getInt(1);
                String username= results.getString(2);
                String dob = results.getString(3);
                System.out.println(userID + " " + username + " " + dob);
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    // Open a database
    private static void openDatabase(String dbFile) {
        try  {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }

    }

    // Close the current database
    private static void closeDatabase(){
        try {
            db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }

}
