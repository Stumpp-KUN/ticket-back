package com.example.orderservice.event;

import com.example.orderservice.enums.Action;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorySaveEvent {

    private Long ticketId;

    private Action action;

    private Long userId;

    private String description;

}
