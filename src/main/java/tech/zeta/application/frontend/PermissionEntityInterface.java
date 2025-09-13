package tech.zeta.application.frontend;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.models.Permission;
import tech.zeta.application.enums.Action;
import tech.zeta.application.enums.Entity;
import tech.zeta.application.repositories.PermissionRepository;
import tech.zeta.application.services.AuthService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
public class PermissionEntityInterface implements FrontendInterface {
    private final Scanner sc = new Scanner(System.in);
    private final PermissionRepository permissionRepository = new PermissionRepository();
    private final AuthService authService = AuthService.getInstance();

    @Override
    public void display()  {
        while (true) {
            System.out.println("\n--- Permission Management ---");
            System.out.println("1. Create Permission");
            System.out.println("2. View Permission by ID");
            System.out.println("3. List All Permissions");
            System.out.println("4. Delete Permission");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt(); sc.nextLine();

            try {
                switch (choice) {
                    case 0:
                        return;
                    case 1:
                        createPermission();
                        break;
                    case 2:
                        viewPermission();
                        break;
                    case 3:
                        listPermissions();
                        break;
                    case 4:
                        deletePermission();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            }catch (SQLException exception){
                System.err.println(exception.getMessage());
            }
        }
    }

    private void createPermission() throws SQLException {
        if (!authService.hasPermission(Action.C, Entity.PERMISSION)) {
            log.info("Not authorized to create permissions!");
            return;
        }
        System.out.print("Enter Action (C/R/U/D): ");
        String act = sc.nextLine();
        System.out.print("Enter Target Entity (USER/ROLE/PERMISSION/etc): ");
        String target = sc.nextLine();

        Permission perm = new Permission(null, Entity.valueOf(target),Action.valueOf(act));
        permissionRepository.save(perm);
        log.info("Permission created!");
    }

    private void viewPermission() throws SQLException {
        if (!authService.hasPermission(Action.R, Entity.PERMISSION)) {
            log.info("Not authorized to read permissions!");
            return;
        }
        System.out.print("Enter permission ID: ");
        Long id = sc.nextLong(); sc.nextLine();
        Optional<Permission> p = permissionRepository.findById(id);
        p.ifPresentOrElse(System.out::println, () -> log.info("Permission not found."));
    }

    private void listPermissions() throws SQLException {
        if (!authService.hasPermission(Action.R, Entity.PERMISSION)) {
            log.info("Not authorized to list permissions!");
            return;
        }
        List<Permission> perms = permissionRepository.findAll();
        perms.forEach(System.out::println);
    }

    private void deletePermission() throws SQLException {
        if (!authService.hasPermission(Action.D, Entity.PERMISSION)) {
            log.info("Not authorized to delete permissions!");
            return;
        }
        System.out.print("Enter permission ID: ");
        Long id = sc.nextLong(); sc.nextLine();
        permissionRepository.delete(id);
        log.info("Permission deleted!");
    }
}
