package tech.zeta.application.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee extends Party{
    String department;
    String panNumber;
}
