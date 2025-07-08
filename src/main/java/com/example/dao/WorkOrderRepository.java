package com.example.dao;

import com.example.model.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for WorkOrder entities.
 * Extends JpaRepository to provide standard CRUD operations
 * and Spring Data JPA's query method capabilities.
 */
@Repository // Indicates that this is a Spring Data JPA repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Integer> {
    // Note: The second generic parameter (Integer) must match the type of your entity's primary key (WorkOrder's workOrderId)

    /**
     * Finds a WorkOrder by its unique work order number.
     * @param workOrderNumber The unique number of the work order.
     * @return An Optional containing the WorkOrder if found, or empty otherwise.
     */
    Optional<WorkOrder> findByWorkOrderNumber(String workOrderNumber);

    /**
     * Finds all WorkOrders with a specific status.
     * @param status The status of the work orders (e.g., "Pending", "In Progress", "Completed").
     * @return A list of WorkOrders matching the given status.
     */
    List<WorkOrder> findByStatus(String status);

    /**
     * Finds all WorkOrders associated with a specific product ID.
     * This method now correctly uses the ManyToOne relationship to query by the Product's ID.
     * @param productId The ID of the product.
     * @return A list of WorkOrders for the given product ID.
     */
    List<WorkOrder> findByProduct_ProductId(Integer productId); // CORRECTED: Changed to navigate through the 'product' relationship
}
