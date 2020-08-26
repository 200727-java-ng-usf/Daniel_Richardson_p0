package com.revature.banking.screens;

import com.revature.banking.models.AppUser;
import com.revature.banking.services.ConsoleService;
import com.revature.banking.services.RouterService;

import java.io.BufferedReader;

/**
 * HomeScreen, singleton
 * Sends user to login or register
 * Overloaded render method to logout (de-reference user object)
 */

public class HomeScreen {
    private static HomeScreen homeScreen = new HomeScreen();
    private HomeScreen() {
        super();
        //System.out.println("[HomeScreen Instantiated]");
    }
    public static HomeScreen getInstance() {
        return homeScreen;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Directs to login or registration screens.
     * @param user User passed in will be de-referenced for logout
     */
    public void render(AppUser user){
        user = null;    //wipe clean the slate
        render();
    }

    public void render() {

        BufferedReader console = ConsoleService.getInstance().getConsole();

        System.out.println("Welcome to Human Banking.");
        System.out.println("1) Login");
        System.out.println("2) Register");

        try {
            System.out.print("> ");
            String userSelection = console.readLine();

            switch (userSelection) {
                case "1":
                        RouterService.getInstance().route("/login");
                    break;
                case "2":
                        RouterService.getInstance().route("/register");
                    break;
                case "3":
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.err.println("Invalid selection");
                    render();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
