package com.retail.inventory.controller;

import com.retail.inventory.model.Inventory;
import com.retail.inventory.service.InventoryService;
import com.retail.inventory.service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/getInventoryLevels")
    public CompletableFuture<ResponseEntity<List<Inventory>>> getInventoryLevels(@RequestParam String productId,
                                                                                 @RequestParam String category,
                                                                                 @RequestParam String location) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Received request to get inventory levels for Product ID: {}, Category: {}, Location: {}", productId, category, location);
            List<Inventory> inventoryLevels = inventoryService.getInventoryLevels(productId, category, location);
            logger.debug("Inventory levels retrieved: {}", inventoryLevels);
            return ResponseEntity.ok(inventoryLevels);
        });
    }

    @PostMapping("/updateInventory")
    public CompletableFuture<ResponseEntity<String>> updateInventory(@RequestParam String productId,
                                                                     @RequestParam int quantityChange,
                                                                     @RequestParam String location) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Received request to update inventory for Product ID: {}, Location: {}, Change: {}", productId, location, quantityChange);
            inventoryService.updateInventory(productId, quantityChange, location);
            logger.debug("Inventory updated successfully for Product ID: {}, Location: {}", productId, location);
            return ResponseEntity.ok("Inventory updated successfully.");
        });
    }

    @PostMapping("/coordinateWithSupplier")
    public CompletableFuture<ResponseEntity<String>> coordinateWithSupplier(@RequestParam String productId,
                                                                            @RequestParam int requiredQuantity,
                                                                            @RequestParam String urgency) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Received request to coordinate with supplier for Product ID: {}, Required Quantity: {}, Urgency: {}", productId, requiredQuantity, urgency);
            supplierService.coordinateWithSupplier(productId, requiredQuantity, urgency);
            logger.debug("Supplier coordination initiated for Product ID: {}, Urgency: {}", productId, urgency);
            return ResponseEntity.ok("Request sent to supplier successfully.");
        });
    }
}
