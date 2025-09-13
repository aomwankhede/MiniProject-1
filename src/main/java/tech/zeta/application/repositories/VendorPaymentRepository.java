package tech.zeta.application.repositories;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.VendorPayment;
import tech.zeta.application.utils.DBUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class VendorPaymentRepository {

    // Save new vendor payment
    public void save(VendorPayment payment) {
        String sql = "INSERT INTO vendor_payment (amount, direction, status, created_at, updated_at, vendor_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getDirection().name());
            stmt.setString(3, payment.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(payment.getCreatedAt() != null ? payment.getCreatedAt() : LocalDateTime.now()));
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getUpdatedAt() != null ? payment.getUpdatedAt() : LocalDateTime.now()));
            stmt.setLong(6, payment.getVendorId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    payment.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            log.error("Exception in VendorPaymentRepository save() {}",e.getMessage());
        }
    }

    // Find by ID
    public Optional<VendorPayment> findById(Long id) {
        String sql = "SELECT * FROM vendor_payment WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToVendorPayment(rs));
                }
            }

        } catch (SQLException e) {
            log.error("Exception in VendorPaymentRepository findById() {}",e.getMessage());
        }
        return Optional.empty();
    }

    // Find all
    public List<VendorPayment> findAll() {
        List<VendorPayment> payments = new ArrayList<>();
        String sql = "SELECT * FROM vendor_payment";

        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payments.add(mapRowToVendorPayment(rs));
            }

        } catch (SQLException e) {
            log.error("Exception in VendorPaymentRepository findAll() {}",e.getMessage());
        }
        return payments;
    }

    // Update vendor payment
    public void update(Long id,VendorPayment payment) {
        String sql = "UPDATE vendor_payment " +
                "SET amount = ?, direction = ?, status = ?, updated_at = ?, vendor_id = ? " +
                "WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getDirection().name());
            stmt.setString(3, payment.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // auto update
            stmt.setLong(5, payment.getVendorId());
            stmt.setLong(6, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception in VendorPaymentRepository update() {}",e.getMessage());
        }
    }

    // Delete by ID
    public void delete(Long id) {
        String sql = "DELETE FROM vendor_payment WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception in VendorPaymentRepository delete() {}",e.getMessage());
        }
    }

    // --- Helper ---
    private VendorPayment mapRowToVendorPayment(ResultSet rs) throws SQLException {
        VendorPayment payment = new VendorPayment();
        payment.setId(rs.getLong("id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setDirection(PaymentDirection.valueOf(rs.getString("direction")));
        payment.setStatus(PaymentStatus.valueOf(rs.getString("status")));
        payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        payment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        payment.setVendorId(rs.getLong("vendor_id"));
        return payment;
    }
}
