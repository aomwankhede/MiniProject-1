package tech.zeta.application.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VendorPayment extends Payment{
    private Long vendorId;
}
