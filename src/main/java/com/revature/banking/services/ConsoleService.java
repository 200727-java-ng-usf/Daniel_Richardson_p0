package com.revature.banking.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ConsoleService{
    //eager singleton
    private static ConsoleService consoleService = new ConsoleService();
    private BufferedReader console;

    private ConsoleService() {
        super();
        //System.out.println("[Console started]");
        console = new BufferedReader(new InputStreamReader(System.in));
    }

    public static ConsoleService getInstance() {
        return consoleService;
    }

    public BufferedReader getConsole() {
        return console;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
