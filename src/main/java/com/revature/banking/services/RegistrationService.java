package com.revature.banking.services;

import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.models.AppUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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


    public void render() {

        String email = null;
        String password = null;

        try {
            boolean valid = false;
            while(!valid){
                System.out.println("Create new human banking account:");
                System.out.print("Email: ");
                email = ConsoleService.getInstance().getConsole().readLine();
                System.out.print("Password: ");
                password = ConsoleService.getInstance().getConsole().readLine();


                if (email == null || email.trim().equals("") || password == null || password.trim().equals("")) {
                    System.err.println("Invalid input");
                    valid = false;
                } else {
                    valid = true;
                }
            }

            //Check to see if email already taken
            if(checkDuplicateEmail(email)){
                System.err.println("Email taken.");
                HomeService.getInstance().render(); //send to home screen
            } else {
                register(email, password);  //send to sql registration
                AppUser newUser = new AppUser(email, password); //auto-login user
                DashboardService.getInstance().render(newUser); //send to dashboard
            }
        } catch (InvalidRequestException e) {
            System.err.println("Registration unsuccessful, invalid values provided.");
        } catch (Exception e) {
            System.err.println("[ERROR] - An unexpected exception occurred: " + e.getMessage());
            System.out.println("[LOG] - Shutting down application");

        }

    }

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

    private void register(String email, String password){
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
    }

}
