package tech.zeta.application.repositories;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.SalaryPayment;
import tech.zeta.application.utils.DBUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SalaryPaymentRepository {

    // Save new salary payment
    public void save(SalaryPayment payment) {
        String sql = "INSERT INTO salary_payment (amount, direction, status, created_at, updated_at, employee_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getDirection().name());
            stmt.setString(3, payment.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(payment.getCreatedAt() != null ? payment.getCreatedAt() : LocalDateTime.now()));
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getUpdatedAt() != null ? payment.getUpdatedAt() : LocalDateTime.now()));
            stmt.setLong(6, payment.getEmployeeId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    payment.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            log.error("Exception in SalaryPaymentRepository save() {}",e.getMessage());
        }
    }

    // Find by ID
    public Optional<SalaryPayment> findById(Long id) {
        String sql = "SELECT * FROM salary_payment WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToSalaryPayment(rs));
                }
            }

        } catch (SQLException e) {
            log.error("Exception in SalaryPaymentRepository findById() {}",e.getMessage());
        }
        return Optional.empty();
    }

    // Find all
    public List<SalaryPayment> findAll() {
        List<SalaryPayment> payments = new ArrayList<>();
        String sql = "SELECT * FROM salary_payment";

        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payments.add(mapRowToSalaryPayment(rs));
            }

        } catch (SQLException e) {
            log.error("Exception in SalaryPaymentRepository findAll() {}",e.getMessage());
        }
        return payments;
    }

    // Update salary payment
    public void update(Long id,SalaryPayment payment) {
        String sql = "UPDATE salary_payment " +
                "SET amount = ?, direction = ?, status = ?, updated_at = ?, employee_id = ? " +
                "WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getDirection().name());
            stmt.setString(3, payment.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // auto update timestamp
            stmt.setLong(5, payment.getEmployeeId());
            stmt.setLong(6, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception in SalaryPaymentRepository update() {}",e.getMessage());
        }
    }

    // Delete by ID
    public void delete(Long id) {
        String sql = "DELETE FROM salary_payment WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception in SalaryPaymentRepository delete() {}",e.getMessage());
        }
    }

    // --- Helper to map DB row -> SalaryPayment ---
    private SalaryPayment mapRowToSalaryPayment(ResultSet rs) throws SQLException {
        SalaryPayment payment = new SalaryPayment();
        payment.setId(rs.getLong("id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setDirection(PaymentDirection.valueOf(rs.getString("direction")));
        payment.setStatus(PaymentStatus.valueOf(rs.getString("status")));
        payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        payment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        payment.setEmployeeId(rs.getLong("employee_id"));
        return payment;
    }
}
