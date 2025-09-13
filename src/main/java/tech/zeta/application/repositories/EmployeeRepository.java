package tech.zeta.application.repositories;

import tech.zeta.application.models.Employee;
import tech.zeta.application.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository {

    // Save employee
    public void save(Employee employee) {
        String sql = "INSERT INTO employee (name, bank_account, contact_email, department, pan_number) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getBankAccount());
            stmt.setString(3, employee.getContactEmail());
            stmt.setString(4, employee.getDepartment());
            stmt.setString(5, employee.getPanNumber());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    employee.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Find employee by ID
    public Optional<Employee> findById(Long id) {
        String sql = "SELECT id, name, bank_account, contact_email, department, pan_number FROM employee WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToEmployee(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Fetch all employees
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT id, name, bank_account, contact_email, department, pan_number FROM employee";

        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(mapRowToEmployee(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // Update employee
    public void update(Long id,Employee employee) {
        String sql = "UPDATE employee SET name = ?, bank_account = ?, contact_email = ?, department = ?, pan_number = ? WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getBankAccount());
            stmt.setString(3, employee.getContactEmail());
            stmt.setString(4, employee.getDepartment());
            stmt.setString(5, employee.getPanNumber());
            stmt.setLong(6, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete employee
    public void delete(Long id) {
        String sql = "DELETE FROM employee WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Helper ---
    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setBankAccount(rs.getString("bank_account"));
        employee.setContactEmail(rs.getString("contact_email"));
        employee.setDepartment(rs.getString("department"));
        employee.setPanNumber(rs.getString("pan_number"));
        return employee;
    }
}
