package com.revature.banking.services;

import com.revature.banking.models.AppUser;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

public class AccountService{
    //eager singleton
    private static AccountService accountService = new AccountService();

    private AccountService() {
        super();
    }

    public static AccountService getInstance() {
        return accountService;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void render(AppUser user){
        user.selfAuthenticate();

        BufferedReader console = ConsoleService.getInstance().getConsole();
        if(!displayAccounts(user)){ //method automatically displays account info, otherwise prompts creation
            System.out.println("Create new account?");
            System.out.println("1) Yes");
            System.out.println("2) Cancel");
            try{
                System.out.print("> ");
                String userSelection = console.readLine();
                switch (userSelection) {
                    case "1":
                        createAccount(user);
                        break;
                    default:
                        DashboardService.getInstance().render(user);
                }
            } catch (Exception e){
                System.err.println("Invalid input");
                render(user);
            }
        }

        //Deposit, withdraw options
        System.out.println("1) Deposit");
        System.out.println("2) Withdraw");
        System.out.println("3) Logout");
        try{
            System.out.print("> ");
            String userSelection = console.readLine();
            switch (userSelection) {
                case "1":
                    deposit(user);
                    break;
                case "2":
                    withdrawal(user);
                    break;
                case "3":
                    HomeService.getInstance().render(user);
                    break;
                default:
                    DashboardService.getInstance().render(user);
            }
        } catch (Exception e){
            System.err.println("Invalid input");
            render(user);
        }


    }

    public boolean displayAccounts(AppUser user){
        boolean exists = false;
        //sql statement to retrieve and display account number / balance
        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = "SELECT ab.account, ab.balance " +
                    "FROM project0.account_balance ab " +
                    "LEFT JOIN project0.user_accounts ac " +
                    "ON ac.account = ab.account " +
                    "WHERE id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user.getId());

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                System.out.print("Account: ");
                System.out.println(rs.getString("account"));
                System.out.print("Balance: ");
                System.out.println(rs.getDouble("balance"));
                exists = true;
            } else {
                System.out.println("No account found.");
                exists = false;
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return exists;
    }

    public void createAccount(AppUser user){
        //sql statement to make account, using user id
        //gives base 100 human currency on creation
        //returns to account render
        Random rand = new Random();
        int num = 0;
        String numStr = null;

        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            boolean accExists = true;
            while(accExists){
                num = rand.nextInt(9999);
                numStr = Integer.toString(num);
                String sql = "SELECT * from project0.user_accounts "+
                        "WHERE account = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, numStr);
                ResultSet rs = pstmt.executeQuery();
                //if the query returns anything, the account number exists
                accExists = rs.next();
            }

            //create account in user_accounts table
            String sql = "INSERT INTO project0.user_accounts (id, account) " +
                    "VALUES (? , ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user.getId());
            pstmt.setString(2, numStr);
            pstmt.executeUpdate();

            //add starter funds to accounts_balance table
            sql = "INSERT INTO project0.account_balance (account, balance) " +
                    "VALUES (? , ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numStr);
            pstmt.setDouble(2, 100.00);
            pstmt.executeUpdate();
            AccountService.getInstance().render(user);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }



    public void deposit(AppUser user){
        //prompt for increase amount (simulating acct money movement)
        //checks for overflows, negative values
        //sql update

        BufferedReader console = ConsoleService.getInstance().getConsole();
        System.out.println("Enter amount to deposit: ");
        try{
            boolean valid = false;
            String amount = null;
            int deposit = 0;

            //uses pattern regex to detect non-numeric characters
            while(!valid){
                System.out.print("> ");
                amount = console.readLine();
                if(Pattern.matches("[a-zA-Z]+", amount)){
                    //todo validate commas/decimal places for foreign users
                    valid = false;
                    System.err.println("Invalid number. Try again.");
                } else {
                    valid = true;
                }
            }
            deposit = Integer.parseInt(amount);

            //todo include sql statement to update row


        } catch (Exception e){
            System.err.println("Invalid input");
            render(user);
        }


    }
    public void withdrawal(AppUser user){
        //include atm simulation
        //checks for negative values, overdrafts
        //sql update

    }
//    public void viewHistory(AppUser user){
//          //this would use a separate table with each row having account, transaction, date/timestamp
//    }
}
