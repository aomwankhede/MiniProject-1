package tech.zeta.application.services;

import org.junit.jupiter.api.*;
import tech.zeta.application.models.Vendor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VendorServiceTest {

    private final VendorService vendorService = VendorService.getInstance();
    private Vendor testVendor;

    @BeforeAll
    void setup() {
        testVendor = new Vendor();
        testVendor.setName("JUnit Vendor");
        testVendor.setBankAccount("VEN-BANK-999");
        testVendor.setContactEmail("vendor@test.com");
        testVendor.setGstNumber("27ABCDE1234F1Z5");
        testVendor.setInvoiceTerms("NET 30");

        vendorService.createVendor(testVendor);
        assertNotNull(testVendor.getId(), "Test vendor should be saved with an ID");
    }

    @AfterAll
    void cleanup() {
        if (testVendor != null && testVendor.getId() != null) {
            vendorService.deleteVendor(testVendor.getId());
        }
    }

    @Test
    @Order(1)
    void testCreateVendor() {
        assertEquals("JUnit Vendor", testVendor.getName());
        assertEquals("27ABCDE1234F1Z5", testVendor.getGstNumber());
    }

    @Test
    @Order(2)
    void testGetVendorById() {
        Optional<Vendor> found = vendorService.getVendorById(testVendor.getId());
        assertTrue(found.isPresent());
        assertEquals(testVendor.getId(), found.get().getId());
    }

    @Test
    @Order(3)
    void testGetAllVendors() {
        List<Vendor> all = vendorService.getAllVendors();
        assertTrue(all.stream().anyMatch(v -> v.getId().equals(testVendor.getId())));
    }

    @Test
    @Order(4)
    void testUpdateVendor() {
        testVendor.setInvoiceTerms("NET 45");
        vendorService.updateVendor(testVendor.getId(), testVendor);

        Optional<Vendor> updated = vendorService.getVendorById(testVendor.getId());
        assertTrue(updated.isPresent());
        assertEquals("NET 45", updated.get().getInvoiceTerms());
    }
}
