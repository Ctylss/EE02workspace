package com.example.service;

import com.example.model.ProductionSchedule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing ProductionSchedule entities.
 * Defines business operations related to production schedules.
 */
public interface ProductionScheduleService {

    /**
     * Creates a new production schedule.
     * @param productionSchedule The ProductionSchedule object to create.
     * @return The created ProductionSchedule object.
     * @throws IllegalArgumentException if the data is invalid.
     */
    ProductionSchedule createProductionSchedule(ProductionSchedule productionSchedule);

    /**
     * Retrieves a production schedule by its ID.
     * @param scheduleId The ID of the production schedule.
     * @return An Optional containing the ProductionSchedule if found, or empty otherwise.
     */
    Optional<ProductionSchedule> getProductionScheduleById(Integer scheduleId);

    /**
     * Retrieves all production schedules.
     * @return A list of all ProductionSchedule objects.
     */
    List<ProductionSchedule> getAllProductionSchedules();

    /**
     * Retrieves production schedules by their status.
     * @param status The status to filter by.
     * @return A list of ProductionSchedule objects matching the given status.
     */
    List<ProductionSchedule> getProductionSchedulesByStatus(String status);

    /**
     * Retrieves production schedules by their product ID.
     * @param productId The ID of the product.
     * @return A list of ProductionSchedule objects for the given product ID.
     */
    List<ProductionSchedule> getProductionSchedulesByProductId(Integer productId);

    /**
     * Retrieves production schedules by their work order ID.
     * @param workOrderId The ID of the work order.
     * @return A list of ProductionSchedule objects for the given work order ID.
     */
    List<ProductionSchedule> getProductionSchedulesByWorkOrderId(Integer workOrderId);

    /**
     * Updates an existing production schedule.
     * @param productionSchedule The ProductionSchedule object with updated information.
     * @return An Optional containing the updated ProductionSchedule if found, or empty otherwise.
     * @throws IllegalArgumentException if the data is invalid.
     */
    Optional<ProductionSchedule> updateProductionSchedule(ProductionSchedule productionSchedule);

    /**
     * Deletes a production schedule by its ID.
     * @param scheduleId The ID of the production schedule to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    boolean deleteProductionSchedule(Integer scheduleId); // 確保這裡的返回類型是 boolean

    /**
     * Marks a production schedule as started, setting the actual start date and updating status.
     * @param scheduleId The ID of the production schedule to start.
     * @param actualStartDate The actual start date and time.
     * @return An Optional containing the updated ProductionSchedule if successful.
     * @throws IllegalStateException if the schedule is not in a valid state to be started.
     * @throws IllegalArgumentException if input is invalid.
     */
    Optional<ProductionSchedule> startProductionSchedule(Integer scheduleId, LocalDateTime actualStartDate);

    /**
     * Marks a production schedule as completed, setting the actual completion date and updating status.
     * @param scheduleId The ID of the production schedule to complete.
     * @param actualQuantity The actual quantity produced.
     * @param actualCompletionDate The actual completion date and time.
     * @return An Optional containing the updated ProductionSchedule if successful.
     * @throws IllegalStateException if the schedule is not in a valid state to be completed.
     * @throws IllegalArgumentException if input is invalid.
     */
    Optional<ProductionSchedule> completeProductionSchedule(Integer scheduleId, BigDecimal actualQuantity, LocalDateTime actualCompletionDate);

    // 您可能還需要這個方法，如果它在 ProductionScheduleServiceImpl 中被調用
    List<ProductionSchedule> getProductionSchedulesByDate(LocalDateTime scheduledDate);
}
