package tech.zeta.application.services;

import org.junit.jupiter.api.*;
import tech.zeta.application.models.Permission;
import tech.zeta.application.models.Role;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoleServiceTest {

    private final RoleService roleService = RoleService.getInstance();
    private Role testRole;
    private List<Permission> originalPermissions;

    @BeforeAll
    void setup() throws SQLException {
        testRole = new Role();
        testRole.setName("JUnitTestRole");
        testRole.setDescription("Role created for JUnit testing");
        testRole.setPermissions(new ArrayList<>()); // start empty

        roleService.createRole(testRole);
        assertNotNull(testRole.getId(), "Test role should be created with an ID");
    }

    @AfterAll
    void cleanup() {
        if (testRole != null && testRole.getId() != null) {
            roleService.deleteRole(testRole.getId());
        }
    }

    @Test
    @Order(1)
    void testGetInstance() {
        assertNotNull(roleService, "RoleService instance should not be null");
    }

    @Test
    @Order(2)
    void testCreateRole() {
        assertEquals("JUnitTestRole", testRole.getName());
        assertEquals("Role created for JUnit testing", testRole.getDescription());
    }

    @Test
    @Order(3)
    void testGetRoleById() {
        Optional<Role> found = roleService.getRoleById(testRole.getId());
        assertTrue(found.isPresent());
        assertEquals(testRole.getId(), found.get().getId());
    }

    @Test
    @Order(4)
    void testGetAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        assertTrue(roles.stream().anyMatch(r -> r.getId().equals(testRole.getId())));
    }

    @Test
    @Order(5)
    void testUpdateRole() throws SQLException {
        String oldDesc = testRole.getDescription();
        testRole.setDescription("Updated JUnit role");
        roleService.updateRole(testRole.getId(), testRole);

        Optional<Role> updated = roleService.getRoleById(testRole.getId());
        assertTrue(updated.isPresent());
        assertEquals("Updated JUnit role", updated.get().getDescription());

        // revert to original so DB remains same
        testRole.setDescription(oldDesc);
        roleService.updateRole(testRole.getId(), testRole);
    }

    @Test
    @Order(6)
    void testFindByName() {
        Optional<Role> found = roleService.findByName("JUnitTestRole");
        assertTrue(found.isPresent());
        assertEquals(testRole.getId(), found.get().getId());
    }

    @Test
    @Order(8)
    void testDeleteRole() throws SQLException {
        Role tempRole = new Role();
        tempRole.setName("TempRole");
        tempRole.setDescription("Temporary role for delete test");
        tempRole.setPermissions(new ArrayList<>());

        roleService.createRole(tempRole);
        assertNotNull(tempRole.getId());

        roleService.deleteRole(tempRole.getId());

        Optional<Role> deleted = roleService.getRoleById(tempRole.getId());
        assertTrue(deleted.isEmpty(), "Temp role should be deleted");
    }
}
