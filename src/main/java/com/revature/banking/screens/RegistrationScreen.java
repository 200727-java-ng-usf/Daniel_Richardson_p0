package com.revature.banking.screens;

import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.models.AppUser;
import com.revature.banking.services.ConsoleService;
import com.revature.banking.services.RegistrationService;
import com.revature.banking.services.RouterService;

/**
 * RegistrationScreen
 * Gathers email, password from user
 * and sends to RegistrationService.
 *
 * Receives newly created user object
 * and sends to dashboard
 */

public class RegistrationScreen {
    private static RegistrationScreen registrationScreen = new RegistrationScreen();
    private RegistrationScreen() {
        super();
    }
    public static RegistrationScreen getInstance() {
        return registrationScreen;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


    public void render() {
        AppUser newUser = null;
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
            newUser = RegistrationService.getInstance().register(email, password); //setUser
            RouterService.getInstance().route("/dashboard",newUser); //send to dashboard


        } catch (InvalidRequestException e) {
            System.err.println("Registration unsuccessful, invalid values provided.");
        } catch (Exception e) {
            System.err.println("[ERROR] - An unexpected exception occurred: " + e.getMessage());
            System.out.println("[LOG] - Shutting down application");

        }

    }

}
