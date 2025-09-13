package tech.zeta.application.services;

import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.*;
import tech.zeta.application.repositories.*;

import java.util.List;

public class ReportGenerationService {
    private static ReportGenerationService instance;
    private final VendorPaymentRepository vendorPaymentRepo;
    private final ClientPaymentRepository clientPaymentRepo;
    private final SalaryPaymentRepository salaryPaymentRepo;
    private final RoleRepository roleRepository;
    private final UserRepository userRepo;

    private ReportGenerationService() {
        this.vendorPaymentRepo = new VendorPaymentRepository();
        this.clientPaymentRepo = new ClientPaymentRepository();
        this.salaryPaymentRepo = new SalaryPaymentRepository();
        this.roleRepository = new RoleRepository();
        this.userRepo = new UserRepository();
    }

    public static ReportGenerationService getInstance() {
        if (instance == null) {
            instance = new ReportGenerationService();
        }
        return instance;
    }

    public List<User> getAllUsersByType(String type){
        Long targetRoleId = roleRepository.findAll().stream().filter(role -> role.getName().equals(type)).toList().getFirst().getId();
        return userRepo.findAll().stream().filter(user -> user.getRoleId().equals(targetRoleId)).toList();
    }

    public List<VendorPayment> getVendorPaymentReport(PaymentStatus status){
        return vendorPaymentRepo.findAll().stream().filter(vendorPayment -> vendorPayment.getStatus() == status).toList();
    }

    public List<SalaryPayment> getSalaryPaymentReport(PaymentStatus status){
        return salaryPaymentRepo.findAll().stream().filter(vendorPayment -> vendorPayment.getStatus() == status).toList();
    }

    public List<ClientPayment> getClientPaymentReport(PaymentStatus status){
        return clientPaymentRepo.findAll().stream().filter(vendorPayment -> vendorPayment.getStatus() == status).toList();
    }


}
