package com.example.orderservice.dto;

import com.example.orderservice.entity.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TicketDTO {
    private Long id;

    @Size(max = 100, message = "Max 100 size for name")
    @Pattern(regexp = "^[a-zA-Z0-9~.\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|}]+$", message = "Invalid characters")
    private String name;

    @Size(max = 100, message = "Max 100 size for description")
    @Pattern(regexp = "^[a-zA-Z0-9~.\"(),:;<>@\\[\\]!#$%&'*+-/=?^_`{|}]+$", message = "Invalid characters")
    private String description;

    private LocalDate createdOn;

    private LocalDate desiredResolutionDate;

    private State stateId;

    private User assigneeId;

    private User ownerId;

    private Urgency urgencyId;

    private Category category_id;

    private User approver;

}
