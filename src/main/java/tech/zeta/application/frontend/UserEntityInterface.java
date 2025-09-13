package tech.zeta.application.frontend;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.enums.Action;
import tech.zeta.application.enums.Entity;
import tech.zeta.application.models.User;
import tech.zeta.application.services.AuthService;
import tech.zeta.application.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


@Slf4j
public class UserEntityInterface implements FrontendInterface {

    private final Scanner sc;
    private final UserService userService;
    private final AuthService authService;

    public UserEntityInterface() {
        this.sc = new Scanner(System.in);
        this.userService = UserService.getInstance();
        this.authService = AuthService.getInstance();
    }

    @Override
    public void display() {
        if (!authService.isAuthenticated()) {
            log.info("Please login first to manage users.");
            return;
        }

        while (true) {
            System.out.println("\n==== User Management ====");
            System.out.println("1. Create new user");
            System.out.println("2. View all users");
            System.out.println("3. View user by ID");
            System.out.println("4. Update user");
            System.out.println("5. Delete user");
            System.out.println("6. Assign role to user");
            System.out.println("0. Back to main menu");
            System.out.print("👉 Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createUser();
                case 2 -> listUsers();
                case 3 -> viewUser();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 6 -> assignRole();
                case 0 -> {
                    return; // exit loop
                }
                default -> System.out.println("⚠️ Invalid choice, try again.");
            }
        }
    }

    private void createUser() {
        if (!authService.hasPermission(Action.C, Entity.USER)) {
            log.info("You don't have permission to create users.");
            return;
        }

        try {
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            System.out.print("Enter email: ");
            String email = sc.nextLine();

            System.out.print("Enter roleId: ");
            Long roleId = sc.nextLong();sc.nextLine();

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setRoleId(roleId);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            User created = userService.createUser(user);
            log.info("User created: {}" , created.getId());
        } catch (Exception e) {
            log.error("Error: {}" , e.getMessage());
        }
    }

    private void listUsers() {
        if (!authService.hasPermission(Action.R, Entity.USER)) {
            log.info("You don't have permission to view users.");
            return;
        }

        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("\n--- Users ---");
            users.forEach(System.out::println);
        }
    }

    private void viewUser() {
        if (!authService.hasPermission(Action.R, Entity.USER)) {
            log.info("You don't have permission to view users.");
            return;
        }

        System.out.print("Enter user ID: ");
        Long id = sc.nextLong();
        sc.nextLine();

        Optional<User> user = userService.getUserById(id);
        user.ifPresentOrElse(
                System.out::println,
                () -> log.info("⚠️ User not found with ID " + id)
        );
    }

    private void updateUser() {
        if (!authService.hasPermission(Action.U, Entity.USER)) {
            log.info("You don't have permission to update users.");
            return;
        }

        System.out.print("Enter user ID to update: ");
        Long id = sc.nextLong();
        sc.nextLine();

        try {
            Optional<User> existing = userService.getUserById(id);
            if (existing.isEmpty()) {
                log.info("User not found.");
                return;
            }

            User user = existing.get();

            System.out.print("New email (blank to skip): ");
            String email = sc.nextLine();
            if (!email.isBlank()) user.setEmail(email);

            System.out.print("New password (blank to skip): ");
            String password = sc.nextLine();
            if (!password.isBlank()) user.setPassword(password);

            user.setUpdatedAt(LocalDateTime.now());
            userService.updateUser(id, user);
            log.info("User updated.");
        } catch (Exception e) {
            log.error("Error: {}" , e.getMessage());
        }
    }

    private void deleteUser() {
        if (!authService.hasPermission(Action.D, Entity.USER)) {
            log.info("You don't have permission to delete users.");
            return;
        }

        System.out.print("Enter user ID to delete: ");
        Long id = sc.nextLong();
        sc.nextLine();

        try {
            userService.deleteUser(id);
            log.info("User {} deleted.",id);
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
        }
    }

    private void assignRole() {
        if (!authService.hasPermission(Action.U, Entity.USER)) {
            log.info("You don't have permission to assign roles.");
            return;
        }

        System.out.print("Enter user ID: ");
        Long userId = sc.nextLong();
        sc.nextLine();

        System.out.print("Enter role ID: ");
        Long roleId = sc.nextLong();
        sc.nextLine();

        try {
            userService.assignRole(userId, roleId);
            log.info("Role {} assigned to {}",roleId,userId);
        } catch (Exception e) {
            log.error("Error: {} ",e.getMessage());
        }
    }
}
