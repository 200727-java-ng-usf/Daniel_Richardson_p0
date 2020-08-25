package com.revature.banking.services;

import com.revature.banking.models.AppUser;
import com.revature.banking.screens.*;

public class RouterService {
    private static RouterService routerService = new RouterService();
    private RouterService() {
        super();
    }
    public static RouterService getInstance() {
        return routerService;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void route(String screen, AppUser user){

        switch(screen){
            case "/home": HomeScreen.getInstance().render(user);
            case "/dashboard": DashboardScreen.getInstance().render(user);
            case "/account": AccountScreen.getInstance().render(user);
        }
    }
    public void route(String screen){
        switch(screen){
            case "/home": HomeScreen.getInstance().render();
            case "/login": LoginScreen.getInstance().render();
            case "/register": RegistrationScreen.getInstance().render();
        }
    }


}
