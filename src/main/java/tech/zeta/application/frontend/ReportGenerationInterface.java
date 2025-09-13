package tech.zeta.application.frontend;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.*;
import tech.zeta.application.services.ReportGenerationService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ReportGenerationInterface {
    private final ReportGenerationService reportService = ReportGenerationService.getInstance();
    private final Scanner sc = new Scanner(System.in);

    public void display() {
        while (true) {
            System.out.println("\n=== Report Generation Menu ===");
            System.out.println("1. Generate User Report by Role");
            System.out.println("2. Generate Vendor Payment Report");
            System.out.println("3. Generate Salary Payment Report");
            System.out.println("4. Generate Client Payment Report");
            System.out.println("5. Generate Overall Payment Summary Report");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> generateUserReport();
                case 2 -> generateVendorReport();
                case 3 -> generateSalaryReport();
                case 4 -> generateClientReport();
                case 5 -> generateOverallPaymentReport();
                case 0 -> {
                    System.out.println("Returning to main menu...");
                    return;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void generateUserReport() {
        System.out.print("Enter role name (e.g., Admin, Financial Manager): ");
        String role = sc.nextLine();

        List<User> users = reportService.getAllUsersByType(role);
        StringBuilder report = new StringBuilder("=== User Report for Role: " + role + " ===\n");

        for (User u : users) {
            report.append("ID: ").append(u.getId())
                    .append(", Username: ").append(u.getUsername())
                    .append(", Email: ").append(u.getEmail())
                    .append("\n");
        }
        writeToFile("src/main/reports/UserReport_" + role + ".txt", report.toString());
    }

    private void generateVendorReport() {
        PaymentStatus status = askPaymentStatus();
        List<VendorPayment> payments = reportService.getVendorPaymentReport(status);

        StringBuilder report = new StringBuilder("=== Vendor Payment Report (" + status + ") ===\n");
        for (VendorPayment p : payments) {
            report.append("ID: ").append(p.getId())
                    .append(", Vendor ID: ").append(p.getVendorId())
                    .append(", Amount: ").append(p.getAmount())
                    .append(", Status: ").append(p.getStatus())
                    .append("\n");
        }

        writeToFile("src/main/reports/VendorPaymentReport_" + status + ".txt", report.toString());
    }

    private void generateSalaryReport() {
        PaymentStatus status = askPaymentStatus();
        List<SalaryPayment> payments = reportService.getSalaryPaymentReport(status);

        StringBuilder report = new StringBuilder("=== Salary Payment Report (" + status + ") ===\n");
        for (SalaryPayment p : payments) {
            report.append("ID: ").append(p.getId())
                    .append(", Employee ID: ").append(p.getEmployeeId())
                    .append(", Amount: ").append(p.getAmount())
                    .append(", Status: ").append(p.getStatus())
                    .append("\n");
        }

        writeToFile("src/main/reports/SalaryPaymentReport_" + status + ".txt", report.toString());
    }

    private void generateClientReport() {
        PaymentStatus status = askPaymentStatus();
        List<ClientPayment> payments = reportService.getClientPaymentReport(status);

        StringBuilder report = new StringBuilder("=== Client Payment Report (" + status + ") ===\n");
        for (ClientPayment p : payments) {
            report.append("ID: ").append(p.getId())
                    .append(", Client ID: ").append(p.getClientId())
                    .append(", Amount: ").append(p.getAmount())
                    .append(", Status: ").append(p.getStatus())
                    .append("\n");
        }

        writeToFile("src/main/reports/ClientPaymentReport_" + status + ".txt", report.toString());
    }

    private void generateOverallPaymentReport() {
        List<VendorPayment> vendorPayments = new ArrayList<>(reportService.getVendorPaymentReport(PaymentStatus.PENDING)
                .stream().toList());
        vendorPayments.addAll(reportService.getVendorPaymentReport(PaymentStatus.COMPLETED));
        vendorPayments.addAll(reportService.getVendorPaymentReport(PaymentStatus.FAILED));

        List<ClientPayment> clientPayments = new ArrayList<>(reportService.getClientPaymentReport(PaymentStatus.PENDING)
                .stream().toList());
        clientPayments.addAll(reportService.getClientPaymentReport(PaymentStatus.COMPLETED));
        clientPayments.addAll(reportService.getClientPaymentReport(PaymentStatus.FAILED));

        List<SalaryPayment> salaryPayments = new ArrayList<>(reportService.getSalaryPaymentReport(PaymentStatus.PENDING)
                .stream().toList());
        salaryPayments.addAll(reportService.getSalaryPaymentReport(PaymentStatus.COMPLETED));
        salaryPayments.addAll(reportService.getSalaryPaymentReport(PaymentStatus.FAILED));

        Double totalIncoming = 0.0;
        Double totalOutgoing = 0.0;
        int pendingCount = 0, completedCount = 0, failedCount = 0;

        // Vendor
        for (VendorPayment vp : vendorPayments) {
            if ("INCOMING".equalsIgnoreCase(vp.getDirection().name())) {
                totalIncoming = totalIncoming + vp.getAmount();
            } else {
                totalOutgoing = totalOutgoing + vp.getAmount();
            }
            pendingCount += vp.getStatus() == PaymentStatus.PENDING ? 1 : 0;
            completedCount += vp.getStatus() == PaymentStatus.COMPLETED ? 1 : 0;
            failedCount += vp.getStatus() == PaymentStatus.FAILED ? 1 : 0;
        }

        // Client
        for (ClientPayment cp : clientPayments) {
            if ("INCOMING".equalsIgnoreCase(cp.getDirection().name())) {
                totalIncoming = totalIncoming + cp.getAmount();
            } else {
                totalOutgoing = totalOutgoing + cp.getAmount();
            }
            pendingCount += cp.getStatus() == PaymentStatus.PENDING ? 1 : 0;
            completedCount += cp.getStatus() == PaymentStatus.COMPLETED ? 1 : 0;
            failedCount += cp.getStatus() == PaymentStatus.FAILED ? 1 : 0;
        }

        // Salary
        for (SalaryPayment sp : salaryPayments) {
            if ("INCOMING".equalsIgnoreCase(sp.getDirection().name())) {
                totalIncoming = totalIncoming + sp.getAmount();
            } else {
                totalOutgoing = totalOutgoing + sp.getAmount();
            }
            pendingCount += sp.getStatus() == PaymentStatus.PENDING ? 1 : 0;
            completedCount += sp.getStatus() == PaymentStatus.COMPLETED ? 1 : 0;
            failedCount += sp.getStatus() == PaymentStatus.FAILED ? 1 : 0;
        }

        StringBuilder report = new StringBuilder("=== Overall Payment Summary Report ===\n");
        report.append("Total Incoming: ").append(totalIncoming).append("\n");
        report.append("Total Outgoing: ").append(totalOutgoing).append("\n");
        report.append("Pending Payments: ").append(pendingCount).append("\n");
        report.append("Completed Payments: ").append(completedCount).append("\n");
        report.append("Failed Payments: ").append(failedCount).append("\n");

        writeToFile("src/main/reports/OverallPaymentReport.txt", report.toString());
    }

    private PaymentStatus askPaymentStatus() {
        System.out.println("Select Payment Status:");
        for (PaymentStatus ps : PaymentStatus.values()) {
            System.out.println("- " + ps);
        }
        System.out.print("Enter status: ");
        String statusStr = sc.nextLine().toUpperCase();

        try {
            return PaymentStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status, defaulting to PENDING");
            return PaymentStatus.PENDING;
        }
    }

    private void writeToFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
            log.info("Report saved as: {} ",fileName);
        } catch (IOException e) {
            log.info("Error writing file: {}",e.getMessage());
        }
    }
}
