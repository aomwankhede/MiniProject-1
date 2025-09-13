package tech.zeta.application.services;

import tech.zeta.application.models.Employee;
import tech.zeta.application.repositories.EmployeeRepository;

import java.util.List;
import java.util.Optional;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private static EmployeeService instance = null;
    private EmployeeService() {
        this.employeeRepository = new EmployeeRepository();
    }
    public static EmployeeService getInstance(){
        if(instance == null){
            instance =  new EmployeeService();
        }
        return instance;
    }

    // Create new employee with validations
    public Employee createEmployee(Employee employee) {
        if (employee.getName() == null || employee.getName().isBlank()) {
            throw new IllegalArgumentException("Employee name cannot be null or blank");
        }
        if (employee.getContactEmail() == null || employee.getContactEmail().isBlank()) {
            throw new IllegalArgumentException("Employee contact email cannot be null or blank");
        }
        if (employee.getDepartment() == null || employee.getDepartment().isBlank()) {
            throw new IllegalArgumentException("Employee department is required");
        }
        if (employee.getPanNumber() == null || employee.getPanNumber().isBlank()) {
            throw new IllegalArgumentException("PAN number is required");
        }

        employeeRepository.save(employee);
        return employee;
    }

    // Fetch employee by ID
    public Optional<Employee> getEmployeeById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid employee ID");
        }
        return employeeRepository.findById(id);
    }

    // Fetch all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Update employee details
    public void updateEmployee(Long id,Employee employee) {
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee ID is required for update");
        }

        Optional<Employee> existing = employeeRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }

        employeeRepository.update(id,employee);
    }

    // Delete employee by ID
    public void deleteEmployee(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid employee ID");
        }

        Optional<Employee> existing = employeeRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }

        employeeRepository.delete(id);
    }
}
