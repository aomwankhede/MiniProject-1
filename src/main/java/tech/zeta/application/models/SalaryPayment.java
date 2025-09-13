package tech.zeta.application.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SalaryPayment extends Payment{
    private Long employeeId;
}
