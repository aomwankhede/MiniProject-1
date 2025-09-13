package tech.zeta.application.frontend;

import tech.zeta.application.services.AuthService;

import java.util.Scanner;

public class AuthInterface implements FrontendInterface{
    AuthService authService = AuthService.getInstance();
    Scanner sc = new Scanner(System.in);
    @Override
    public void display() {
        System.out.println("Enter username : ");
        String username = sc.nextLine();

        System.out.println("Enter password : ");
        String password = sc.nextLine();
        if(authService.login(username,password)){
            System.out.println("You are successfully logged in...");
        }
        else{
            System.out.println("Login unsuccessful...");
        }
    }
}
