package com.revature.banking.services;

import com.revature.banking.models.AppUser;
import com.revature.banking.screens.AccountScreen;
import com.revature.banking.screens.DashboardScreen;
import com.revature.banking.screens.HomeScreen;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Handles acct/balance retrieval, acct creation, deposits and withdrawals
 */

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

    /**
     * Displays acct/balance and sets user object fields
     * @param user to find acct on db
     * @return returns existence of acct (to later prompt acct creation)
     */

    public boolean getAccounts(AppUser user){
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
                System.out.print("Account: #");
                String account = rs.getString("account");
                System.out.println(account);
                user.setAccount(account); //set user acct field

                System.out.print("Balance: $");
                Double balance = rs.getDouble("balance");
                System.out.println(balance);
                user.setAccountBalance(balance); //set user balance field
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

    /**
     * Creates a new acct and deposits 100 currency as a cute bonus
     * @param user User used to match id with new account
     */

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

            //re-renders accounts screen
            AccountScreen.getInstance().render(user);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }


    /**
     * Handles deposits
     * @param user required to pull acct/balance info for validation
     */

    public void deposit(AppUser user){
        //prompt for increase amount (simulating acct money movement)
        //checks for overflows, negative values
        //sql update
        try{
            BufferedReader console = ConsoleService.getInstance().getConsole();
            System.out.println("Enter amount to deposit: ");
            double deposit = 0;
            System.out.print("> $");
            deposit = Double.parseDouble(console.readLine());
            if(deposit<0){
                deposit = 0;
            }
            deposit = UtilService.getInstance().decimalRounding(deposit);
            //sql updates
            try (Connection conn = ConnectionService.getInstance().getConnection()) {
                String sql = "update project0.account_balance " +
                        "set balance = balance + ? " +
                        "where account = ? ";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setDouble(1, deposit);
                pstmt.setString(2, user.getAccount());
                pstmt.executeUpdate();
                user.setAccountBalance(user.getAccountBalance()+deposit); //update user balance
                System.out.println("Deposited: $"+deposit);
                System.out.println("---");
                AccountScreen.getInstance().render(user);

            } catch (SQLException sqle) {
                sqle.printStackTrace();
                System.err.println("Something went wrong.");
            }
        } catch (Exception e){
            System.err.println("Invalid input");
            System.out.println("---");
            AccountScreen.getInstance().render(user);
        }
    }

    /**
     * Handles withdrawals
     * @param user Required to validate acct/balance
     */

    public void withdraw(AppUser user){
        //include atm simulation
        //checks for negative values, overdrafts
        //sql update
        try{
            double withdraw = 0;
            BufferedReader console = ConsoleService.getInstance().getConsole();
            System.out.println("Enter amount to withdraw: ");
            System.out.print("> $");
            withdraw = Double.parseDouble(console.readLine());
            if(withdraw<0){
                withdraw = 0;
            }
            withdraw = UtilService.getInstance().decimalRounding(withdraw);

            if(withdraw >= user.getAccountBalance()){
                withdraw = user.getAccountBalance();
            }

            //sql updates
            try (Connection conn = ConnectionService.getInstance().getConnection()) {
                String sql = "update project0.account_balance " +
                        "set balance = balance - ? " +
                        "where account = ? ";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setDouble(1, withdraw);
                pstmt.setString(2, user.getAccount());
                pstmt.executeUpdate();
                user.setAccountBalance(user.getAccountBalance()-withdraw); //update user balance
                System.out.println("Withdrew: $"+withdraw);
                System.out.println("---");
                AccountScreen.getInstance().render(user);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                System.err.println("Something went wrong.");
            }


        } catch (Exception e){
            System.err.println("Invalid input");
            System.out.println("---");
            AccountScreen.getInstance().render(user);
        }
    }
}
