package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("users/")
public class UserController {
    // Add a new user to the database
    @POST
    @Path("add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String addUser(@FormDataParam("username") String username, @FormDataParam("password") String password, @FormDataParam("DOB") String DOB) {
        try {

            if(username == null || DOB == null)
                throw new Exception("One or more form data parameters are missing in the HTTP request.");

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Usernames (username, password, DOB) VALUES (?, ?, ?)");

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, DOB);

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
    public String updateActivity(@FormDataParam("userID") int userID, @FormDataParam("password") String password, @FormDataParam("DOB") String DOB) {
        try {

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Usernames SET password=?, DOB=? WHERE userID=?");


            ps.setString(1, password);
            ps.setString(2, DOB);
            ps.setInt(3, userID);


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
    public String removeUser(@FormDataParam("userID") int UserID) {
        try {
            if (UserID == 0)
                throw new Exception("One or more form data parameters are missing in the HTTP request.");

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Usernames WHERE UserID = ?");
            ps.setInt(1, UserID);
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
    public String listUsers() {
        JSONArray list = new JSONArray();
        try {

            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Username, Password, DOB FROM Usernames");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("userID", results.getInt(1));
                item.put("username", results.getString(2));
                item.put("password", results.getString(3));
                item.put("DOB", results.getString(4));
                list.add(item);
            }
            return list.toString();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(@FormDataParam("username") String username, @FormDataParam("password") String password) {

        try {

            System.out.println("user/login");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Password FROM Usernames WHERE Username = ?");
            ps1.setString(1, username);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next()) {

                String correctPassword = loginResults.getString(1);
                if (password.equals(correctPassword)) {

                    String token = UUID.randomUUID().toString();

                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Usernames SET Token = ? WHERE Username = ?");
                    ps2.setString(1, token);
                    ps2.setString(2, username);
                    ps2.executeUpdate();

                    PreparedStatement ps3 = Main.db.prepareStatement("SELECT UserID FROM Usernames WHERE Username = ?");
                    ps3.setString(1, username);
                    ResultSet results = ps3.executeQuery();

                    JSONObject userDetails = new JSONObject();
                    userDetails.put("userID", results.getInt(1));
                    userDetails.put("username", username);
                    userDetails.put("token", token);
                    return userDetails.toString();

                } else {
                    return "{\"error\": \"Incorrect password!\"}";
                }

            } else {
                return "{\"error\": \"Unknown user!\"}";
            }

        } catch (Exception exception){
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutUser(@CookieParam("token") String token) {

        try {

            System.out.println("user/logout");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT UserID FROM Usernames WHERE Token = ?");
            ps1.setString(1, token);
            ResultSet logoutResults = ps1.executeQuery();
            if (logoutResults.next()) {

                int id = logoutResults.getInt(1);

                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Usernames SET Token = NULL WHERE UserID = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                return "{\"status\": \"OK\"}";
            } else {

                return "{\"error\": \"Invalid token!\"}";

            }

        } catch (Exception exception){
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }

    }


                public static boolean validToken(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        } catch (Exception exception) {
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return false;
        }
    }
}
