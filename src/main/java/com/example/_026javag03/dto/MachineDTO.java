package com.example._026javag03.dto;

import com.example._026javag03.util.machine.ProductieStatusMachine;
import com.example._026javag03.util.machine.StatusMachine;

import java.time.LocalDate;
import java.util.List;

public record MachineDTO(Long id,
                         String code,
                         Long siteId,
                         String locatie,
                         String productinfo,
                         StatusMachine status,
                         ProductieStatusMachine productieStatus,
                         String uptime,
                         LocalDate laatsteOnderhoud,
                         List<Long> werknemerIds) {
}
