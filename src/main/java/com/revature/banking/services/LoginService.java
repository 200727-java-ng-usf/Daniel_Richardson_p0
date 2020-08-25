package com.revature.banking.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.banking.models.AppUser;
import com.revature.banking.screens.HomeScreen;


/**
 * Handles User Login Authentication
 * Requires ConnectionService, ConsoleService
 * Will send to dashboard
 */
public class LoginService{
    //eager singleton
    private static LoginService loginService = new LoginService();

    private LoginService() {
        super();
    }

    public static LoginService getInstance() {
        return loginService;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     *
     * @param email Should equal db email
     * @param password on the app_user table
     * @return Returns user object with updated fields
     *
     */

    public AppUser login(String email, String password){
        AppUser currentUser = null;
        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = "SELECT * FROM project0.app_users au " +
                    "WHERE email = ? AND password = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.println("Login Successful");
                currentUser = new AppUser(email, password);
                //DashboardService.getInstance().render(currentUser); //passes user to dashboard
                    //changed to return user to caller
                return currentUser;
            } else {
                System.err.println("Email and/or password not found.");
                HomeScreen.getInstance().render(); //no user object made, return home
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return currentUser;
    }
}
