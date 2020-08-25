package com.revature.banking.services;

import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.models.AppUser;
import com.revature.banking.screens.HomeScreen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * RegistrationService
 * Checks for taken emails on db. Builds sql query to add new app_user
 */

public class RegistrationService{
    //eager singleton
    private static RegistrationService registrationService = new RegistrationService();

    private RegistrationService() {
        super();
    }

    public static RegistrationService getInstance() {
        return registrationService;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     *
     * @param email Checks on db for existing users via email
     * @return boolean on whether email exists on db or not
     */

    private boolean checkDuplicateEmail(String email){
        boolean exists = false;
        Optional<AppUser> eAddress = Optional.empty();

        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = "SELECT * FROM project0.app_users au " +
                        "WHERE email = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                exists = true;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return exists;
    }

    /**
     *
     * @param email proposed email for registration
     * @param password user's new password
     * @return Returns newly created user from given email/password,
     * sets ID in next screen from serialized column in db
     */

    public AppUser register(String email, String password){
        AppUser user = null;
        //Check to see if email already taken
        if(checkDuplicateEmail(email)){
            System.err.println("Email taken.");
            RouterService.getInstance().route("/home"); //back to start
        } else {

            try (Connection conn = ConnectionService.getInstance().getConnection()) {
                String sql = "INSERT INTO project0.app_users (email, password) " +
                        "VALUES (?, ?)";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                pstmt.executeUpdate();


            } catch (SQLException sqle) {
                sqle.printStackTrace();
                System.err.println("Something went wrong.");
            }
            user = new AppUser(email, password);
        }
        return user;
    }

}
