package ru.doublebyte.amznsm.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Stores item links
 */
public class ItemStorage {

    private static final Logger logger = LoggerFactory.getLogger(ItemStorage.class);

    private static final String PROPERTIES_FILE = "items.properties";

    private Map<String, String> items = new HashMap<>();

    ///////////////////////////////////////////////////////////////////////////

    public ItemStorage() {
        loadItems();
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get all items in a read only map
     * @return Items
     */
    public Map<String, String> getItems() {
        return Collections.unmodifiableMap(items);
    }

    /**
     * Add new item
     * @param link Item's link
     * @return Item's key
     */
    public String addItem(String link) {
        String key = UUID.randomUUID().toString();

        logger.info("Creating item: {}={}", key, link);

        items.put(key, link);
        saveItems();

        return key;
    }

    /**
     * Delete item by it's key
     * @param key Item's key
     */
    public void deleteItem(String key) {
        if (!items.containsKey(key)) {
            return;
        }

        logger.info("Removing item: {}={}", key, items.get(key));

        items.remove(key);
        saveItems();
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Load items from local properties file
     */
    private synchronized void loadItems() {
        logger.info("Loading items from {}...", PROPERTIES_FILE);

        Path propertiesPath = Paths.get(PROPERTIES_FILE);

        if (!Files.exists(propertiesPath)) {
            logger.warn("Items file not exists");
            return;
        }

        try (
                InputStream is = Files.newInputStream(propertiesPath)
        ) {
            items = new HashMap<>();

            Properties properties = new Properties();
            properties.load(is);
            properties.forEach((key, value) -> items.put((String) key, (String) value));
        } catch (Exception e) {
            logger.error("Unable to read items file", e);
        }
    }

    /**
     * Save items to local properties file
     */
    @Async
    private synchronized void saveItems() {
        logger.info("Saving items to {}...", PROPERTIES_FILE);

        Path propertiesPath = Paths.get(PROPERTIES_FILE);

        try (
            OutputStream os = Files.newOutputStream(propertiesPath)
        ) {
            Properties properties = new Properties();
            properties.putAll(items);
            properties.store(os, "items");
        } catch (Exception e) {
            logger.error("Unable to save items", e);
        }
    }

}
