package com.example.orderservice.repository;

import com.example.orderservice.entity.Ticket;
import com.example.orderservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketsRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByAssigneer(User user);

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.assigneer.role = 'EMPLOYEE' " +
            "AND t.stateId = 'NEW'")
    List<Ticket> findAllAssigneeEmployeeAndStateNew();

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.approver.email = :approverEmail " +
            "AND t.stateId IN ('APPROVED', 'DECLINED', 'CANCELLED', 'IN_PROGRESS', 'DONE')")
    List<Ticket> findTicketsByApproverAndStates(@Param("approverEmail") String approverEmail);

    @Query("SELECT t FROM Ticket t " +
            "WHERE (t.ownerId.role = 'EMPLOYEE' OR t.ownerId.role = 'MANAGER') " +
            "AND t.stateId = 'APPROVED'")
    List<Ticket> findTicketsCreatedByEmployeesAndManagersInApprovedState();

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.assigneer.email = :assigneeEmail " +
            "AND t.stateId IN ('IN_PROGRESS', 'DONE')")
    List<Ticket> findTicketsByAssigneeInInProgressAndDoneStates(@Param("assigneeEmail") String assigneeEmail);
}
