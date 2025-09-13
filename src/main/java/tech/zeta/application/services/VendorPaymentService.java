package tech.zeta.application.services;

import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.VendorPayment;
import tech.zeta.application.repositories.VendorPaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class VendorPaymentService {

    private final VendorPaymentRepository vendorPaymentRepository;
    private static VendorPaymentService instance = null;
    private VendorPaymentService() {
        this.vendorPaymentRepository = new VendorPaymentRepository();
    }
    public static VendorPaymentService getInstance(){
        if(instance == null){
            instance =  new VendorPaymentService();
        }
        return instance;
    }

    public VendorPayment createVendorPayment(VendorPayment vendorPayment) {
        if (vendorPayment.getAmount() == null || vendorPayment.getAmount() <= 0) {
            throw new IllegalArgumentException("Vendor payment amount must be greater than zero");
        }

        if(!vendorPayment.getDirection().name().equals(PaymentDirection.OUTGOING.name())){
            throw new IllegalArgumentException("Vendor payment must have outgoing direction");
        }

        vendorPaymentRepository.save(vendorPayment);
        return vendorPayment;
    }


    public Optional<VendorPayment> getVendorPaymentById(Long id) {
        return vendorPaymentRepository.findById(id);
    }


    public List<VendorPayment> getAllVendorPayments() {
        return vendorPaymentRepository.findAll();
    }


    public void updateVendorPayment(Long id,VendorPayment payment) {
        if (payment.getId() == null) {
            throw new IllegalArgumentException("Payment ID cannot be null for update");
        }
        if(vendorPaymentRepository.findById(id).isEmpty()){
            throw new IllegalArgumentException("No such Vendor payment exists");
        }
        payment.setUpdatedAt(LocalDateTime.now());
        vendorPaymentRepository.update(id,payment);
    }


    public void deleteVendorPayment(Long id) {
        vendorPaymentRepository.delete(id);
    }


    public void markPaymentAsCompleted(Long id) {
        Optional<VendorPayment> optionalPayment = vendorPaymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            VendorPayment payment = optionalPayment.get();
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setUpdatedAt(LocalDateTime.now());
            vendorPaymentRepository.update(id,payment);
        } else {
            throw new IllegalArgumentException("Vendor payment with id " + id + " not found");
        }
    }

    public void markPaymentAsFailed(Long id) {
        Optional<VendorPayment> optionalPayment = vendorPaymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            VendorPayment payment = optionalPayment.get();
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            vendorPaymentRepository.update(id,payment);
        } else {
            throw new IllegalArgumentException("Vendor payment with id " + id + " not found");
        }
    }
}
