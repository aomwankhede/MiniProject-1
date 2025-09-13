package tech.zeta.application.repositories;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.ClientPayment;
import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.utils.DBUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ClientPaymentRepository {

    // Save payment
    public void save(ClientPayment payment) {
        String sql = "INSERT INTO client_payment (amount, direction, status, created_at, updated_at, client_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getDirection().name());
            stmt.setString(3, payment.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(payment.getCreatedAt() != null ? payment.getCreatedAt() : LocalDateTime.now()));
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getUpdatedAt() != null ? payment.getUpdatedAt() : LocalDateTime.now()));
            stmt.setLong(6, payment.getClientId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    payment.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            log.error("Exception in ClientPayment save() {}",e.getMessage());
        }
    }

    // Find by ID
    public Optional<ClientPayment> findById(Long id) {
        String sql = "SELECT * FROM client_payment WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToClientPayment(rs));
                }
            }

        } catch (SQLException e) {
            log.error("Exception in ClientPayment findById() {}",e.getMessage());
        }
        return Optional.empty();
    }

    public List<ClientPayment> findAll() {
        List<ClientPayment> payments = new ArrayList<>();
        String sql = "SELECT * FROM client_payment";

        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payments.add(mapRowToClientPayment(rs));
            }

        } catch (SQLException e) {
            log.error("Exception in ClientPayment findAll() {}",e.getMessage());
        }
        return payments;
    }

    public void update(Long id,ClientPayment payment) {
        String sql = "UPDATE client_payment SET amount = ?, direction = ?, status = ?, updated_at = ?, client_id = ? WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getDirection().name());
            stmt.setString(3, payment.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // auto update timestamp
            stmt.setLong(5, payment.getClientId());
            stmt.setLong(6, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception in ClientPayment update() {}",e.getMessage());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM client_payment WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception in ClientPayment delete() {}",e.getMessage());
        }
    }

    private ClientPayment mapRowToClientPayment(ResultSet rs) throws SQLException {
        ClientPayment payment = new ClientPayment();
        payment.setId(rs.getLong("id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setDirection(PaymentDirection.valueOf(rs.getString("direction")));
        payment.setStatus(PaymentStatus.valueOf(rs.getString("status")));
        payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        payment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        payment.setClientId(rs.getLong("client_id"));
        return payment;
    }
}
