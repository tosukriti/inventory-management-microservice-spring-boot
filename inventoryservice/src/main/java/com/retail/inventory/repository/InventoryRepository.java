
package com.retail.inventory.repository;


import com.retail.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByProductIdAndCategoryAndLocation(String productId, String category, String location);

    Inventory findByProductIdAndLocation(String productId, String location);
}
