package de.mschwan.finance_tracker_dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        BigDecimal amount,
        LocalDate date,
        String description,
        Long categoryId
) {}