package com.example.orderservice.service;

import com.example.orderservice.entity.Comment;
import com.example.orderservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> getAllCommentByTicketId(Long ticketId){
        log.info("Finding all comments with ticket_id {}",ticketId);

        return commentRepository.findAllByTicketId(ticketId);
    }
}
