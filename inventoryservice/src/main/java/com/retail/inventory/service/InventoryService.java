package com.retail.inventory.service;

import com.retail.inventory.model.Inventory;
import com.retail.inventory.repository.InventoryRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private InventoryRepository inventoryRepository;

    @Cacheable("inventoryLevels")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackGetInventoryLevels")
    public List<Inventory> getInventoryLevels(String productId, String category, String location) {
        logger.info("Fetching inventory levels for Product ID: {}, Category: {}, Location: {}", productId, category, location);
        List<Inventory> inventoryList = inventoryRepository.findByProductIdAndCategoryAndLocation(productId, category, location);
        logger.debug("Retrieved inventory levels: {}", inventoryList);
        return inventoryList;
    }

    public List<Inventory> fallbackGetInventoryLevels(String productId, String category, String location, Throwable t) {
        // fallback method implementation
        return new ArrayList<>();
    }

    @CacheEvict(value = "inventoryLevels", allEntries = true)
    public void updateInventory(String productId, int quantityChange, String location) {
        logger.info("Updating inventory for Product ID: {}, Location: {}, Change: {}", productId, location, quantityChange);
        Inventory inventory = inventoryRepository.findByProductIdAndLocation(productId, location);
        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + quantityChange);
            inventoryRepository.save(inventory);
            logger.debug("Updated inventory: {}", inventory);
        } else {
            logger.warn("Inventory not found for Product ID: {}, Location: {}", productId, location);
        }
    }
}
