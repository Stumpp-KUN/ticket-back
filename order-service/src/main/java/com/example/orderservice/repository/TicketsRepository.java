package com.example.orderservice.repository;

import com.example.orderservice.entity.Ticket;
import com.example.orderservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketsRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByOwnerId(User user);

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.ownerId.role='MANAGER' " +
            "AND t.ownerId=:user " +
            "AND t.stateId='DRAFT'")
    List<Ticket> findAllByOwnerIdAndStateId_Draft(User user);

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.ownerId.role='MANAGER' " +
            "AND t.ownerId=:user " +
            "AND t.stateId='DECLINED'")
    List<Ticket> findAllByOwnerIdAndStateId_Declined(User user);

    List<Ticket> findAllByAssigneer(User user);

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.ownerId.role = 'EMPLOYEE' " +
            "AND t.stateId = 'NEW'")
    List<Ticket> findAllOwnerEmployeeAndStateNew();

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.approver.email = :approverEmail " +
            "AND t.stateId IN ('APPROVED', 'DECLINED', 'CANCELLED', 'IN_PROGRESS', 'DONE')")
    List<Ticket> findTicketsByApproverAndStates(@Param("approverEmail") String approverEmail);

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.ownerId = :user " +
            "AND (t.stateId = 'DECLINED' OR t.stateId = 'DRAFT')")
    List<Ticket> findAllByOwnerIdAndStateId_DraftAndStateId_Declined(User user);


    @Query("SELECT t FROM Ticket t " +
            "WHERE t.stateId='APPROVED' " +
            "AND t.stateId = 'IN_PROGRESS' ")
    List<Ticket> findAllApprovedAndInProgressTickets();


    @Query("SELECT t FROM Ticket t " +
            "WHERE (t.ownerId.role = 'EMPLOYEE' OR t.ownerId.role = 'MANAGER') " +
            "AND t.stateId = 'APPROVED'")
    List<Ticket> findTicketsCreatedByEmployeesAndManagersInApprovedState();

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.assigneer.email = :assigneeEmail " +
            "AND t.stateId IN ('IN_PROGRESS', 'DONE')")
    List<Ticket> findTicketsByAssigneeInInProgressAndDoneStates(@Param("assigneeEmail") String assigneeEmail);
}
