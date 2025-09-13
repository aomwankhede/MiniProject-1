package tech.zeta.application.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public abstract class Party {
    protected Long id;
    protected String name;
    protected String bankAccount;
    protected String contactEmail;
}
