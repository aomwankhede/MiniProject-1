package tech.zeta.application.services;

import org.junit.jupiter.api.*;
import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.Vendor;
import tech.zeta.application.models.VendorPayment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VendorPaymentServiceTest {

    private final VendorPaymentService vendorPaymentService = VendorPaymentService.getInstance();
    private final VendorService vendorService = VendorService.getInstance();

    private Vendor testVendor;
    private VendorPayment testPayment;

    @BeforeAll
    void setup() {
        // --- Create test vendor ---
        testVendor = new Vendor();
        testVendor.setName("JUnit Vendor");
        testVendor.setBankAccount("VEN-BANK-987");
        testVendor.setContactEmail("vendor@test.com");
        testVendor.setGstNumber("27ABCDE1234F1Z5");
        testVendor.setInvoiceTerms("NET 30");

        vendorService.createVendor(testVendor);
        assertNotNull(testVendor.getId(), "Test vendor should be saved with an ID");

        // --- Create test vendor payment ---
        testPayment = new VendorPayment();
        testPayment.setVendorId(testVendor.getId());
        testPayment.setAmount(25000.0);
        testPayment.setDirection(PaymentDirection.OUTGOING);
        testPayment.setStatus(PaymentStatus.PENDING);
        testPayment.setCreatedAt(LocalDateTime.now());
        testPayment.setUpdatedAt(LocalDateTime.now());

        vendorPaymentService.createVendorPayment(testPayment);
        assertNotNull(testPayment.getId(), "Test vendor payment should be saved with an ID");
    }

    @AfterAll
    void cleanup() {
        try {
            if (testPayment != null && testPayment.getId() != null) {
                vendorPaymentService.deleteVendorPayment(testPayment.getId());
            }
        } catch (Exception ignored) {}
        try {
            if (testVendor != null && testVendor.getId() != null) {
                vendorService.deleteVendor(testVendor.getId());
            }
        } catch (Exception ignored) {}
    }

    @Test
    @Order(1)
    void testGetInstance() {
        assertNotNull(vendorPaymentService);
    }

    @Test
    @Order(2)
    void testCreateVendorPayment() {
        assertEquals(25000.0, testPayment.getAmount(), 0.0001);
        assertEquals(PaymentStatus.PENDING, testPayment.getStatus());
        assertEquals(PaymentDirection.OUTGOING, testPayment.getDirection());
    }

    @Test
    @Order(3)
    void testGetVendorPaymentById() {
        Optional<VendorPayment> payment = vendorPaymentService.getVendorPaymentById(testPayment.getId());
        assertTrue(payment.isPresent());
        assertEquals(testPayment.getId(), payment.get().getId());
    }

    @Test
    @Order(4)
    void testGetAllVendorPayments() {
        List<VendorPayment> all = vendorPaymentService.getAllVendorPayments();
        assertTrue(all.stream().anyMatch(p -> p.getId().equals(testPayment.getId())));
    }

    @Test
    @Order(5)
    void testUpdateVendorPayment() {
        Double oldAmount = testPayment.getAmount();
        testPayment.setAmount(oldAmount + 1500);
        vendorPaymentService.updateVendorPayment(testPayment.getId(), testPayment);

        Optional<VendorPayment> updated = vendorPaymentService.getVendorPaymentById(testPayment.getId());
        assertTrue(updated.isPresent());
        assertEquals(oldAmount + 1500, updated.get().getAmount(), 0.0001);
    }

    @Test
    @Order(6)
    void testMarkPaymentAsCompleted() {
        vendorPaymentService.markPaymentAsCompleted(testPayment.getId());
        Optional<VendorPayment> updated = vendorPaymentService.getVendorPaymentById(testPayment.getId());
        assertEquals(PaymentStatus.COMPLETED, updated.get().getStatus());
    }

    @Test
    @Order(7)
    void testMarkPaymentAsFailed() {
        vendorPaymentService.markPaymentAsFailed(testPayment.getId());
        Optional<VendorPayment> updated = vendorPaymentService.getVendorPaymentById(testPayment.getId());
        assertEquals(PaymentStatus.FAILED, updated.get().getStatus());
    }
}
