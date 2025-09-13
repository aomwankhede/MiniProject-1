package tech.zeta.application.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.zeta.application.enums.Entity;
import tech.zeta.application.enums.UserAction;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuditLog {
    private Long id;
    private Entity entity;
    private UserAction action;
    private Long actorId;//referring to user table
    private String details;
    private LocalDateTime timestamp;
}
