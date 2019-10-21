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
import java.util.HashMap;
import java.util.Map;

@Path("activities/")
public class ActivityController {
    // Add a new user to the database
    public static void addActivity(int activityID, String description, boolean productive) {
        try {

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Activities (activityID, description, productive) VALUES (?, ?, ?)");

            ps.setInt(1, activityID);
            ps.setString(2, description);
            ps.setBoolean(3, productive);


            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    // Removes a user from the database
    public static void removeActivity(int activityID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Activities WHERE activityID = ?");
            ps.setInt(1, activityID);
            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }


    // Print the entire database to console
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listActivities() {
        JSONArray list = new JSONArray();
        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT activityID, description, productive FROM Activities");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("activityID", results.getInt(1));
                item.put("description", results.getString(2));
                item.put("productive", results.getBoolean(3));
                list.add(item);
            }
            return list.toString();


        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}
