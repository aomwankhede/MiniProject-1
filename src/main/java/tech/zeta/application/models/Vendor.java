package tech.zeta.application.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Vendor extends Party{
    String gstNumber;
    String invoiceTerms;
}
