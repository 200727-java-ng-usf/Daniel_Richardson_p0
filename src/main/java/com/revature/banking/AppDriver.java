package com.revature.banking;

import com.revature.banking.services.HomeService;
public class AppDriver {

    public static void main(String[] args) {

        HomeService.getInstance().render();

    }
}
