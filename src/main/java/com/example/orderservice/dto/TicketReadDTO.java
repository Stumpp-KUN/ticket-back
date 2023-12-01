package com.example.orderservice.dto;

import com.example.orderservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketReadDTO {

    private Long id;

    private String name;

    private String description;

    private LocalDate desiredResolutionDate;

    private String stateId;

    private User assigneer;

    private User ownerId;

    private String urgencyId;

    private String categoryId;

    private User approver;
}
