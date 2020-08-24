package com.revature.banking.services;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.revature.banking.exceptions.*;
import com.revature.banking.models.AppUser;


/**
 * Handles User Login Authentication
 * Requires ConnectionService, ConsoleService
 *
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

    public void render() {
        String email, password;
        BufferedReader console = ConsoleService.getInstance().getConsole();

        try {
            System.out.println("Please provide your login credentials");
            System.out.print("Email: ");
            email = console.readLine();
            System.out.print("Password: ");
            password = console.readLine();
            if (email == null || email.trim().equals("") || password == null || password.trim().equals("")) {
                //throw new InvalidRequestException("Invalid input.");
                System.err.println("Invalid input");
                HomeService.getInstance().render();
            }

            //Authenticate user from database
            login(email, password);

        } catch (InvalidRequestException | AuthenticationException e) {
            System.err.println("Invalid login!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] - An unexpected exception occurred: " + e.getMessage());
            System.out.println("[LOG] - Shutting down application");
            //app.setAppRunning(false);
        }



    }

    public void login(String email, String password){

        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = "SELECT * FROM project0.app_users au " +
                    "WHERE email = ? AND password = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.println("Login Successful");
                AppUser currentUser = new AppUser(email, password);
                DashboardService.getInstance().render(currentUser); //passes user to dashboard
            } else {
                System.err.println("Email and/or password not found.");
                HomeService.getInstance().render(); //no user object made, return home
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
