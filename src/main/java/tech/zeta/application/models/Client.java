package tech.zeta.application.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Client extends Party{
    private String company;
    private String contractId;
}
