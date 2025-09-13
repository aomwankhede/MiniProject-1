package tech.zeta.application.repositories;

import tech.zeta.application.enums.Action;
import tech.zeta.application.enums.Entity;
import tech.zeta.application.models.Permission;
import tech.zeta.application.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PermissionRepository {

    // Insert a new Permission
    public Permission save(Permission permission) throws SQLException {
        String sql = "INSERT INTO permission (target, action) VALUES (?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, permission.getTarget().name());
            ps.setString(2, permission.getAction().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    permission.setId(rs.getLong(1));
                }
            }
        }
        return permission;
    }

    // Find by ID
    public Optional<Permission> findById(Long id) throws SQLException {
        String sql = "SELECT id, target, action FROM permission WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    // Find all
    public List<Permission> findAll() throws SQLException {
        String sql = "SELECT id, target, action FROM permission";
        List<Permission> permissions = new ArrayList<>();

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                permissions.add(mapRow(rs));
            }
        }
        return permissions;
    }

    // Update
    public boolean update(Permission permission) throws SQLException {
        String sql = "UPDATE permission SET target = ?, action = ? WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, permission.getTarget().name());
            ps.setString(2, permission.getAction().name());
            ps.setLong(3, permission.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // Delete
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM permission WHERE id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Helper: Map row → Permission object
    private Permission mapRow(ResultSet rs) throws SQLException {
        return new Permission(
                rs.getLong("id"),
                Entity.valueOf(rs.getString("target")),
                Action.valueOf(rs.getString("action"))
        );
    }
}

