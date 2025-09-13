package tech.zeta.application.frontend;


import java.sql.SQLException;

public interface FrontendInterface {
    default void display() {
        System.out.println("This display has not yet implemented...");
    }
}
