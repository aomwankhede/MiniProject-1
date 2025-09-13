package tech.zeta.application.frontend;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.enums.Action;
import tech.zeta.application.enums.Entity;
import tech.zeta.application.models.Client;
import tech.zeta.application.models.Employee;
import tech.zeta.application.models.Vendor;
import tech.zeta.application.services.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
public class PartyEntityInterface implements FrontendInterface {

    private final AuthService authService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final VendorService vendorService;
    private final Scanner scanner;

    public PartyEntityInterface() {
        this.authService = AuthService.getInstance();
        this.clientService = ClientService.getInstance();
        this.employeeService = EmployeeService.getInstance();
        this.vendorService = VendorService.getInstance();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void display() {
        while (true) {
            System.out.println("\n=== Payment Management ===");
            System.out.println("1. Create Client");
            System.out.println("2. Create Vendor");
            System.out.println("3. Create Employee");
            System.out.println("4. View All Clients");
            System.out.println("5. View All Vendors");
            System.out.println("6. View All Employees");
            System.out.println("7. View Client By id");
            System.out.println("8. View Vendor By id");
            System.out.println("9. View employee By id");
            System.out.println("10. update client");
            System.out.println("11. update vendor");
            System.out.println("12. update employee");
            System.out.println("13. delete client");
            System.out.println("14. delete vendor");
            System.out.println("15. delete employee");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> createClient();
                case 2 -> createVendor();
                case 3 -> createEmployee();
                case 4 -> getAllClients();
                case 5 -> getAllVendors();
                case 6 -> getAllEmployees();
                case 7 -> getClientById();
                case 8 -> getVendorById();
                case 9 -> getEmployeeById();
                case 10 -> updateClient();
                case 11 -> updateVendor();
                case 12 -> updateEmployee();
                case 13 -> deleteClient();
                case 14 -> deleteVendor();
                case 15 -> deleteEmployee();
                case 0 -> {
                    System.out.println("Exiting Payment Management...");
                    return;
                }
                default -> log.info("Invalid choice, try again.");
            }
        }
    }


    public void createClient() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.C, Entity.CLIENT)) {
            System.out.print("Enter Client Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Bank Account: ");
            String bankAccount = scanner.nextLine();
            System.out.print("Enter Contact Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Company: ");
            String company = scanner.nextLine();
            System.out.print("Enter Contract ID: ");
            String contractId = scanner.nextLine();

            Client client = new Client();
            client.setName(name);
            client.setBankAccount(bankAccount);
            client.setContactEmail(email);
            client.setCompany(company);
            client.setContractId(contractId);
            clientService.createClient(client);
            log.info("Client {} created successfully!",name);
        } else {
            throw new SecurityException("Not authorized to create client");
        }
    }

    public void getClientById() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.R, Entity.CLIENT)) {
            System.out.print("Enter Client ID: ");
            Long id = Long.parseLong(scanner.nextLine());
            Optional<Client> client = clientService.getClientById(id);
            client.ifPresentOrElse(
                    System.out::println,
                    () -> log.info("Client not found")
            );
        } else {
            throw new SecurityException("Not authorized to read client");
        }
    }

    public void getAllClients() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.R, Entity.CLIENT)) {
            List<Client> clients = clientService.getAllClients();
            clients.forEach(System.out::println);
        } else {
            throw new SecurityException("Not authorized to read clients");
        }
    }

    public void updateClient() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.U, Entity.CLIENT)) {
            System.out.print("Enter Client ID to update: ");
            Long id = Long.parseLong(scanner.nextLine());

            System.out.print("Enter New Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter New Bank Account: ");
            String bankAccount = scanner.nextLine();
            System.out.print("Enter New Contact Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter New Company: ");
            String company = scanner.nextLine();
            System.out.print("Enter New Contract ID: ");
            String contractId = scanner.nextLine();

            Client updated = new Client();
            updated.setId(id);
            updated.setName(name);
            updated.setBankAccount(bankAccount);
            updated.setContactEmail(email);
            updated.setCompany(company);
            updated.setContractId(contractId);
            clientService.updateClient(id, updated);
            log.info("Client {} updated successfully!",name);
        } else {
            throw new SecurityException("Not authorized to update client");
        }
    }

    public void deleteClient() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.D, Entity.CLIENT)) {
            System.out.print("Enter Client ID to delete: ");
            Long id = Long.parseLong(scanner.nextLine());
            clientService.deleteClient(id);
            log.info("Client with id {} deleted successfully!",id);
        } else {
            throw new SecurityException("Not authorized to delete client");
        }
    }

    public void createEmployee() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.C, Entity.EMPLOYEE)) {
            System.out.print("Enter Employee Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Bank Account: ");
            String bankAccount = scanner.nextLine();
            System.out.print("Enter Contact Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Department: ");
            String department = scanner.nextLine();
            System.out.print("Enter PAN Number: ");
            String pan = scanner.nextLine();

            Employee employee = new Employee();
            employee.setName(name);
            employee.setBankAccount(bankAccount);
            employee.setContactEmail(email);
            employee.setDepartment(department);
            employee.setPanNumber(pan);

            employeeService.createEmployee(employee);
            log.info("Employee {} created successfully!",name);
        } else {
            throw new SecurityException("Not authorized to create employee");
        }
    }

    public void getEmployeeById() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.R, Entity.EMPLOYEE)) {
            System.out.print("Enter Employee ID: ");
            Long id = Long.parseLong(scanner.nextLine());
            Optional<Employee> emp = employeeService.getEmployeeById(id);
            emp.ifPresentOrElse(
                    System.out::println,
                    () -> log.info("Employee not found.")
            );
        } else {
            throw new SecurityException("Not authorized to read employee");
        }
    }

    public void getAllEmployees() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.R, Entity.EMPLOYEE)) {
            List<Employee> employees = employeeService.getAllEmployees();
            employees.forEach(System.out::println);
        } else {
            throw new SecurityException("Not authorized to read employees");
        }
    }

    public void updateEmployee() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.U, Entity.EMPLOYEE)) {
            System.out.print("Enter Employee ID to update: ");
            Long id = Long.parseLong(scanner.nextLine());

            System.out.print("Enter New Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter New Bank Account: ");
            String bankAccount = scanner.nextLine();
            System.out.print("Enter New Contact Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter New Department: ");
            String department = scanner.nextLine();
            System.out.print("Enter New PAN Number: ");
            String pan = scanner.nextLine();

            Employee updated = new Employee();
            updated.setId(id);
            updated.setName(name);
            updated.setBankAccount(bankAccount);
            updated.setContactEmail(email);
            updated.setDepartment(department);
            updated.setPanNumber(pan);

            employeeService.updateEmployee(id, updated);
            log.info("Employee {} updated successfully!",name);
        } else {
            throw new SecurityException("Not authorized to update employee");
        }
    }

    public void deleteEmployee() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.D, Entity.EMPLOYEE)) {
            System.out.print("Enter Employee ID to delete: ");
            Long id = Long.parseLong(scanner.nextLine());
            employeeService.deleteEmployee(id);
            log.info("Employee with id {} deleted successfully!",id);
        } else {
            throw new SecurityException("Not authorized to delete employee");
        }
    }

    public void createVendor() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.C, Entity.VENDOR)) {
            System.out.print("Enter Vendor Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Bank Account: ");
            String bankAccount = scanner.nextLine();
            System.out.print("Enter Contact Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter GST Number: ");
            String gst = scanner.nextLine();
            System.out.print("Enter Invoice Terms: ");
            String terms = scanner.nextLine();

            Vendor vendor = new Vendor();
            vendor.setName(name);
            vendor.setBankAccount(bankAccount);
            vendor.setContactEmail(email);
            vendor.setGstNumber(gst);
            vendor.setInvoiceTerms(terms);

            vendorService.createVendor(vendor);
            log.info("Vendor {} created successfully!",name);
        } else {
            throw new SecurityException("Not authorized to create vendor");
        }
    }

    public void getVendorById() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.R, Entity.VENDOR)) {
            System.out.print("Enter Vendor ID: ");
            Long id = Long.parseLong(scanner.nextLine());
            Optional<Vendor> vendor = vendorService.getVendorById(id);
            vendor.ifPresentOrElse(
                    System.out::println,
                    () -> log.info("Vendor not found.")
            );
        } else {
            throw new SecurityException("Not authorized to read vendor");
        }
    }

    public void getAllVendors() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.R, Entity.VENDOR)) {
            List<Vendor> vendors = vendorService.getAllVendors();
            vendors.forEach(System.out::println);
        } else {
            throw new SecurityException("Not authorized to read vendors");
        }
    }

    public void updateVendor() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.U, Entity.VENDOR)) {
            System.out.print("Enter Vendor ID to update: ");
            Long id = Long.parseLong(scanner.nextLine());

            System.out.print("Enter New Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter New Bank Account: ");
            String bankAccount = scanner.nextLine();
            System.out.print("Enter New Contact Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter New GST Number: ");
            String gst = scanner.nextLine();
            System.out.print("Enter New Invoice Terms: ");
            String terms = scanner.nextLine();

            Vendor updated = new Vendor();
            updated.setId(id);
            updated.setName(name);
            updated.setBankAccount(bankAccount);
            updated.setContactEmail(email);
            updated.setGstNumber(gst);
            updated.setInvoiceTerms(terms);

            vendorService.updateVendor(id, updated);

            log.info("Vendor {} updated successfully!",name);
        } else {
            throw new SecurityException("Not authorized to update vendor");
        }
    }

    public void deleteVendor() {
        if (authService.isAuthenticated() && authService.hasPermission(Action.D, Entity.VENDOR)) {
            System.out.print("Enter Vendor ID to delete: ");
            Long id = Long.parseLong(scanner.nextLine());
            vendorService.deleteVendor(id);
            log.info("Vendor with id {} deleted successfully!",id);
        } else {
            throw new SecurityException("Not authorized to delete vendor");
        }
    }
}
