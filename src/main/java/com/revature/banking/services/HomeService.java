package com.revature.banking.services;

import com.revature.banking.models.AppUser;

import java.io.BufferedReader;

public class HomeService {
    //eager singleton
    private static HomeService homeService = new HomeService();

    private HomeService() {
        super();
        System.out.println("[HomeScreen Instantiated]");
    }

    public static HomeService getInstance() {
        return homeService;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    public void render(AppUser user){
        user = null;    //wipe clean the slate
        render();       //continue to normal method
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
                        LoginService.getInstance().render();
                    break;
                case "2":
                        RegistrationService.getInstance().render();
                    break;
                default:
                    System.out.println("[LOG] - Invalid selection!");
                    render();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
