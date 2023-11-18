package com.example.orderservice.dto;

import com.example.orderservice.entity.Action;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDTO {

    private Long id;

    private Ticket ticket;

    private LocalDate date;

    private Action action;

    private User user;

    private String description;
}
