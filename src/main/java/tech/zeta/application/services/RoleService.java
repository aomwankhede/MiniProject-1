package tech.zeta.application.services;

import tech.zeta.application.models.Permission;
import tech.zeta.application.models.Role;
import tech.zeta.application.repositories.PermissionRepository;
import tech.zeta.application.repositories.RoleRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private static RoleService instance = null;
    private RoleService() {
        this.roleRepository = new RoleRepository();
        this.permissionRepository = new PermissionRepository();
    }
    public static RoleService getInstance(){
        if(instance == null){
            instance =  new RoleService();
        }
        return instance;
    }
    // Create a new Role with optional permissions
    public Role createRole(Role role) throws SQLException {
        if (role.getName() == null || role.getName().isBlank()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }

        // Check if role with same name already exists
        Optional<Role> existing = findByName(role.getName());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Role already exists: " + role.getName());
        }

        // Validate permission IDs
        if (role.getPermissions() != null) {
            for (Permission p : role.getPermissions()) {
                if (permissionRepository.findById(p.getId()).isEmpty()) {
                    throw new IllegalArgumentException("Permission not found with ID: " + p.getId());
                }
            }
        }

        roleRepository.save(role);
        return role;
    }

    // Fetch role by ID
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    // Fetch all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Update existing role
    public void updateRole(Long id,Role role) throws SQLException {
        Optional<Role> existing = roleRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Role not found with ID: " + id);
        }

        // Validate permissions if present
        if (role.getPermissions() != null) {
            for (Permission p : role.getPermissions()) {
                if (permissionRepository.findById(p.getId()).isEmpty()) {
                    throw new IllegalArgumentException("Permission not found with ID: " + p.getId());
                }
            }
        }

        roleRepository.update(id,role);
    }

    // Delete role
    public void deleteRole(Long id) {
        Optional<Role> existing = roleRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Role not found with ID: " + id);
        }
        roleRepository.delete(id);
    }

    // Assign new permissions to role
    public void assignPermissions(Long roleId, List<Long> permissionIds) throws SQLException {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isEmpty()) {
            throw new IllegalArgumentException("Role not found with ID: " + roleId);
        }

        Role role = roleOpt.get();
        List<Permission> permissions = permissionRepository.findAll();

        // Filter only required permissions
        List<Permission> matched = permissions.stream()
                .filter(p -> permissionIds.contains(p.getId()))
                .toList();

        if (matched.size() != permissionIds.size()) {
            throw new IllegalArgumentException("Some permissions were not found");
        }

        role.setPermissions(matched);
        roleRepository.update(roleId,role);
    }

    // Helper: Find role by name (simple filter)
    public Optional<Role> findByName(String name) {
        return roleRepository.findAll()
                .stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
