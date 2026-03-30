package com.example._026javag03.dto;

import com.example._026javag03.util.notificatie.NotificatieStatus;

import java.time.LocalDateTime;

public record NotificatieDTO(
        String code,
        LocalDateTime datum,
        String type,
        String inhoud,
        NotificatieStatus status) {
}
