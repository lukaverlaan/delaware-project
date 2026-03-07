package com.example._026javag03.dto;

import com.example._026javag03.util.site.ProductieStatus;
import com.example._026javag03.util.Status;

public record SiteDTO(
        Long id,
        String naam,
        String locatie,
        int capaciteit,
        Status operationeleStatus,
        ProductieStatus productieStatus
) {}