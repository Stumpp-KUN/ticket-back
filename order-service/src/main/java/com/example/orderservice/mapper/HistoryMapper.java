package com.example.orderservice.mapper;

import com.example.orderservice.dto.HistoryDTO;
import com.example.orderservice.entity.History;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HistoryMapper {
    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);

    History toHistory(HistoryDTO historyDTO);

    @InheritInverseConfiguration
    HistoryDTO fromHistory(History history);

}
