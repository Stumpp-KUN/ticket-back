package com.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(columnDefinition = "ticket_id")
    private Ticket ticket;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Action action;

    @ManyToOne
    @JoinColumn(columnDefinition = "user_id")
    private User user;

    private String description;

    public History(Ticket ticket, LocalDate date, Action action, User user, String description) {
        this.ticket = ticket;
        this.date = date;
        this.action = action;
        this.user = user;
        this.description = description;
    }
}
