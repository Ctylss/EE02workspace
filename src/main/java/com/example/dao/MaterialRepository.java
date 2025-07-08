package com.example.dao;

import com.example.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Material entities.
 * Extends JpaRepository to provide standard CRUD operations
 * and Spring Data JPA's query method capabilities for Material data.
 *
 * <p>The first generic parameter (Material) specifies the entity type,
 * and the second (Integer) specifies the type of the entity's primary key (materialId).</p>
 */
@Repository // Marks this interface as a Spring Data JPA repository component
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    /**
     * Finds a Material by its unique material code.
     * Spring Data JPA will automatically generate the query for this method.
     *
     * @param materialCode The unique code of the material.
     * @return An {@link Optional} containing the {@link Material} if found, or empty otherwise.
     */
    Optional<Material> findByMaterialCode(String materialCode);

    /**
     * Finds a list of Materials by their name. This allows for cases where
     * multiple materials might share the same name, or for partial name searches
     * depending on configuration (e.g., using 'Containing' in the method name).
     *
     * @param materialName The name of the material.
     * @return A {@link List} of {@link Material} objects matching the given name.
     */
    List<Material> findByMaterialName(String materialName);

    /**
     * Finds all Materials based on their active status.
     *
     * @param isActive A boolean indicating whether the material is active (true) or inactive (false).
     * @return A {@link List} of {@link Material} objects matching the active status.
     */
    List<Material> findByIsActive(boolean isActive);

    // JpaRepository already provides:
    // - save(Material material): For creating and updating materials.
    // - findById(Integer materialId): For getting a material by ID. Returns Optional<Material>.
    // - findAll(): For getting all materials.
    // - deleteById(Integer materialId): For deleting a material by ID.
    // - existsById(Integer materialId): To check if a material exists by ID.
}