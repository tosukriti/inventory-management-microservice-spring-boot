package com.retail.inventory;

import com.retail.inventory.model.Inventory;
import com.retail.inventory.repository.InventoryRepository;
import com.retail.inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInventoryLevels() {
        String productId = "123";
        String category = "Electronics";
        String location = "Warehouse1";
        Inventory mockInventory = new Inventory(productId, category, location, 100);
        List<Inventory> expectedInventoryList = Arrays.asList(mockInventory);

        when(inventoryRepository.findByProductIdAndCategoryAndLocation(productId, category, location)).thenReturn(expectedInventoryList);

        List<Inventory> result = inventoryService.getInventoryLevels(productId, category, location);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockInventory, result.get(0));
        verify(inventoryRepository).findByProductIdAndCategoryAndLocation(productId, category, location);
    }

    @Test
    void testUpdateInventory() {
        String productId = "123";
        int quantityChange = 50;
        String location = "Warehouse1";
        Inventory inventory = new Inventory(productId, "Electronics", location, 100);

        when(inventoryRepository.findByProductIdAndLocation(productId, location)).thenReturn(inventory);

        inventoryService.updateInventory(productId, quantityChange, location);

        assertEquals(150, inventory.getQuantity());
        verify(inventoryRepository).findByProductIdAndLocation(productId, location);
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void testUpdateInventory_NotFound() {
        String productId = "123";
        int quantityChange = 50;
        String location = "Warehouse1";

        when(inventoryRepository.findByProductIdAndLocation(productId, location)).thenReturn(null);

        inventoryService.updateInventory(productId, quantityChange, location);

        verify(inventoryRepository).findByProductIdAndLocation(productId, location);
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }
}
