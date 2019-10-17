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

@Path("users/activities/")
public class UserActivityController {
    // Add a new user to the database
    public static void addActivity(int userID, int activityID, String startDate, String startTime, String endDate, String endTime) {
        try {

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO UserActivities (userID, activityID, startDate, startTime, endDate, endTime) VALUES (?, ?, ?, ?, ?, ?)");

            ps.setInt(1, userID);
            ps.setInt(2, activityID);
            ps.setString(3, startDate);
            ps.setString(4, startTime);
            ps.setString(5, endDate);
            ps.setString(6, endTime);


            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    // Removes a user from the database
    public static void removeActivity(int UserActivityID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM UserActivities WHERE UserActivityID = ?");
            ps.setInt(1, UserActivityID);
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

            PreparedStatement ps = Main.db.prepareStatement("SELECT UserActivityID, UserID, UserActivities.ActivityID, Description, StartDate, StartTime, EndDate, EndTime FROM UserActivities INNER JOIN Activities ON UserActivities.ActivityID=Activities.ActivityID;");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("userActivityID", results.getInt(1));
                item.put("userID", results.getInt(2));
                item.put("activityID", results.getInt(3));
                item.put("activityName", results.getString(4));
                item.put("startDate", results.getString(5));
                item.put("startTime", results.getString(6));
                item.put("endDate", results.getString(7));
                item.put("endTime", results.getString(8));
                list.add(item);
            }
            return list.toString();


        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

}
