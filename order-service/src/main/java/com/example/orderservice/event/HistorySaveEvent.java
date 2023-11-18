package com.example.orderservice.event;

import com.example.orderservice.entity.Action;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorySaveEvent {
    private Long id;

    private Ticket ticket;

    private LocalDate date;

    private Action action;

    private User user;

    private String description;

    public HistorySaveEvent(Ticket ticket, LocalDate date, Action action, User user, String description) {
        this.ticket = ticket;
        this.date = date;
        this.action = action;
        this.user = user;
        this.description = description;
    }
}
