package com.example.orderservice.event;

import com.example.orderservice.event.dto.ActionDTO;
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

    private ActionDTO action;

    private Long userId;

    private String description;

}
