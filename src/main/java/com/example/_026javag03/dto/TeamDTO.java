package com.example._026javag03.dto;

import java.util.List;


public record TeamDTO(
        Long id,
        Long site,
        String teamCode,
        Long verantwoordelijke,
        List<Long> werknemers

) {
}
