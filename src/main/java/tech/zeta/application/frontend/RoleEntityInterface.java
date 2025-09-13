package tech.zeta.application.frontend;

import tech.zeta.application.enums.Action;
import tech.zeta.application.enums.Entity;
import tech.zeta.application.models.Role;
import tech.zeta.application.services.AuthService;
import tech.zeta.application.services.RoleService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RoleEntityInterface implements FrontendInterface {
    private final Scanner sc = new Scanner(System.in);
    private final RoleService roleService = RoleService.getInstance();
    private final AuthService authService = AuthService.getInstance();

    @Override
    public void display() {
        while (true) {
            System.out.println("\n--- Role Management ---");
            System.out.println("1. Create Role");
            System.out.println("2. View Role by ID");
            System.out.println("3. List All Roles");
            System.out.println("4. Update Role");
            System.out.println("5. Delete Role");
            System.out.println("6. Assign Permissions to Role");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 0 -> { return; }
                case 1 -> createRole();
                case 2 -> viewRole();
                case 3 -> listRoles();
                case 4 -> updateRole();
                case 5 -> deleteRole();
                case 6 -> assignPermissions();
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    private void createRole() {
        if (!authService.hasPermission(Action.C, Entity.ROLE)) {
            System.out.println("❌ Not authorized to create roles!");
            return;
        }
        try {
            System.out.print("Enter role name: ");
            String name = sc.nextLine();
            System.out.print("Enter role description: ");
            String desc = sc.nextLine();

            Role role = new Role(null, name, desc, new ArrayList<>());
            roleService.createRole(role);
            System.out.println("✅ Role created successfully!");
        } catch (SQLException e) {
            System.out.println("⚠️ Error creating role: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private void viewRole() {
        if (!authService.hasPermission(Action.R, Entity.ROLE)) {
            System.out.println("❌ Not authorized to view roles!");
            return;
        }
        System.out.print("Enter role ID: ");
        Long id = sc.nextLong(); sc.nextLine();
        Optional<Role> role = roleService.getRoleById(id);
        role.ifPresentOrElse(System.out::println, () -> System.out.println("⚠️ Role not found."));
    }

    private void listRoles() {
        if (!authService.hasPermission(Action.R, Entity.ROLE)) {
            System.out.println("❌ Not authorized to list roles!");
            return;
        }
        List<Role> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            System.out.println("⚠️ No roles found.");
        } else {
            roles.forEach(System.out::println);
        }
    }

    private void updateRole() {
        if (!authService.hasPermission(Action.U, Entity.ROLE)) {
            System.out.println("❌ Not authorized to update roles!");
            return;
        }
        try {
            System.out.print("Enter role ID: ");
            Long id = sc.nextLong(); sc.nextLine();

            System.out.print("Enter new role name: ");
            String name = sc.nextLine();
            System.out.print("Enter new role description: ");
            String desc = sc.nextLine();

            Role updated = new Role(id, name, desc, new ArrayList<>());
            roleService.updateRole(id, updated);
            System.out.println("✅ Role updated successfully!");
        } catch (SQLException e) {
            System.out.println("⚠️ Error updating role: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private void deleteRole() {
        if (!authService.hasPermission(Action.D, Entity.ROLE)) {
            System.out.println("❌ Not authorized to delete roles!");
            return;
        }
        try {
            System.out.print("Enter role ID: ");
            Long id = sc.nextLong(); sc.nextLine();
            roleService.deleteRole(id);
            System.out.println("✅ Role deleted successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private void assignPermissions() {
        if (!authService.hasPermission(Action.U, Entity.ROLE)) {
            System.out.println("❌ Not authorized to assign permissions!");
            return;
        }
        try {
            System.out.print("Enter role ID: ");
            Long roleId = sc.nextLong(); sc.nextLine();

            System.out.print("Enter permission IDs (comma separated): ");
            String input = sc.nextLine();
            String[] parts = input.split(",");
            List<Long> permIds = new ArrayList<>();
            for (String p : parts) {
                try {
                    permIds.add(Long.parseLong(p.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid ID skipped: " + p);
                }
            }

            roleService.assignPermissions(roleId, permIds);
            System.out.println("✅ Permissions assigned successfully!");
        } catch (SQLException e) {
            System.out.println("⚠️ Error assigning permissions: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }
}
