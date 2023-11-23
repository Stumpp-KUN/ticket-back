package com.example.historyservice.mapper;

import com.example.historyservice.entity.History;
import com.example.historyservice.event.HistorySaveEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HistoryMapper {
    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);

    History mapToHistory(HistorySaveEvent historySaveEvent);

    HistorySaveEvent mapToHistorySaveEvent(History history);
}

