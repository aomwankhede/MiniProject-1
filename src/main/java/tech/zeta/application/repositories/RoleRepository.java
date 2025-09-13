package tech.zeta.application.repositories;

import tech.zeta.application.enums.Action;
import tech.zeta.application.enums.Entity;
import tech.zeta.application.models.Role;
import tech.zeta.application.models.Permission;
import tech.zeta.application.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleRepository {

    // Save role + permissions
    public void save(Role role) {
        String sql = "INSERT INTO role (name, description) VALUES (?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, role.getName());
            stmt.setString(2, role.getDescription());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    role.setId(rs.getLong(1));
                }
            }

            if (role.getPermissions() != null) {
                saveRolePermissions(conn, role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Find role by ID (with permissions)
    public Optional<Role> findById(Long id) {
        String sql = "SELECT id, name, description FROM role WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            getPermissionsByRoleId(conn, id)
                    );
                    return Optional.of(role);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Fetch all roles with permissions
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT id, name, description FROM role";

        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Long roleId = rs.getLong("id");
                Role role = new Role(
                        roleId,
                        rs.getString("name"),
                        rs.getString("description"),
                        getPermissionsByRoleId(conn, roleId)
                );
                roles.add(role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    // Update role + its permissions
    public void update(Long id,Role role) {
        String sql = "UPDATE role SET name = ?, description = ? WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role.getName());
            stmt.setString(2, role.getDescription());
            stmt.setLong(3, id);
            stmt.executeUpdate();

            // Refresh role_permissions
            deleteRolePermissions(conn, id);
            if (role.getPermissions() != null) {
                saveRolePermissions(conn, role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete role + cascade deletes role_permissions
    public void delete(Long id) {
        String sql = "DELETE FROM role WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Helper methods ---
    private void saveRolePermissions(Connection conn, Role role) throws SQLException {
        String sql = "INSERT INTO role_permissions (role_id, permission_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Permission p : role.getPermissions()) {
                stmt.setLong(1, role.getId());
                stmt.setLong(2, p.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void deleteRolePermissions(Connection conn, Long roleId) throws SQLException {
        String sql = "DELETE FROM role_permissions WHERE role_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, roleId);
            stmt.executeUpdate();
        }
    }

    private List<Permission> getPermissionsByRoleId(Connection conn, Long roleId) throws SQLException {
        List<Permission> permissions = new ArrayList<>();
        String sql = """
                SELECT p.id, p.target, p.action
                FROM permission p
                INNER JOIN role_permissions rp ON p.id = rp.permission_id
                WHERE rp.role_id = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Permission permission = new Permission(
                            rs.getLong("id"),
                            Entity.valueOf(rs.getString("target")),
                            Action.valueOf(rs.getString("action"))
                    );
                    permissions.add(permission);
                }
            }
        }
        return permissions;
    }
}
