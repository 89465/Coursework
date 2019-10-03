package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("users/")
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
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listUsers() {
        JSONArray list = new JSONArray();
        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Username, DOB FROM Usernames");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("userID", results.getInt(1));
                item.put("username", results.getString(2));
                item.put("DOB", results.getString(3));
                list.add(item);
            }
            return list.toString();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

}
