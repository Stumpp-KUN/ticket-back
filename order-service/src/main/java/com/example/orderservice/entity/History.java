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
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Action action;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String description;

    public History(Ticket ticket, Action action, User user, String description) {
        this.ticket = ticket;
        this.date = LocalDate.now();
        this.action = action;
        this.user = user;
        this.description = description;
    }
}
