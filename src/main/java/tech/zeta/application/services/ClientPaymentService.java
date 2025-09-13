package tech.zeta.application.services;

import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.ClientPayment;
import tech.zeta.application.models.SalaryPayment;
import tech.zeta.application.repositories.ClientPaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ClientPaymentService {

    private final ClientPaymentRepository clientPaymentRepository;
    private static ClientPaymentService instance  = null;
    private ClientPaymentService() {
        this.clientPaymentRepository = new ClientPaymentRepository();
    }
    public static ClientPaymentService getInstance(){
        if(instance == null){
            instance =  new ClientPaymentService();
        }
        return instance;
    }
    // Create new client payment
    public ClientPayment createClientPayment(ClientPayment payment) {
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }
        if (payment.getDirection() == null) {
            throw new IllegalArgumentException("Payment direction is required");
        }
        if(!payment.getDirection().name().equals(PaymentDirection.INCOMING.name())){
            throw new IllegalArgumentException("Payment direction for client must be incoming");
        }
        if (payment.getStatus() == null || payment.getStatus().name().isBlank()) {
            throw new IllegalArgumentException("Payment status is required");
        }
        if (payment.getClientId() == null || payment.getClientId() <= 0) {
            throw new IllegalArgumentException("Client ID is required");
        }

        clientPaymentRepository.save(payment);
        return payment;
    }

    // Fetch by ID
    public Optional<ClientPayment> getClientPaymentById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid payment ID");
        }
        return clientPaymentRepository.findById(id);
    }

    // Fetch all
    public List<ClientPayment> getAllClientPayments() {
        return clientPaymentRepository.findAll();
    }

    // Update payment
    public void updateClientPayment(Long id,ClientPayment payment) {
        if (payment.getId() == null || payment.getId() <= 0) {
            throw new IllegalArgumentException("Payment ID is required for update");
        }

        Optional<ClientPayment> existing = clientPaymentRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Client payment not found with ID: " + id);
        }

        clientPaymentRepository.update(id,payment);
    }

    // Delete payment
    public void deleteClientPayment(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid payment ID");
        }

        Optional<ClientPayment> existing = clientPaymentRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Client payment not found with ID: " + id);
        }

        clientPaymentRepository.delete(id);
    }


    /**
     * Mark a SalaryPayment as completed
     */
    public void markPaymentAsCompleted(Long id) {
        Optional<ClientPayment> clientPayment = clientPaymentRepository.findById(id);
        if (clientPayment.isPresent()) {
            ClientPayment payment = clientPayment.get();
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setUpdatedAt(LocalDateTime.now());
            clientPaymentRepository.update(id,payment);
        } else {
            throw new IllegalArgumentException("Salary payment with id " + id + " not found");
        }
    }

    /**
     * Mark a SalaryPayment as failed
     */
    public void markPaymentAsFailed(Long id) {
        Optional<ClientPayment> optionalPayment = clientPaymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            ClientPayment payment = optionalPayment.get();
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            clientPaymentRepository.update(id,payment);
        } else {
            throw new IllegalArgumentException("Salary payment with id " + id + " not found");
        }
    }
}
