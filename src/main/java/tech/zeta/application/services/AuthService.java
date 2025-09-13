package tech.zeta.application.services;

import lombok.Getter;
import tech.zeta.application.enums.Action;
import tech.zeta.application.enums.Entity;
import tech.zeta.application.models.Permission;
import tech.zeta.application.models.Role;
import tech.zeta.application.models.User;
import tech.zeta.application.repositories.RoleRepository;
import tech.zeta.application.repositories.UserRepository;
import java.util.List;


public class AuthService {
    @Getter private User currentUser = null;
    private UserRepository userRepository = null;
    private RoleRepository roleRepository = null;
    private static AuthService instance = null;
    private AuthService(){
        userRepository = new UserRepository();
        roleRepository = new RoleRepository();
    }
    public static AuthService getInstance(){
        if(instance == null){
            instance =  new AuthService();
        }
        return instance;
    }
    public boolean login(String username, String password) {
        // fetch user from DB
        List<User> users = userRepository.findAll();

        // compare password hash
        List<User> filteredUser = users.stream().filter(user-> user.getUsername().equals(username) && user.getPassword().equals(password)).toList();

        // if ok -> currentUser = user
        if(filteredUser.isEmpty()){
            return false;
        } else if (filteredUser.size() > 1) {
            return false;
        }
        this.currentUser = filteredUser.getFirst();
        return true;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isAuthenticated() {
        return this.currentUser != null;
    }

    public boolean hasPermission(Action action, Entity target) {
        if (currentUser == null) return false;

        Long roleId = currentUser.getRoleId();

        if(roleRepository.findById(roleId).isEmpty()){
            return false;
        }

        Role role = roleRepository.findById(roleId).get();

        for(Permission permission:role.getPermissions()){
            if(permission.getTarget().equals(target)){
                if(permission.getAction().name().contains(action.name())){
                    return true;
                }
            }
        }
        return false;
    }
}
