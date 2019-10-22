package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("users/")
public class UserController {
    // Add a new user to the database
    @POST
    @Path("add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String addUser(@FormDataParam("username") String username, @FormDataParam("DOB") String DOB) {
        try {

            if(username == null || DOB == null)
                throw new Exception("One or more form data parameters are missing in the HTTP request.");

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Usernames (username, DOB) VALUES (?, ?)");

            ps.setString(1, username);
            ps.setString(2, DOB);

            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
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
