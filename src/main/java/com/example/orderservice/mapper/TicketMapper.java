package com.example.orderservice.mapper;

import com.example.orderservice.dto.TicketCreateDTO;
import com.example.orderservice.dto.TicketReadDTO;
import com.example.orderservice.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
@Component
public interface TicketMapper {

    @Mapping(target = "category_id.id", source = "categoryId")
    Ticket toEntity(TicketReadDTO ticketReadDTO);

    @Mapping(target = "categoryId", source = "category_id.id")
    TicketReadDTO fromEntity(Ticket ticket);

    @Mapping(target = "category_id.id", source = "categoryId")
    Ticket toEntityFromCreate(TicketCreateDTO ticketCreateDTO);

    @Mapping(target = "categoryId", source = "category_id.id")
    TicketCreateDTO fromEntityToCreate(Ticket ticket);

}
