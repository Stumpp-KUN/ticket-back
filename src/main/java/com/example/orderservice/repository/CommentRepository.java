package com.example.orderservice.repository;

import com.example.orderservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTicketId(Long ticketId);
}
