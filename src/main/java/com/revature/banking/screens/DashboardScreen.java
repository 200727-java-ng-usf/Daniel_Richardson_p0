package com.revature.banking.screens;

import com.revature.banking.models.AppUser;
import com.revature.banking.services.AccountService;
import com.revature.banking.services.ConsoleService;
import com.revature.banking.services.RouterService;

import java.io.BufferedReader;

/**
 * Offers user options to logout or view/handle accounts
 */

public class DashboardScreen {
    //eager singleton
    private static DashboardScreen dashboardScreen = new DashboardScreen();

    private DashboardScreen() {
        super();
    }

    public static DashboardScreen getInstance() {
        return dashboardScreen;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void render(AppUser user){
        user.setId();
        //System.out.println(user.toString());
        BufferedReader console = ConsoleService.getInstance().getConsole();

        System.out.println("Welcome to your dashboard.");
        System.out.println("1) Accounts");
        System.out.println("2) Logout");

        try {
            System.out.print("> ");
            String userSelection = console.readLine();

            switch (userSelection) {
                case "1":
                    RouterService.getInstance().route("/account",user);
                    break;
                case "2":
                    RouterService.getInstance().route("/home",user);
                    break;
                default:
                    System.out.println("[LOG] - Invalid selection!");
                    render(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
