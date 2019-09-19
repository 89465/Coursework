import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ActivityController {
    // Add a new user to the database
    public static void addActivity(int userID, String activity, String startDate, String startTime, String endDate, String endTime) {
        try {

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Activities (userID, activity, startDate, startTime, endDate, endTime) VALUES (?, ?, ?, ?, ?, ?)");

            ps.setInt(1, userID);
            ps.setString(2, activity);
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
    public static void removeActivity(int ActivityID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Activities WHERE ActivityID = ?");
            ps.setInt(1, ActivityID);
            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }


    // Print the entire database to console
    public static void listActivities() {
        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT ActivityID, UserID, Activity, StartDate, StartTime, EndDate, EndTime FROM Activities");

            ResultSet results = ps.executeQuery();
            System.out.println("Activities:");
            while (results.next()) {
                int activityID = results.getInt(1);
                int userID = results.getInt(2);
                String activity = results.getString(3);
                String startDate = results.getString(4);
                String startTime = results.getString(5);
                String endDate = results.getString(6);
                String endTime = results.getString(7);

                System.out.println(activityID + ":  " + userID + " " + activity + " " + startDate + " " + startTime + " " + endDate + " " + endTime);
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

}
