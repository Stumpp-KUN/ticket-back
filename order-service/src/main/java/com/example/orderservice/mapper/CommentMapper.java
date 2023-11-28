package com.example.orderservice.mapper;

import com.example.orderservice.dto.CommentDTO;
import com.example.orderservice.entity.Comment;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.entity.User;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.service.TicketService;
import com.example.orderservice.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class CommentMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Mappings({
            @Mapping(target = "userId", source = "comment.user.id"),
            @Mapping(target = "ticketId", source = "comment.ticket.id")
    })
    public abstract CommentDTO fromEntity(Comment comment);

    @Mappings({
            @Mapping(target = "user", source = "commentDTO.userId"),
            @Mapping(target = "ticket", source = "commentDTO.ticketId")
    })
    public abstract Comment toEntity(CommentDTO commentDTO);

    protected Long mapUserToUserId(User user) {
        return user.getId();
    }

    protected User mapUserIdToUser(Long userId) throws EntityNotFoundException {
        return userMapper.toEntity(userService.getById(userId));
    }

    protected Long mapTicketToTicketId(Ticket ticket) {
        return ticket.getId();
    }

    protected Ticket mapTicketIdToTicket(Long ticketId) throws EntityNotFoundException {
        return ticketMapper.toEntity(ticketService.getById(ticketId));
    }
}
