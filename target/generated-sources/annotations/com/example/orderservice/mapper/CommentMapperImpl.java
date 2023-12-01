package com.example.orderservice.mapper;

import com.example.orderservice.dto.CommentDTO;
import com.example.orderservice.dto.CommentDTO.CommentDTOBuilder;
import com.example.orderservice.entity.Comment;
import com.example.orderservice.entity.Comment.CommentBuilder;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.entity.User;
import com.example.orderservice.exception.EntityNotFoundException;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-29T19:09:08+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl extends CommentMapper {

    @Override
    public CommentDTO fromEntity(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDTOBuilder commentDTO = CommentDTO.builder();

        commentDTO.userId( commentUserId( comment ) );
        commentDTO.ticketId( commentTicketId( comment ) );
        commentDTO.id( comment.getId() );
        commentDTO.text( comment.getText() );
        commentDTO.date( comment.getDate() );

        return commentDTO.build();
    }

    @Override
    public Comment toEntity(CommentDTO commentDTO) {
        if ( commentDTO == null ) {
            return null;
        }

        CommentBuilder comment = Comment.builder();

        try {
            comment.user( mapUserIdToUser( commentDTO.getUserId() ) );
        }
        catch ( EntityNotFoundException e ) {
            throw new RuntimeException( e );
        }
        try {
            comment.ticket( mapTicketIdToTicket( commentDTO.getTicketId() ) );
        }
        catch ( EntityNotFoundException e ) {
            throw new RuntimeException( e );
        }
        comment.id( commentDTO.getId() );
        comment.text( commentDTO.getText() );
        comment.date( commentDTO.getDate() );

        return comment.build();
    }

    private Long commentUserId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        User user = comment.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long commentTicketId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Ticket ticket = comment.getTicket();
        if ( ticket == null ) {
            return null;
        }
        Long id = ticket.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
