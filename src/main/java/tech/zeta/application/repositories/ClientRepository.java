package tech.zeta.application.repositories;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.models.Client;
import tech.zeta.application.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ClientRepository {

    // Insert new client
    public void save(Client client) {
        String sql = "INSERT INTO client (name, bank_account, contact_email, company, contract_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getBankAccount());
            stmt.setString(3, client.getContactEmail());
            stmt.setString(4, client.getCompany());
            stmt.setString(5, client.getContractId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            log.error("Exception in ClientRepository save() {}",e.getMessage());
        }
    }

    // Find client by ID
    public Optional<Client> findById(Long id) {
        String sql = "SELECT id, name, bank_account, contact_email, company, contract_id FROM client WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToClient(rs));
                }
            }

        } catch (SQLException e) {
            log.error("Exception in ClientRepository findById() {}",e.getMessage());
        }
        return Optional.empty();
    }

    // Fetch all clients
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT id, name, bank_account, contact_email, company, contract_id FROM client";

        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapRowToClient(rs));
            }

        } catch (SQLException e) {
            log.error("Exception in ClientRepository findAll() {}",e.getMessage());
        }
        return clients;
    }

    // Update client
    public void update(Long id,Client client) {
        String sql = "UPDATE client SET name = ?, bank_account = ?, contact_email = ?, company = ?, contract_id = ? WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getBankAccount());
            stmt.setString(3, client.getContactEmail());
            stmt.setString(4, client.getCompany());
            stmt.setString(5, client.getContractId());
            stmt.setLong(6, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception in ClientRepository update() {}",e.getMessage());
        }
    }

    // Delete client
    public void delete(Long id) {
        String sql = "DELETE FROM client WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception in ClientRepository delete() {}",e.getMessage());
        }
    }

    // --- Helper to map resultset to Client ---
    private Client mapRowToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setName(rs.getString("name"));
        client.setBankAccount(rs.getString("bank_account"));
        client.setContactEmail(rs.getString("contact_email"));
        client.setCompany(rs.getString("company"));
        client.setContractId(rs.getString("contract_id"));
        return client;
    }
}
