package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketCreateDTO {
    private String name;

    private String description;

    private LocalDate desiredResolutionDate;

    private String urgencyId;

    private String categoryId;
}
