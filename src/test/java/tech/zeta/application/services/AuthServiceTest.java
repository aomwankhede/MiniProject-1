package tech.zeta.application.services;

import org.junit.jupiter.api.Test;
import tech.zeta.application.enums.Action;
import tech.zeta.application.enums.Entity;
import tech.zeta.application.models.User;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private final AuthService authService = AuthService.getInstance();

    @Test
    void testLogin() {
        boolean result = authService.login("aom", "{hashed}aom");
        assertTrue(result, "Login should succeed for correct credentials");

        boolean badLogin = authService.login("aom", "wrongpass");
        assertFalse(badLogin, "Login should fail for wrong password");
    }

    @Test
    void testLogout() {
        authService.login("aom", "{hashed}aom");
        assertTrue(authService.isAuthenticated());

        authService.logout();
        assertFalse(authService.isAuthenticated());
    }

    @Test
    void testIsAuthenticated() {
        authService.logout();
        assertFalse(authService.isAuthenticated());

        authService.login("aom", "{hashed}aom");
        assertTrue(authService.isAuthenticated());
    }

    @Test
    void testHasPermission() {
        authService.login("aom", "{hashed}aom");

        boolean canWriteUser = authService.hasPermission(Action.C, Entity.USER);
        boolean canReadUser = authService.hasPermission(Action.R, Entity.USER);

        assertTrue(canReadUser || canWriteUser,
                "Admin user should have at least read or write permission on USER");
    }

    @Test
    void testGetCurrentUser() {
        authService.login("aom", "{hashed}aom");
        User current = authService.getCurrentUser();

        assertNotNull(current, "Current user should not be null after login");
        assertEquals("aom", current.getUsername());
    }
}
