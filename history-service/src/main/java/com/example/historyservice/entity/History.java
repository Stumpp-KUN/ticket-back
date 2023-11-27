package com.example.historyservice.entity;

import com.example.historyservice.enums.Action;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ticketId;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Action action;

    private Long userId;

    private String description;

}
