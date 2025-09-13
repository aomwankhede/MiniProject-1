package tech.zeta.application.services;

import org.junit.jupiter.api.*;
import tech.zeta.application.models.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    private final UserService userService = UserService.getInstance();
    private User testUser;
    private Long originalRoleId;

    @BeforeAll
    void setup() {
        testUser = new User();
        testUser.setUsername("junit_user");
        testUser.setPassword("secret123");
        testUser.setEmail("junit@test.com");
        testUser.setRoleId(5L); // assume base roleId=1 exists

        userService.createUser(testUser);
        assertNotNull(testUser.getId(), "Test user should be created with an ID");
    }

    @AfterAll
    void cleanup() {
        if (testUser != null && testUser.getId() != null) {
            userService.deleteUser(testUser.getId());
        }
    }

    @Test
    @Order(1)
    void testGetInstance() {
        assertNotNull(userService, "UserService instance should not be null");
    }

    @Test
    @Order(2)
    void testCreateUser() {
        assertEquals("junit_user", testUser.getUsername());
        assertEquals("junit@test.com", testUser.getEmail());
    }

    @Test
    @Order(3)
    void testGetUserById() {
        Optional<User> found = userService.getUserById(testUser.getId());
        assertTrue(found.isPresent());
        assertEquals(testUser.getId(), found.get().getId());
    }

    @Test
    @Order(4)
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(testUser.getId())));
    }

    @Test
    @Order(5)
    void testUpdateUser() {
        testUser.setEmail("updated@test.com");
        userService.updateUser(testUser.getId(), testUser);

        Optional<User> updated = userService.getUserById(testUser.getId());
        assertTrue(updated.isPresent());
        assertEquals("updated@test.com", updated.get().getEmail());
    }

    @Test
    @Order(6)
    void testAssignRole() {
        Optional<User> before = userService.getUserById(testUser.getId());
        assertTrue(before.isPresent());
        originalRoleId = before.get().getRoleId();

        userService.assignRole(testUser.getId(), 5L);

        Optional<User> after = userService.getUserById(testUser.getId());
        assertTrue(after.isPresent());
        assertEquals(5L, after.get().getRoleId(), "Role should be updated to 5");

        // revert back to original role so DB remains unchanged
        userService.assignRole(testUser.getId(), originalRoleId);
    }

    @Test
    @Order(7)
    void testFindByUsername() {
        Optional<User> found = userService.findByUsername("junit_user");
        assertTrue(found.isPresent());
        assertEquals(testUser.getId(), found.get().getId());
    }

    @Test
    @Order(8)
    void testDeleteUser() {
        // test deletion only if we recreate the user here
        User tempUser = new User();
        tempUser.setUsername("temp_user");
        tempUser.setPassword("temp123");
        tempUser.setEmail("temp@test.com");
        tempUser.setRoleId(5L);

        userService.createUser(tempUser);
        assertNotNull(tempUser.getId());

        userService.deleteUser(tempUser.getId());

        Optional<User> deleted = userService.getUserById(tempUser.getId());
        assertTrue(deleted.isEmpty(), "Temp user should be deleted");
    }
}
