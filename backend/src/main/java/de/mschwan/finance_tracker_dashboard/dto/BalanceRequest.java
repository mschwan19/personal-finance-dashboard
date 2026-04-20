package de.mschwan.finance_tracker_dashboard.dto;
import java.math.BigDecimal;

public record BalanceRequest(BigDecimal initialBalance) {}