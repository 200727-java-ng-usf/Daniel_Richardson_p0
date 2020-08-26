package com.revature.banking;

import com.revature.banking.services.RouterService;

public class AppDriver {

    public static void main(String[] args) {
        RouterService.getInstance().route("/home");
    }


}

