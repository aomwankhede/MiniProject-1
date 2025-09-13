package tech.zeta.application.services;

import tech.zeta.application.models.User;
import tech.zeta.application.repositories.UserRepository;
import tech.zeta.application.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static UserService instance = null;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private UserService() {
        this.userRepository = new UserRepository();
        this.roleRepository = new RoleRepository();
    }

    public static UserService getInstance(){
        if(instance == null){
            instance =  new UserService();
        }
        return instance;
    }

    // Create new user with validations
    public User createUser(User user) {
        // 1. Validate username uniqueness
        Optional<User> existingUser = findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }

        // 2. Validate email
        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + user.getEmail());
        }

        // 3. Hash password (stubbed for now)
        user.setPassword(hashPassword(user.getPassword()));

        // 4. Save
        userRepository.save(user);
        return user;
    }

    // Fetch user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Fetch all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user (with validations)
    public void updateUser(Long id, User user) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.update(id, user);
    }

    // Delete user
    public void deleteUser(Long id) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.delete(id);
    }

    // Assign role to user
    public void assignRole(Long userId, Long roleId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        if (roleRepository.findById(roleId).isEmpty()) {
            throw new IllegalArgumentException("Role not found with ID: " + roleId);
        }

        User user = userRepository.findById(userId).get();
        user.setRoleId(roleId);
        userRepository.update(userId, user);
    }

    // --- Helper: find user by username (not in repository yet) ---
    public Optional<User> findByUsername(String username) {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    // --- Helper: hash password (stubbed) ---
    private String hashPassword(String rawPassword) {
        // TODO: replace with BCrypt
        return "{hashed}" + rawPassword;
    }
}
