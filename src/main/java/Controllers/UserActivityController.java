package Controllers;

import Server.Main;
//import jdk.swing.interop.SwingInterOpUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("users/activities/")
public class UserActivityController {
    // Add a new user to the database
    @POST
    @Path("add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String addActivity(@FormDataParam("userID") int userID, @FormDataParam("activityID") int activityID, @FormDataParam("startDate") String startDate, @FormDataParam("startTime") String startTime, @FormDataParam("endDate") String endDate, @FormDataParam("endTime") String endTime) {
        try {

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO UserActivities (userID, activityID, startDate, startTime, endDate, endTime) VALUES (?, ?, ?, ?, ?, ?)");

            ps.setInt(1, userID);
            ps.setInt(2, activityID);
            ps.setString(3, startDate);
            ps.setString(4, startTime);
            ps.setString(5, endDate);
            ps.setString(6, endTime);


            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }

    // Update an existing user in the database
    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateActivity(@FormDataParam("userActivityID") int userActivityID, @FormDataParam("userID") int userID, @FormDataParam("activityID") int activityID, @FormDataParam("startDate") String startDate, @FormDataParam("startTime") String startTime, @FormDataParam("endDate") String endDate, @FormDataParam("endTime") String endTime) {
        try {

            PreparedStatement ps = Main.db.prepareStatement("UPDATE UserActivities SET userID=?, activityID=?, startDate=?, startTime=?, endDate=?, endTime=? WHERE UserActivityID=?");

            ps.setInt(1, userID);
            ps.setInt(2, activityID);
            ps.setString(3, startDate);
            ps.setString(4, startTime);
            ps.setString(5, endDate);
            ps.setString(6, endTime);
            ps.setInt(7, userActivityID);


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
    public String removeActivity(@FormDataParam("userActivityID") int userActivityID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM UserActivities WHERE userActivityID = ?");
            ps.setInt(1, userActivityID);
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

            PreparedStatement ps = Main.db.prepareStatement("SELECT UserActivityID, UserActivities.UserID, Usernames.Username, UserActivities.ActivityID, Description, StartDate, StartTime, EndDate, EndTime " +
                    "FROM UserActivities INNER JOIN Activities ON UserActivities.ActivityID=Activities.ActivityID" +
                    " INNER JOIN Usernames ON UserActivities.UserID=Usernames.UserID;");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("userActivityID", results.getInt(1));
                item.put("userID", results.getInt(2));
                item.put("username", results.getString(3));
                item.put("activityID", results.getInt(4));
                item.put("activityName", results.getString(5));
                item.put("startDate", results.getString(6));
                item.put("startTime", results.getString(7));
                item.put("endDate", results.getString(8));
                item.put("endTime", results.getString(9));
                list.add(item);
            }
            return list.toString();


        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

}
