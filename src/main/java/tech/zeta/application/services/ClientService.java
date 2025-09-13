package tech.zeta.application.services;

import tech.zeta.application.models.Client;
import tech.zeta.application.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientService {

    private final ClientRepository clientRepository;
    private static ClientService instance = null;
    private ClientService() {
        this.clientRepository = new ClientRepository();
    }
    public static ClientService getInstance(){
        if(instance == null){
            instance =  new ClientService();
        }
        return instance;
    }
    // Create new client with validation
    public Client createClient(Client client) {
        if (client.getName() == null || client.getName().isBlank()) {
            throw new IllegalArgumentException("Client name cannot be null or blank");
        }
        if (client.getContactEmail() == null || client.getContactEmail().isBlank()) {
            throw new IllegalArgumentException("Contact email cannot be null or blank");
        }
        if (client.getCompany() == null || client.getCompany().isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or blank");
        }

        clientRepository.save(client);
        return client;
    }

    // Fetch client by ID
    public Optional<Client> getClientById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid client ID");
        }
        return clientRepository.findById(id);
    }

    // Fetch all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Update client info
    public void updateClient(Long id,Client client) {
        if (client.getId() == null) {
            throw new IllegalArgumentException("Client ID is required for update");
        }

        Optional<Client> existing = clientRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Client not found with ID: " + client.getId());
        }

        clientRepository.update(id,client);
    }

    // Delete client by ID
    public void deleteClient(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid client ID");
        }

        Optional<Client> existing = clientRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Client not found with ID: " + id);
        }

        clientRepository.delete(id);
    }
}
