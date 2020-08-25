package com.revature.banking.screens;
import com.revature.banking.exceptions.AuthenticationException;
import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.models.AppUser;
import com.revature.banking.services.ConsoleService;
import com.revature.banking.services.LoginService;
import com.revature.banking.services.RouterService;
import java.io.BufferedReader;


/**
 * LoginScreen singleton
 * Retrieves from user email and password,
 * sends to LoginService for sql authentication
 */

public class LoginScreen {

    private static LoginScreen loginScreen = new LoginScreen();
    private LoginScreen() {
        super();
    }
    public static LoginScreen getInstance() {
        return loginScreen;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    public void render() {
        AppUser user;
        String email, password;
        BufferedReader console = ConsoleService.getInstance().getConsole();

        try {
            System.out.println("Please provide your login credentials");
            System.out.print("Email: ");
            email = console.readLine();
            System.out.print("Password: ");
            password = console.readLine();
            if (email == null || email.trim().equals("") || password == null || password.trim().equals("")) {
                System.err.println("Invalid input");
                RouterService.getInstance().route("/home");
            }

            //Authenticate user from database
            user = LoginService.getInstance().login(email, password);
            //sends to router>dashboard
            RouterService.getInstance().route("/dashboard", user);

        } catch (InvalidRequestException | AuthenticationException e) {
            System.err.println("Invalid login!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] - An unexpected exception occurred: " + e.getMessage());
            System.out.println("[LOG] - Shutting down application");
        }



    }
}
