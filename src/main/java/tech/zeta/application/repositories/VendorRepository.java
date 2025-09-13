package tech.zeta.application.repositories;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.models.Vendor;
import tech.zeta.application.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class VendorRepository {

    // Insert new vendor
    public void save(Vendor vendor) {
        String sql = "INSERT INTO vendor (name, bank_account, contact_email, gst_number, invoice_terms) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vendor.getName());
            stmt.setString(2, vendor.getBankAccount());
            stmt.setString(3, vendor.getContactEmail());
            stmt.setString(4, vendor.getGstNumber());
            stmt.setString(5, vendor.getInvoiceTerms());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vendor.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            log.error("Exception in VendorRepository save() {}",e.getMessage());
        }
    }

    // Find vendor by ID
    public Optional<Vendor> findById(Long id) {
        String sql = "SELECT id, name, bank_account, contact_email, gst_number, invoice_terms FROM vendor WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToVendor(rs));
                }
            }

        } catch (SQLException e) {
            log.error("Exception in VendorRepository findById() {}",e.getMessage());
        }
        return Optional.empty();
    }

    // Fetch all vendors
    public List<Vendor> findAll() {
        List<Vendor> vendors = new ArrayList<>();
        String sql = "SELECT id, name, bank_account, contact_email, gst_number, invoice_terms FROM vendor";

        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vendors.add(mapRowToVendor(rs));
            }

        } catch (SQLException e) {
            log.error("Exception in VendorRepository findAll() {}",e.getMessage());
        }
        return vendors;
    }

    // Update vendor
    public void update(Long id,Vendor vendor) {
        String sql = "UPDATE vendor SET name = ?, bank_account = ?, contact_email = ?, gst_number = ?, invoice_terms = ? WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vendor.getName());
            stmt.setString(2, vendor.getBankAccount());
            stmt.setString(3, vendor.getContactEmail());
            stmt.setString(4, vendor.getGstNumber());
            stmt.setString(5, vendor.getInvoiceTerms());
            stmt.setLong(6, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception in VendorRepository update() {}",e.getMessage());
        }
    }

    // Delete vendor
    public void delete(Long id) {
        String sql = "DELETE FROM vendor WHERE id = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception in VendorRepository delete() {}",e.getMessage());
        }
    }

    // --- Helper method ---
    private Vendor mapRowToVendor(ResultSet rs) throws SQLException {
        Vendor vendor = new Vendor();
        vendor.setId(rs.getLong("id"));
        vendor.setName(rs.getString("name"));
        vendor.setBankAccount(rs.getString("bank_account"));
        vendor.setContactEmail(rs.getString("contact_email"));
        vendor.setGstNumber(rs.getString("gst_number"));
        vendor.setInvoiceTerms(rs.getString("invoice_terms"));
        return vendor;
    }
}
