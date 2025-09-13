package tech.zeta.application.services;

import org.junit.jupiter.api.*;
import tech.zeta.application.models.Employee;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeServiceTest {

    private final EmployeeService employeeService = EmployeeService.getInstance();
    private Employee testEmployee;

    @BeforeAll
    void setup() {
        testEmployee = new Employee();
        testEmployee.setName("JUnit Employee");
        testEmployee.setBankAccount("EMP-BANK-123");
        testEmployee.setContactEmail("employee@test.com");
        testEmployee.setDepartment("QA");
        testEmployee.setPanNumber("ABCDE1234F");

        employeeService.createEmployee(testEmployee);
        assertNotNull(testEmployee.getId(), "Test employee should be saved with an ID");
    }

    @AfterAll
    void cleanup() {
        if (testEmployee != null && testEmployee.getId() != null) {
            employeeService.deleteEmployee(testEmployee.getId());
        }
    }

    @Test
    @Order(1)
    void testCreateEmployee() {
        assertEquals("JUnit Employee", testEmployee.getName());
        assertEquals("QA", testEmployee.getDepartment());
    }

    @Test
    @Order(2)
    void testGetEmployeeById() {
        Optional<Employee> found = employeeService.getEmployeeById(testEmployee.getId());
        assertTrue(found.isPresent());
        assertEquals(testEmployee.getId(), found.get().getId());
    }

    @Test
    @Order(3)
    void testGetAllEmployees() {
        List<Employee> all = employeeService.getAllEmployees();
        assertTrue(all.stream().anyMatch(e -> e.getId().equals(testEmployee.getId())));
    }

    @Test
    @Order(4)
    void testUpdateEmployee() {
        testEmployee.setDepartment("Development");
        employeeService.updateEmployee(testEmployee.getId(), testEmployee);

        Optional<Employee> updated = employeeService.getEmployeeById(testEmployee.getId());
        assertTrue(updated.isPresent());
        assertEquals("Development", updated.get().getDepartment());
    }
}
