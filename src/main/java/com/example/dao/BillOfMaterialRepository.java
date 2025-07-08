package com.example.dao;

import com.example.model.BillOfMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for BillOfMaterial entities.
 * Extends JpaRepository to provide standard CRUD operations
 * and Spring Data JPA's query method capabilities for BillOfMaterial data.
 *
 * <p>The first generic parameter (BillOfMaterial) specifies the entity type,
 * and the second (Integer) specifies the type of the entity's primary key (bomId).</p>
 */
@Repository // Marks this interface as a Spring Data JPA repository component
public interface BillOfMaterialRepository extends JpaRepository<BillOfMaterial, Integer> {

    /**
     * Finds all BillOfMaterial items for a given Product ID.
     * Since BillOfMaterial now has a @ManyToOne relationship with Product,
     * the query method directly uses the 'product' field's 'productId' property.
     *
     * @param productId The ID of the product.
     * @return A List of BillOfMaterial items associated with the given product.
     */
    // CORRECTED: Changed to findByProduct_ProductId to navigate through the 'product' relationship
    List<BillOfMaterial> findByProduct_ProductId(Integer productId);

    /**
     * Checks if any BillOfMaterial item references a given Material ID.
     * This is crucial for preventing deletion of materials that are still in use.
     * Uses the 'material' field's 'materialId' property.
     *
     * @param materialId The ID of the material.
     * @return true if any BOM references the material, false otherwise.
     */
    // CORRECTED: Changed to existsByMaterial_MaterialId to navigate through the 'material' relationship
    boolean existsByMaterial_MaterialId(Integer materialId);

    /**
     * Finds all BillOfMaterial items that use a specific Material ID.
     *
     * @param materialId The ID of the material.
     * @return A List of BillOfMaterial items that include the given material.
     */
    // CORRECTED: Changed to findByMaterial_MaterialId to navigate through the 'material' relationship
    List<BillOfMaterial> findByMaterial_MaterialId(Integer materialId);

    /**
     * Deletes all BillOfMaterial items associated with a specific Product ID.
     * This method will be executed in a transactional context.
     *
     * @param productId The ID of the product whose BOM items are to be deleted.
     */
    // CORRECTED: Changed to deleteByProduct_ProductId to navigate through the 'product' relationship
    void deleteByProduct_ProductId(Integer productId);

    // JpaRepository already provides:
    // - save(BillOfMaterial bom): For creating and updating BOM items.
    // - findById(Integer bomId): For getting a BOM item by ID. Returns Optional<BillOfMaterial>.
    // - findAll(): For getting all BOM items.
    // - deleteById(Integer bomId): For deleting a BOM item by ID.
    // - existsById(Integer bomId): To check if a BOM item exists by ID.
}
