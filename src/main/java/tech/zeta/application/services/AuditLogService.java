package tech.zeta.application.services;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.application.repositories.RoleRepository;
import tech.zeta.application.repositories.UserRepository;

@Slf4j
public class AuditLogService {
    private static AuditLogService instance = null;
    private AuditLogService(){
    }
    public static AuditLogService getInstance(){
        if(instance == null){
            instance =  new AuditLogService();
        }
        return instance;
    }
}
