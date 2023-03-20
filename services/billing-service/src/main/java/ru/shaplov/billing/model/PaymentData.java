package ru.shaplov.billing.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentData {

    @NotNull
    private UUID id;
    @Min(1)
    private BigDecimal amount;
    @Size(min = 4, max = 4)
    private String prefix;
    @Size(min = 4, max = 4)
    private String suffix;
}
