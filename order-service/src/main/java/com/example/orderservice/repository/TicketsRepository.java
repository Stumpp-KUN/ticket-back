package com.example.orderservice.repository;

import com.example.orderservice.entity.Role;
import com.example.orderservice.entity.State;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketsRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByOwnerId(User user);

    List<Ticket> findAllByStateId(State state);

    @Query("""
            SELECT t FROM Ticket t 
            WHERE t.ownerId=:user 
            AND t.stateId=:state
            """)
    List<Ticket> findAllByOwnerIdAndStateId(User user, State state);

    @Query("""
            SELECT t FROM Ticket t 
            WHERE t.ownerId.role =:role 
            AND t.stateId =:state
            """)
    List<Ticket> findAllOwnerRoleAndState(Role role, State state);

    @Query("""
            SELECT t FROM Ticket t 
            WHERE t.approver.email = :approverEmail 
            AND t.stateId=:state
            """)
    List<Ticket> findTicketsByApproverAndStates(@Param("approverEmail") String approverEmail, State state);

    @Query("""
            SELECT t FROM Ticket t 
            WHERE t.assigneer.email = :assigneeEmail
            AND t.stateId=:state
            """)
    List<Ticket> findTicketsByAssigneeInStates(@Param("assigneeEmail") String assigneeEmail, State state);
}
