package com.revature.banking.services;

import com.revature.banking.models.AppUser;

import java.io.BufferedReader;

public class DashboardService{
    //eager singleton
    private static DashboardService dashboardService = new DashboardService();

    private DashboardService() {
        super();
    }

    public static DashboardService getInstance() {
        return dashboardService;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void render(AppUser user){

        user.setId();
        System.out.println(user.toString());
        BufferedReader console = ConsoleService.getInstance().getConsole();

        System.out.println("Welcome to your dashboard.");
        System.out.println("1) Accounts");
        System.out.println("2) Logout");

        try {
            System.out.print("> ");
            String userSelection = console.readLine();

            switch (userSelection) {
                case "1":
                    AccountService.getInstance().render(user);
                    break;
                case "2":
                    HomeService.getInstance().render(user);
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
