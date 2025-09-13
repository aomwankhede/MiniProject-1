package tech.zeta.application.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.Client;
import tech.zeta.application.models.ClientPayment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientPaymentServiceTest {

    private final ClientPaymentService clientPaymentService = ClientPaymentService.getInstance();
    private final ClientService clientService = ClientService.getInstance();

    private Client testClient;
    private ClientPayment testPayment;

    @BeforeAll
    void setup() {
        // --- Create a new test client (set ALL client fields) ---
        testClient = new Client();
        testClient.setName("JUnit Test Client");
        testClient.setBankAccount("BANK-123-456");
        testClient.setContactEmail("junit_client@example.com");
        testClient.setCompany("JUnit Co.");
        testClient.setContractId("CON-TEST-001");

        // Persist client (should set ID on the object)
        clientService.createClient(testClient);
        assertNotNull(testClient.getId(), "Test client should be saved with an ID");

        // --- Create a new test payment with all required payment fields ---
        testPayment = new ClientPayment();
        testPayment.setAmount(1000.0);
        testPayment.setClientId(testClient.getId());
        testPayment.setDirection(PaymentDirection.INCOMING);
        testPayment.setStatus(PaymentStatus.PENDING);
        testPayment.setCreatedAt(LocalDateTime.now());
        testPayment.setUpdatedAt(LocalDateTime.now());

        clientPaymentService.createClientPayment(testPayment);
        assertNotNull(testPayment.getId(), "Test payment should be saved with an ID");
    }

    @AfterAll
    void cleanup() {
        // delete payment first (FK constraint safety)
        try {
            if (testPayment != null && testPayment.getId() != null) {
                clientPaymentService.deleteClientPayment(testPayment.getId());
            }
        } catch (Exception e) {
            log.error("Error : {} {}",e.getMessage(),"You will have to clean  manually");
        }

        // delete client
        try {
            if (testClient != null && testClient.getId() != null) {
                clientService.deleteClient(testClient.getId());
            }
        } catch (Exception e) {
            log.error("Error : {} {}",e.getMessage(),"You will have to clean  manually");
        }
    }

    @Test
    @Order(1)
    void testGetInstance() {
        assertNotNull(clientPaymentService, "Service instance should not be null");
    }

    @Test
    @Order(2)
    void testCreateClientPayment() {
        assertNotNull(testPayment.getId(), "Created payment should have an ID assigned by DB");
        assertEquals(1000.0, testPayment.getAmount(), 0.0001);
        assertEquals(PaymentStatus.PENDING, testPayment.getStatus());
        assertEquals(PaymentDirection.INCOMING, testPayment.getDirection());
        assertEquals(testClient.getId(), testPayment.getClientId());
    }

    @Test
    @Order(3)
    void testGetClientPaymentById() {
        Optional<ClientPayment> payment = clientPaymentService.getClientPaymentById(testPayment.getId());
        assertTrue(payment.isPresent(), "Payment should exist");
        assertEquals(testPayment.getId(), payment.get().getId());
    }

    @Test
    @Order(4)
    void testGetAllClientPayments() {
        List<ClientPayment> all = clientPaymentService.getAllClientPayments();
        assertNotNull(all, "List should not be null");
        assertTrue(all.stream().anyMatch(p -> p.getId().equals(testPayment.getId())),
                "Test payment should be in the list");
    }

    @Test
    @Order(5)
    void testUpdateClientPayment() {
        Double oldAmount = testPayment.getAmount();
        testPayment.setAmount(oldAmount + 500);
        testPayment.setUpdatedAt(LocalDateTime.now());

        clientPaymentService.updateClientPayment(testPayment.getId(), testPayment);

        Optional<ClientPayment> updated = clientPaymentService.getClientPaymentById(testPayment.getId());
        assertTrue(updated.isPresent());
        assertEquals(oldAmount + 500, updated.get().getAmount(), 0.0001);
    }

    @Test
    @Order(6)
    void testMarkPaymentAsCompleted() {
        clientPaymentService.markPaymentAsCompleted(testPayment.getId());

        Optional<ClientPayment> updated = clientPaymentService.getClientPaymentById(testPayment.getId());
        assertTrue(updated.isPresent());
        assertEquals(PaymentStatus.COMPLETED, updated.get().getStatus());
    }

    @Test
    @Order(7)
    void testMarkPaymentAsFailed() {
        clientPaymentService.markPaymentAsFailed(testPayment.getId());

        Optional<ClientPayment> updated = clientPaymentService.getClientPaymentById(testPayment.getId());
        assertTrue(updated.isPresent());
        assertEquals(PaymentStatus.FAILED, updated.get().getStatus());
    }
}
