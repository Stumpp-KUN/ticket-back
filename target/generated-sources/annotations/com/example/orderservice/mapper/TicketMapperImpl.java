package com.example.orderservice.mapper;

import com.example.orderservice.dto.TicketCreateDTO;
import com.example.orderservice.dto.TicketCreateDTO.TicketCreateDTOBuilder;
import com.example.orderservice.dto.TicketReadDTO;
import com.example.orderservice.dto.TicketReadDTO.TicketReadDTOBuilder;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.entity.Ticket.TicketBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-29T19:02:54+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class TicketMapperImpl extends TicketMapper {

    @Override
    public Ticket toEntity(TicketReadDTO ticketReadDTO) {
        if ( ticketReadDTO == null ) {
            return null;
        }

        TicketBuilder ticket = Ticket.builder();

        ticket.id( ticketReadDTO.getId() );
        ticket.name( ticketReadDTO.getName() );
        ticket.description( ticketReadDTO.getDescription() );
        ticket.desiredResolutionDate( ticketReadDTO.getDesiredResolutionDate() );
        ticket.stateId( mapStringToState( ticketReadDTO.getStateId() ) );
        ticket.assigneer( ticketReadDTO.getAssigneer() );
        ticket.ownerId( ticketReadDTO.getOwnerId() );
        ticket.urgencyId( mapStringToUrgency( ticketReadDTO.getUrgencyId() ) );
        ticket.category_id( mapStringToCategory( ticketReadDTO.getCategory_id() ) );
        ticket.approver( ticketReadDTO.getApprover() );

        return ticket.build();
    }

    @Override
    public TicketReadDTO fromEntity(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }

        TicketReadDTOBuilder ticketReadDTO = TicketReadDTO.builder();

        ticketReadDTO.id( ticket.getId() );
        ticketReadDTO.name( ticket.getName() );
        ticketReadDTO.description( ticket.getDescription() );
        ticketReadDTO.desiredResolutionDate( ticket.getDesiredResolutionDate() );
        ticketReadDTO.stateId( mapStateToString( ticket.getStateId() ) );
        ticketReadDTO.assigneer( ticket.getAssigneer() );
        ticketReadDTO.ownerId( ticket.getOwnerId() );
        ticketReadDTO.urgencyId( mapUrgencyToString( ticket.getUrgencyId() ) );
        ticketReadDTO.category_id( mapCategoryToString( ticket.getCategory_id() ) );
        ticketReadDTO.approver( ticket.getApprover() );

        return ticketReadDTO.build();
    }

    @Override
    public Ticket toEntityFromCreate(TicketCreateDTO ticketCreateDTO) {
        if ( ticketCreateDTO == null ) {
            return null;
        }

        TicketBuilder ticket = Ticket.builder();

        ticket.name( ticketCreateDTO.getName() );
        ticket.description( ticketCreateDTO.getDescription() );
        ticket.desiredResolutionDate( ticketCreateDTO.getDesiredResolutionDate() );
        ticket.urgencyId( mapStringToUrgency( ticketCreateDTO.getUrgencyId() ) );
        ticket.category_id( mapStringToCategory( ticketCreateDTO.getCategory_id() ) );

        return ticket.build();
    }

    @Override
    public TicketCreateDTO fromEntityToCreate(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }

        TicketCreateDTOBuilder ticketCreateDTO = TicketCreateDTO.builder();

        ticketCreateDTO.name( ticket.getName() );
        ticketCreateDTO.description( ticket.getDescription() );
        ticketCreateDTO.desiredResolutionDate( ticket.getDesiredResolutionDate() );
        ticketCreateDTO.urgencyId( mapUrgencyToString( ticket.getUrgencyId() ) );
        ticketCreateDTO.category_id( mapCategoryToString( ticket.getCategory_id() ) );

        return ticketCreateDTO.build();
    }
}
