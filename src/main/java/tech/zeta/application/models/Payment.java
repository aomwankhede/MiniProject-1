package tech.zeta.application.models;

import lombok.*;
import tech.zeta.application.enums.PaymentDirection;
import tech.zeta.application.enums.PaymentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class Payment {
    protected Long id;
    protected Double amount;
    protected PaymentDirection direction; //enum
    protected PaymentStatus status;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
