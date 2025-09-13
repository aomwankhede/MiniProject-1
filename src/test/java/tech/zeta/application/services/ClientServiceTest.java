package tech.zeta.application.services;

import org.junit.jupiter.api.*;
import tech.zeta.application.models.Client;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientServiceTest {

    private final ClientService clientService = ClientService.getInstance();
    private Client testClient;

    @BeforeAll
    void setup() {
        testClient = new Client();
        testClient.setName("JUnit Client");
        testClient.setBankAccount("CLI-BANK-555");
        testClient.setContactEmail("client@test.com");
        testClient.setCompany("Zeta");
        testClient.setContractId("7635476617");
        clientService.createClient(testClient);
        assertNotNull(testClient.getId(), "Test client should be saved with an ID");
    }

    @AfterAll
    void cleanup() {
        if (testClient != null && testClient.getId() != null) {
            clientService.deleteClient(testClient.getId());
        }
    }

    @Test
    @Order(1)
    void testCreateClient() {
        assertEquals("JUnit Client", testClient.getName());
        assertEquals("CLI-BANK-555", testClient.getBankAccount());
    }

    @Test
    @Order(2)
    void testGetClientById() {
        Optional<Client> found = clientService.getClientById(testClient.getId());
        assertTrue(found.isPresent());
        assertEquals(testClient.getId(), found.get().getId());
    }

    @Test
    @Order(3)
    void testGetAllClients() {
        List<Client> all = clientService.getAllClients();
        assertTrue(all.stream().anyMatch(c -> c.getId().equals(testClient.getId())));
    }

    @Test
    @Order(4)
    void testUpdateClient() {
        testClient.setName("Updated Client");
        clientService.updateClient(testClient.getId(), testClient);

        Optional<Client> updated = clientService.getClientById(testClient.getId());
        assertTrue(updated.isPresent());
        assertEquals("Updated Client", updated.get().getName());
    }
}
