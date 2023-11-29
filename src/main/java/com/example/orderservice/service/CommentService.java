package com.example.orderservice.service;

import com.example.orderservice.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllCommentByTicketId(Long ticketId);
}
