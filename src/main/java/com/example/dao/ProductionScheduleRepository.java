package com.example.dao;

import com.example.model.ProductionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime; // Import LocalDateTime
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ProductionSchedule entities.
 * Extends JpaRepository to provide standard CRUD operations
 * and Spring Data JPA's query method capabilities.
 */
@Repository // Indicates that this is a Spring Data JPA repository
public interface ProductionScheduleRepository extends JpaRepository<ProductionSchedule, Integer> {
    // Note: The second generic parameter (Integer) must match the type of your entity's primary key (ProductionSchedule's scheduleId)

    /**
     * Finds a ProductionSchedule by its unique schedule number.
     * @param scheduleNumber The unique number of the production schedule.
     * @return An Optional containing the ProductionSchedule if found, or empty otherwise.
     */
    Optional<ProductionSchedule> findByScheduleNumber(String scheduleNumber);

    /**
     * Finds all ProductionSchedules with a specific status.
     * @param status The status of the production schedules (e.g., "Planned", "In Progress", "Completed").
     * @return A list of ProductionSchedules matching the given status.
     */
    List<ProductionSchedule> findByStatus(String status);

    /**
     * Finds all ProductionSchedules associated with a specific work order ID.
     * @param workOrderId The ID of the work order.
     * @return A list of ProductionSchedules for the given work order ID.
     */
    List<ProductionSchedule> findByWorkOrderId(Integer workOrderId);

    /**
     * Finds all ProductionSchedules associated with a specific product ID.
     * @param productId The ID of the product.
     * @return A list of ProductionSchedules for the given product ID.
     */
    List<ProductionSchedule> findByProductId(Integer productId);

    /**
     * Finds all ProductionSchedules scheduled for a specific date/time.
     * @param scheduledDate The exact date and time the production is scheduled for.
     * @return A list of ProductionSchedules for the given scheduled date.
     */
    List<ProductionSchedule> findByScheduledDate(LocalDateTime scheduledDate); // ADDED THIS METHOD
}
