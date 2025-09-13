package tech.zeta.application.models;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Role {
    private Long id;
    private String name;
    private String description;
    private List<Permission> permissions;
}
