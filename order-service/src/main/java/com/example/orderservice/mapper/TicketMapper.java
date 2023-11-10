package com.example.orderservice.mapper;

import com.example.orderservice.dto.TicketDTO;
import com.example.orderservice.entity.Ticket;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    Ticket toTicket(TicketDTO ticketDTO);

    @InheritInverseConfiguration
    TicketDTO fromTicket(Ticket ticket);


}
