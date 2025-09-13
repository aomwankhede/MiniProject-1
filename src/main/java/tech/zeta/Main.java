package tech.zeta;

import java.sql.SQLException;

public class Main {
    //testing
    public static void main(String[] args) throws SQLException {
        System.out.println("Welcome to Zeta's PMS");
        PMS application = new PMS();
        application.start();
    }
}