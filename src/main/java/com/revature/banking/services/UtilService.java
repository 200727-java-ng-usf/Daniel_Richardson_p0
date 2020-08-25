package com.revature.banking.services;

public class UtilService {

    private static UtilService utilService = new UtilService();

    private UtilService() {
        super();
    }

    public static UtilService getInstance() {
        return utilService;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public double decimalRounding(Double num){
        return Math.floor(num * 100) / 100;
    }

}
