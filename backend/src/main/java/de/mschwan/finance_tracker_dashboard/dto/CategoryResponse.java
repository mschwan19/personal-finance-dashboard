package de.mschwan.finance_tracker_dashboard.dto;

import de.mschwan.finance_tracker_dashboard.model.CategoryType;

public record CategoryResponse(
        Long id,
        String name,
        CategoryType type,
        String colorHex
) {}