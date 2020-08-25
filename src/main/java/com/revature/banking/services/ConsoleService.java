package com.revature.banking.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ConsoleService{
    //eager singleton
    private static ConsoleService consoleService = new ConsoleService();
    private BufferedReader console;
    private Scanner scanner;

    private ConsoleService() {
        super();
        System.out.println("[Console started]");
        console = new BufferedReader(new InputStreamReader(System.in));
        scanner = new Scanner(new InputStreamReader(System.in));
    }

    public static ConsoleService getInstance() {
        return consoleService;
    }

    public BufferedReader getConsole() {
        return console;
    }
    public Scanner getScanner() {
        return scanner;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
