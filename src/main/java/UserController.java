import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserController {
    // Add a new user to the database
    public static void addUser(String username, String DOB) {
        try {

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Usernames (username, DOB) VALUES (?, ?)");

            ps.setString(1, username);
            ps.setString(2, DOB);

            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    // Removes a user from the database
    public static void removeUser(int UserID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Usernames WHERE UserID = ?");
            ps.setInt(1, UserID);
            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }


    // Print the entire database to console
    public static void listUsers() {
        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Username, DOB FROM Usernames");

            ResultSet results = ps.executeQuery();
            System.out.println("Usernames:");
            while (results.next()) {
                int userID = results.getInt(1);
                String username= results.getString(2);
                String dob = results.getString(3);

                System.out.println(userID + ":  " + username + " " + dob);
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

}
