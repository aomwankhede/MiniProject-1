package tech.zeta.application.services;

import lombok.NoArgsConstructor;
import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.SalaryPayment;
import tech.zeta.application.repositories.SalaryPaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SalaryPaymentService {

    private final SalaryPaymentRepository salaryPaymentRepository;
    private static SalaryPaymentService instance = null;
    private SalaryPaymentService() {
        this.salaryPaymentRepository = new SalaryPaymentRepository();
    }

    public static SalaryPaymentService getInstance(){
        if(instance == null){
            instance =  new SalaryPaymentService();
        }
        return instance;
    }

    public SalaryPayment createSalaryPayment(SalaryPayment payment) {
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Salary amount must be greater than zero");
        }
        if(!payment.getDirection().name().equals(PaymentDirection.OUTGOING.name())){
            throw new IllegalArgumentException("Salary amount must be a outgoing payment");
        }
        salaryPaymentRepository.save(payment);
        return payment;
    }

    public Optional<SalaryPayment> getSalaryPaymentById(Long id) {
        return salaryPaymentRepository.findById(id);
    }

    public List<SalaryPayment> getAllSalaryPayments() {
        return salaryPaymentRepository.findAll();
    }

    public void updateSalaryPayment(Long id,SalaryPayment payment) {
        if (payment.getId() == null) {
            throw new IllegalArgumentException("Payment ID cannot be null for update");
        }
        if(salaryPaymentRepository.findById(id).isEmpty()){
            throw new IllegalArgumentException("No such salary payment exists");
        }
        payment.setUpdatedAt(LocalDateTime.now());
        salaryPaymentRepository.update(id,payment);
    }

    public void deleteSalaryPayment(Long id) {
        salaryPaymentRepository.delete(id);
    }

    public void markPaymentAsCompleted(Long id) {
        Optional<SalaryPayment> optionalPayment = salaryPaymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            SalaryPayment payment = optionalPayment.get();
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setUpdatedAt(LocalDateTime.now());
            salaryPaymentRepository.update(id,payment);
        } else {
            throw new IllegalArgumentException("Salary payment with id " + id + " not found");
        }
    }

    public void markPaymentAsFailed(Long id) {
        Optional<SalaryPayment> optionalPayment = salaryPaymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            SalaryPayment payment = optionalPayment.get();
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            salaryPaymentRepository.update(id,payment);
        } else {
            throw new IllegalArgumentException("Salary payment with id " + id + " not found");
        }
    }
}
