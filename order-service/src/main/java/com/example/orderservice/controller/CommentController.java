package com.example.orderservice.controller;

import com.example.orderservice.dto.CommentDTO;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/v1/ticket/comment")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/collect/{ticketId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByTicketId(@PathVariable Long ticketId) throws EntityNotFoundException {
        return ResponseEntity.ok(commentService.getAllCommentByTicketId(ticketId));
    }
}
