package com.example.orderservice.dto;

import com.example.orderservice.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private String name;

    private String description;

    private LocalDate desiredResolutionDate;

    private String urgencyId;

    private String category_id;

}
