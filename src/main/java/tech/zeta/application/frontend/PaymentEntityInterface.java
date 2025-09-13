package tech.zeta.application.frontend;

import tech.zeta.application.enums.Action;
import tech.zeta.application.enums.Entity;
import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.ClientPayment;
import tech.zeta.application.models.SalaryPayment;
import tech.zeta.application.models.VendorPayment;
import tech.zeta.application.services.AuthService;
import tech.zeta.application.services.ClientPaymentService;
import tech.zeta.application.services.SalaryPaymentService;
import tech.zeta.application.services.VendorPaymentService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class PaymentEntityInterface implements FrontendInterface {

    private final ClientPaymentService clientPaymentService = ClientPaymentService.getInstance();
    private final SalaryPaymentService salaryPaymentService = SalaryPaymentService.getInstance();
    private final VendorPaymentService vendorPaymentService = VendorPaymentService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void display() {
        while (true) {
            System.out.println("\n=== Payment Management ===");
            System.out.println("1. Create Client Payment");
            System.out.println("2. Create Salary Payment");
            System.out.println("3. Create Vendor Payment");
            System.out.println("4. View All Client Payments");
            System.out.println("5. View All Salary Payments");
            System.out.println("6. View All Vendor Payments");
            System.out.println("7. Mark Payment as Completed");
            System.out.println("8. Mark Payment as Failed");
            System.out.println("9. Delete Payment");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> createClientPayment();
                case 2 -> createSalaryPayment();
                case 3 -> createVendorPayment();
                case 4 -> clientPaymentService.getAllClientPayments().forEach(System.out::println);
                case 5 -> salaryPaymentService.getAllSalaryPayments().forEach(System.out::println);
                case 6 -> vendorPaymentService.getAllVendorPayments().forEach(System.out::println);
                case 7 -> markPaymentCompleted();
                case 8 -> markPaymentFailed();
                case 9 -> deletePayment();
                case 0 -> {
                    System.out.println("Exiting Payment Management...");
                    return;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void createClientPayment() {
        if(!authService.hasPermission(Action.C, Entity.CLIENT_PAYMENT)){
            System.out.println("❌ You don't have permission to create client payments.");
            return;
        }
        System.out.print("Enter client ID: ");
        Long clientId = Long.parseLong(scanner.nextLine());
        System.out.print("Enter amount: ");
        Double amount = Double.parseDouble(scanner.nextLine());

        ClientPayment payment = new ClientPayment();
        payment.setClientId(clientId);
        payment.setAmount(amount);
        payment.setDirection(PaymentDirection.INCOMING);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        clientPaymentService.createClientPayment(payment);
        System.out.println("✅ Client payment created successfully.");
    }

    private void createSalaryPayment() {
        if(!authService.hasPermission(Action.C, Entity.SALARY_PAYMENT)){
            System.out.println("❌ You don't have permission to create salary payments.");
            return;
        }
        System.out.print("Enter employee ID: ");
        Long employeeId = Long.parseLong(scanner.nextLine());
        System.out.print("Enter salary amount: ");
        Double amount = Double.parseDouble(scanner.nextLine());

        SalaryPayment payment = new SalaryPayment();
        payment.setEmployeeId(employeeId);
        payment.setAmount(amount);
        payment.setDirection(PaymentDirection.OUTGOING);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        salaryPaymentService.createSalaryPayment(payment);
        System.out.println("✅ Salary payment created successfully.");
    }

    private void createVendorPayment() {
        if(!authService.hasPermission(Action.C, Entity.VENDOR_PAYMENT)){
            System.out.println("❌ You don't have permission to create vendor payments.");
            return;
        }
        System.out.print("Enter vendor ID: ");
        Long vendorId = Long.parseLong(scanner.nextLine());
        System.out.print("Enter payment amount: ");
        Double amount = Double.parseDouble(scanner.nextLine());

        VendorPayment payment = new VendorPayment();
        payment.setVendorId(vendorId);
        payment.setAmount(amount);
        payment.setDirection(PaymentDirection.OUTGOING);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        vendorPaymentService.createVendorPayment(payment);
        System.out.println("✅ Vendor payment created successfully.");
    }

    private void markPaymentCompleted() {
        if(!authService.hasPermission(Action.U, Entity.CLIENT_PAYMENT)){
            System.out.println("❌ You don't have permission to update payments.");
            return;
        }
        System.out.print("Enter Payment Type (client/salary/vendor): ");
        String type = scanner.nextLine().toLowerCase();
        System.out.print("Enter Payment ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        switch (type) {
            case "client" -> clientPaymentService.markPaymentAsCompleted(id);
            case "salary" -> salaryPaymentService.markPaymentAsCompleted(id);
            case "vendor" -> vendorPaymentService.markPaymentAsCompleted(id);
            default -> System.out.println("Invalid type");
        }
        System.out.println("✅ Payment marked as completed.");
    }

    private void markPaymentFailed() {
        if(!authService.hasPermission(Action.U, Entity.CLIENT_PAYMENT)){
            System.out.println("❌ You don't have permission to update payments.");
            return;
        }
        System.out.print("Enter Payment Type (client/salary/vendor): ");
        String type = scanner.nextLine().toLowerCase();
        System.out.print("Enter Payment ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        switch (type) {
            case "client" -> clientPaymentService.markPaymentAsFailed(id);
            case "salary" -> salaryPaymentService.markPaymentAsFailed(id);
            case "vendor" -> vendorPaymentService.markPaymentAsFailed(id);
            default -> System.out.println("Invalid type");
        }
        System.out.println("❌ Payment marked as failed.");
    }

    private void deletePayment() {
        if(!authService.hasPermission(Action.D, Entity.CLIENT_PAYMENT)){
            System.out.println("❌ You don't have permission to delete payments.");
            return;
        }
        System.out.print("Enter Payment Type (client/salary/vendor): ");
        String type = scanner.nextLine().toLowerCase();
        System.out.print("Enter Payment ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        switch (type) {
            case "client" -> clientPaymentService.deleteClientPayment(id);
            case "salary" -> salaryPaymentService.deleteSalaryPayment(id);
            case "vendor" -> vendorPaymentService.deleteVendorPayment(id);
            default -> System.out.println("Invalid type");
        }
        System.out.println("🗑 Payment deleted successfully.");
    }
}
