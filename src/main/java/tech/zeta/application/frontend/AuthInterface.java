package tech.zeta.application.frontend;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.services.AuthService;

import java.util.Scanner;

@Slf4j
public class AuthInterface implements FrontendInterface{
    AuthService authService = AuthService.getInstance();
    Scanner sc = new Scanner(System.in);
    @Override
    public void display() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();
        if(authService.login(username,password)){
            log.info("User {} logged in successfully",username);
        }
        else{
            log.info("User {} login unsuccessful",username);
        }
    }
}
