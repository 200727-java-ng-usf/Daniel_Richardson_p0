package com.revature.banking.screens;


import com.revature.banking.models.AppUser;
import com.revature.banking.services.AccountService;
import com.revature.banking.services.ConsoleService;
import com.revature.banking.services.RouterService;

import java.io.BufferedReader;

/**
 * Auto-displays account and balance,
 * and offers deposits/withdrawals
 * if none exists, prompts creation
 */

public class AccountScreen {
    //eager singleton
    private static AccountScreen accountScreen = new AccountScreen();

    private AccountScreen() {
        super();
    }

    public static AccountScreen getInstance() {
        return accountScreen;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


    public void render(AppUser user){
        user.selfAuthenticate(); //double-check authentication
        BufferedReader console = ConsoleService.getInstance().getConsole();

        //method automatically displays account info, otherwise prompts creation if no acct
        //also sets the user object's acct and balance fields
        if(!AccountService.getInstance().getAccounts(user)){
            System.out.println("Create new account?");
            System.out.println("1) Yes");
            System.out.println("2) Cancel");
            try{
                System.out.print("> ");
                String userSelection = console.readLine();
                switch (userSelection) {
                    case "1":
                        //call acct creation method
                        AccountService.getInstance().createAccount(user);
                        break;
                    case "2":
                        //back to dash
                        RouterService.getInstance().route("/dashboard", user);
                        break;
                    default:
                        //send back to dashboard
                        System.err.println("Invalid.");
                        RouterService.getInstance().route("/dashboard", user);
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
                    AccountService.getInstance().deposit(user);
                    break;
                case "2":
                    AccountService.getInstance().withdraw(user);
                    break;
                case "3": //home logout method
                    RouterService.getInstance().route("/home",user);
                    break;
                default: //or back a page
                    RouterService.getInstance().route("/dashboard",user);
            }
        } catch (Exception e){
            System.err.println("Invalid input");
            render(user);
        }


    }

}
