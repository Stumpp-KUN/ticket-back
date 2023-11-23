package com.example.historyservice.event;

import com.example.historyservice.event.dto.Action;
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
