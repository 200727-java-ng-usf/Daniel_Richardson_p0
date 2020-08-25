package com.revature.banking.models;

import com.revature.banking.services.ConnectionService;
import com.revature.banking.screens.HomeScreen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


//todo dao object pattern
public class AppUser {

    // fields/attributes
    private Integer id;
    private String password;
    private String email;
    private String account;
    private double accountBalance;

    //Constructors: ----------------------------
    // no args
    public AppUser() {
        super();
    }

    //base constructor, id fetched from DB
    public AppUser(String email, String password) {
        this.password = password;
        this.email = email;
        //this.setId();
    }

    // getters and setters ---------------------------

    public String getAccount(){
        return account;
    }
    public void setAccount(String account){
        this.account = account;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Integer getId() {
        return id;
    }

    //set ID is only used after registration
    //to fetch the serialized id from database
    public void setId() {

        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = "SELECT id FROM project0.app_users au " +
                    "WHERE email = ? AND password = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                this.id = rs.getInt("id");
            } else {
                System.err.println("Unable to retrieve ID");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void selfAuthenticate(){
        //to double check if user is authenticated and valid
        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = "SELECT * FROM project0.app_users au " +
                    "WHERE email = ? AND password = ? AND id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, this.email);
            pstmt.setString(2, this.password);
            pstmt.setInt(3, this.id);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()){ //if there is no exact match with data, user fails authentication
                System.err.println("Authentication Error");
                this.id = null;
                this.email = null;
                this.password = null; //clean the slate
                HomeScreen.getInstance().render(); //send back to start
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }


    }


    // overridden Object methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id) &&
                Objects.equals(password, appUser.password) &&
                Objects.equals(email, appUser.email);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, password, email);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}