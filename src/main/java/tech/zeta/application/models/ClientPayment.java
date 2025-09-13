package tech.zeta.application.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClientPayment extends Payment{
    private Long clientId;
}
