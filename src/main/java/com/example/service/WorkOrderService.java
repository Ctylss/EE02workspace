package com.example.service;

import com.example.model.WorkOrder;
import java.time.LocalDateTime; // Correct: Use java.time.LocalDateTime for consistency
// import java.util.Date; // <-- REMOVE THIS IMPORT if not used by any other method in this interface
import java.util.List;
import java.util.Optional;

/**
 * WorkOrderService interface defines business operations related to production work orders.
 */
public interface WorkOrderService {

    /**
     * Creates a new production work order.
     *
     * @param workOrder The work order object to be created.
     * @return The work order object after creation, including the database-generated ID.
     * @throws IllegalArgumentException If the work order data is invalid or the related product ID does not exist.
     */
    WorkOrder createWorkOrder(WorkOrder workOrder) throws IllegalArgumentException;

    /**
     * Retrieves work order information by work order ID.
     *
     * @param workOrderId The unique identifier ID of the work order. (Changed to Integer)
     * @return An Optional containing the matching work order object, or an empty Optional if not found.
     * @throws IllegalArgumentException If the work order ID is invalid.
     */
    Optional<WorkOrder> getWorkOrderById(Integer workOrderId) throws IllegalArgumentException;

    /**
     * Retrieves a list of all production work orders.
     *
     * @return A list containing all WorkOrder objects.
     */
    List<WorkOrder> getAllWorkOrders();

    /**
     * Retrieves a list of work orders based on their status.
     *
     * @param status The work order status to filter by.
     * @return A list of WorkOrder objects matching the specified status.
     * @throws IllegalArgumentException If the status string is invalid.
     */
    List<WorkOrder> getWorkOrdersByStatus(String status) throws IllegalArgumentException;

    /**
     * Retrieves a list of related production work orders based on the product ID.
     *
     * @param productId The ID of the product. (Changed to Integer)
     * @return A list of all WorkOrder objects related to the product.
     * @throws IllegalArgumentException If the product ID is invalid.
     */
    List<WorkOrder> getWorkOrdersByProductId(Integer productId) throws IllegalArgumentException;

    /**
     * Updates production work order information.
     *
     * @param workOrder The work order object containing updated information (must include a valid work order ID).
     * @return An Optional containing the updated work order object. Returns an empty Optional if the work order with the specified ID does not exist.
     * @throws IllegalArgumentException If the work order data is invalid.
     */
    Optional<WorkOrder> updateWorkOrder(WorkOrder workOrder) throws IllegalArgumentException;

    /**
     * Deletes the production work order with the specified ID.
     *
     * @param workOrderId The ID of the work order to delete. (Changed to Integer)
     * @return True if deletion is successful, false otherwise.
     * @throws IllegalArgumentException If the work order ID is invalid.
     */
    boolean deleteWorkOrder(Integer workOrderId) throws IllegalArgumentException;

    /**
     * Sets the actual start date for a work order and changes its status to "In Progress".
     *
     * @param workOrderId The work order ID. (Changed to Integer)
     * @param actualStartDate The actual start date. (Changed to LocalDateTime)
     * @return An Optional containing the updated work order object. Returns an empty Optional if the work order does not exist or its status does not allow starting.
     * @throws IllegalArgumentException If the work order ID or date is invalid.
     * @throws IllegalStateException If the work order status does not allow starting (e.g., already completed or cancelled).
     */
    Optional<WorkOrder> startWorkOrder(Integer workOrderId, LocalDateTime actualStartDate) throws IllegalArgumentException, IllegalStateException;

    /**
     * Sets the actual completion date for a work order and changes its status to "Completed".
     *
     * @param workOrderId The work order ID. (Changed to Integer)
     * @param actualCompletionDate The actual completion date. (Changed to LocalDateTime)
     * @return An Optional containing the updated work order object. Returns an empty Optional if the work order does not exist or its status does not allow completion.
     * @throws IllegalArgumentException If the work order ID or date is invalid.
     * @throws IllegalStateException If the work order status does not allow completion (e.g., not yet started or cancelled).
     */
    Optional<WorkOrder> completeWorkOrder(Integer workOrderId, LocalDateTime actualCompletionDate) throws IllegalArgumentException, IllegalStateException;

    // This method seems redundant, as createWorkOrder already exists and returns the WorkOrder
    // If you intend for it to be different, clarify its purpose. Otherwise, remove it.

    // *** THESE ARE THE DUPLICATE METHODS CAUSING THE ERROR. REMOVE THEM. ***
    // void startWorkOrder(Integer workOrderId, Date actualStartDate);
    // void completeWorkOrder(Integer workOrderId, Date actualCompletionDate);
}
