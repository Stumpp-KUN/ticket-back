package com.example.orderservice.service.impl;

import com.example.orderservice.dto.CommentDTO;
import com.example.orderservice.entity.Comment;
import com.example.orderservice.mapper.CommentMapper;
import com.example.orderservice.repository.CommentRepository;
import com.example.orderservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentDTO> getAllCommentByTicketId(Long ticketId){
        log.info("Finding all comments with ticket_id {}",ticketId);

        List<Comment> comments = commentRepository.findAllByTicketId(ticketId);
        return comments.stream()
                .map(commentMapper::fromEntity)
                .collect(Collectors.toList());
    }
}
