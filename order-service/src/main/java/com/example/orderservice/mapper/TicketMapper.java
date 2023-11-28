package com.example.orderservice.mapper;

import com.example.orderservice.dto.TicketDTOCreate;
import com.example.orderservice.dto.TicketDTORead;
import com.example.orderservice.entity.Category;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.enums.State;
import com.example.orderservice.enums.Urgency;
import com.example.orderservice.service.impl.CategoryServiceImpl;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class TicketMapper {

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    public abstract Ticket toEntity(TicketDTORead ticketDTORead);

    public abstract TicketDTORead fromEntity(Ticket ticket);

    public abstract Ticket toEntityFromCreate(TicketDTOCreate ticketDTOCreate);

    public abstract TicketDTOCreate fromEntityToCreate(Ticket ticket);


    String mapCategoryToString(Category category){
        if(category == null){
            return null;
        }

        return category.getName();
    }

    Category mapStringToCategory(String category_name) {
        if (category_name == null) {
            return null;
        }

        return categoryServiceImpl.getCategoryByName(category_name);
    }

    State mapStringToState(String state) {
        if (state == null) {
            return null;
        }

        try {
            return State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unexpected enum constant: " + state);
        }
    }

    String mapStateToString(State state) {
        if (state == null) {
            return null;
        }

        return state.name();
    }

    Urgency mapStringToUrgency(String urgency) {
        if (urgency == null) {
            return null;
        }

        try {
            return Urgency.valueOf(urgency.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unexpected enum constant: " + urgency);
        }
    }

    String mapUrgencyToString(Urgency urgency) {
        if (urgency == null) {
            return null;
        }

        return urgency.name();
    }
}
