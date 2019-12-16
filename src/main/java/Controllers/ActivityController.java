package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Path("activities/")
public class ActivityController {
    // Add a new user to the database
    @POST
    @Path("add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String addActivity(@FormDataParam("description") String description, @FormDataParam("productive") boolean productive) {
        try {

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Activities (description, productive) VALUES (?, ?)");

            ps.setString(1, description);
            ps.setBoolean(2, productive);


            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }

    // Removes a user from the database
    @POST
    @Path("remove")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String removeActivity(@FormDataParam("activityID") int activityID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Activities WHERE activityID = ?");
            ps.setInt(1, activityID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
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
                System.out.println(item.get("activityID") + ": " + item.get("description") + " (" + item.get("productive") + ")");
            }
            return list.toString();


        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}
