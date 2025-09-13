package tech.zeta;

import tech.zeta.application.frontend.*;
import tech.zeta.application.services.*;

import java.util.Scanner;

public class PMS {
    AuditLogService auditLogService = AuditLogService.getInstance() ;
    AuthService authService = AuthService.getInstance() ;
    ClientPaymentService clientPaymentService = ClientPaymentService.getInstance() ;
    ClientService clientService = ClientService.getInstance();
    EmployeeService employeeService = EmployeeService.getInstance();
    RoleService roleService = RoleService.getInstance() ;
    SalaryPaymentService salaryPaymentService = SalaryPaymentService.getInstance() ;
    UserService userService = UserService.getInstance();
    VendorPaymentService vendorPaymentService = VendorPaymentService.getInstance() ;
    VendorService vendorService = VendorService.getInstance() ;
    AuthInterface authInterface = new AuthInterface() ;
    PartyEntityInterface partyEntityInterface = new PartyEntityInterface() ;
    PaymentEntityInterface paymentEntityInterface = new PaymentEntityInterface() ;
    PermissionEntityInterface permissionEntityInterface = new PermissionEntityInterface() ;
    UserEntityInterface userEntityInterface = new UserEntityInterface() ;
    ReportGenerationInterface reportGenerationInterface = new ReportGenerationInterface() ;
    RoleEntityInterface roleEntityInterface = new RoleEntityInterface() ;
    Scanner sc = new Scanner(System.in) ;
    public PMS(){
        System.out.println("Initialised PMS...");
    }

    public void start(){
        while(true){
            if(!authService.isAuthenticated()){
                System.out.println("\nPlease register or login to continue");
                authInterface.display();
            }
            else{
                System.out.println("\n=========================================");
                System.out.println("Main Menu - Choose an entity to manage");
                System.out.println("=========================================");
                System.out.println("1. Manage Users (create/update/delete/search)");
                System.out.println("2. Manage Payments (client/vendor/employee payments)");
                System.out.println("3. Manage Parties (clients/vendors/employees)");
                System.out.println("4. Manage Permissions (view or update permissions)");
                System.out.println("5. Generate Reports (audit logs, payment summaries)");
                System.out.println("6. Manage Roles (assign/update role permissions)");
                System.out.println("7. Logout");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();sc.nextLine();
                switch (choice){
                    case 0:
                        authService.logout();
                        System.out.println("Thank you for visiting PMS !!");
                        return;
                    case 1:
                        userEntityInterface.display();
                        break;
                    case 2:
                        paymentEntityInterface.display();
                        break;
                    case 3:
                        partyEntityInterface.display();
                        break;
                    case 4:
                        permissionEntityInterface.display();
                        break;
                    case 5:
                        reportGenerationInterface.display();
                        break;
                    case 6:
                        roleEntityInterface.display();
                        break;
                    case 7:
                        authService.logout();
                        break;
                    default:
                        System.out.println("Pls enter valid number");
                }
            }
        }
    }
}
