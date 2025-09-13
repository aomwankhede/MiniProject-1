package tech.zeta.application.models;

import lombok.*;
import tech.zeta.application.enums.Action;
import tech.zeta.application.enums.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Permission {
    private Long id;
    private Entity target;
    private Action action;  //subset of CRUD field
}
