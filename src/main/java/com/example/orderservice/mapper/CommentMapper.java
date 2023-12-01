package com.example.orderservice.mapper;

import com.example.orderservice.dto.CommentDTO;
import com.example.orderservice.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TicketMapper.class})
@Component
public interface CommentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "ticketId", source = "ticket.id")
    CommentDTO fromEntity(Comment comment);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "ticket.id", source = "ticketId")
    Comment toEntity(CommentDTO commentDTO);

}
