package tech.zeta.application.services;

import org.junit.jupiter.api.*;
import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.enums.PaymentStatus;
import tech.zeta.application.models.Employee;
import tech.zeta.application.models.SalaryPayment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SalaryPaymentServiceTest {

    private final SalaryPaymentService salaryPaymentService = SalaryPaymentService.getInstance();
    private final EmployeeService employeeService = EmployeeService.getInstance();

    private Employee testEmployee;
    private SalaryPayment testPayment;

    @BeforeAll
    void setup() {
        // --- Create test employee ---
        testEmployee = new Employee();
        testEmployee.setName("JUnit Employee");
        testEmployee.setBankAccount("EMP-BANK-123");
        testEmployee.setContactEmail("employee@test.com");
        testEmployee.setDepartment("QA");
        testEmployee.setPanNumber("ABCDE1234F");

        employeeService.createEmployee(testEmployee);
        assertNotNull(testEmployee.getId(), "Test employee should be saved with an ID");

        // --- Create test salary payment ---
        testPayment = new SalaryPayment();
        testPayment.setEmployeeId(testEmployee.getId());
        testPayment.setAmount(45000.0);
        testPayment.setDirection(PaymentDirection.OUTGOING);
        testPayment.setStatus(PaymentStatus.PENDING);
        testPayment.setCreatedAt(LocalDateTime.now());
        testPayment.setUpdatedAt(LocalDateTime.now());

        salaryPaymentService.createSalaryPayment(testPayment);
        assertNotNull(testPayment.getId(), "Test salary payment should be saved with an ID");
    }

    @AfterAll
    void cleanup() {
        try {
            if (testPayment != null && testPayment.getId() != null) {
                salaryPaymentService.deleteSalaryPayment(testPayment.getId());
            }
        } catch (Exception ignored) {}
        try {
            if (testEmployee != null && testEmployee.getId() != null) {
                employeeService.deleteEmployee(testEmployee.getId());
            }
        } catch (Exception ignored) {}
    }

    @Test
    @Order(1)
    void testGetInstance() {
        assertNotNull(salaryPaymentService);
    }

    @Test
    @Order(2)
    void testCreateSalaryPayment() {
        assertEquals(45000.0, testPayment.getAmount(), 0.0001);
        assertEquals(PaymentStatus.PENDING, testPayment.getStatus());
        assertEquals(PaymentDirection.OUTGOING, testPayment.getDirection());
    }

    @Test
    @Order(3)
    void testGetSalaryPaymentById() {
        Optional<SalaryPayment> payment = salaryPaymentService.getSalaryPaymentById(testPayment.getId());
        assertTrue(payment.isPresent());
        assertEquals(testPayment.getId(), payment.get().getId());
    }

    @Test
    @Order(4)
    void testGetAllSalaryPayments() {
        List<SalaryPayment> all = salaryPaymentService.getAllSalaryPayments();
        assertTrue(all.stream().anyMatch(p -> p.getId().equals(testPayment.getId())));
    }

    @Test
    @Order(5)
    void testUpdateSalaryPayment() {
        Double oldAmount = testPayment.getAmount();
        testPayment.setAmount(oldAmount + 5000);
        salaryPaymentService.updateSalaryPayment(testPayment.getId(), testPayment);

        Optional<SalaryPayment> updated = salaryPaymentService.getSalaryPaymentById(testPayment.getId());
        assertTrue(updated.isPresent());
        assertEquals(oldAmount + 5000, updated.get().getAmount(), 0.0001);
    }

    @Test
    @Order(6)
    void testMarkPaymentAsCompleted() {
        salaryPaymentService.markPaymentAsCompleted(testPayment.getId());
        Optional<SalaryPayment> updated = salaryPaymentService.getSalaryPaymentById(testPayment.getId());
        assertEquals(PaymentStatus.COMPLETED, updated.get().getStatus());
    }

    @Test
    @Order(7)
    void testMarkPaymentAsFailed() {
        salaryPaymentService.markPaymentAsFailed(testPayment.getId());
        Optional<SalaryPayment> updated = salaryPaymentService.getSalaryPaymentById(testPayment.getId());
        assertEquals(PaymentStatus.FAILED, updated.get().getStatus());
    }
}
