import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;


public class LFUCache<K, V> {
    private final int capacity;
    private int minFrequency;
    private final Map<K, V> keyToValue;
    private final Map<K, Integer> keyToFrequency;
    private final Map<Integer, LinkedHashSet<K>> frequencyToKeys;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFrequency = 0;
        this.keyToValue = new HashMap<>();
        this.keyToFrequency = new HashMap<>();
        this.frequencyToKeys = new HashMap<>();
    }

    public V get(K key) {
        if (!keyToValue.containsKey(key)) {
            return null;
        }
        
        // Update frequency for this key
        updateFrequency(key);
        
        return keyToValue.get(key);
    }
    
    private void updateFrequency(K key) {
        int oldFrequency = keyToFrequency.get(key);
        int newFrequency = oldFrequency + 1;
        keyToFrequency.put(key, newFrequency);
        
        frequencyToKeys.get(oldFrequency).remove(key);
        frequencyToKeys.computeIfAbsent(newFrequency, k -> new LinkedHashSet<>()).add(key);
        
        if (frequencyToKeys.get(oldFrequency).isEmpty() && oldFrequency == minFrequency) {
            minFrequency++;
        }
    }

    public void put(K key, V value) {
        if (capacity == 0) {
            return;
        }
        
        if (keyToValue.containsKey(key)) {
            keyToValue.put(key, value);
            updateFrequency(key);
            return;
        }
        
        if (keyToValue.size() >= capacity) {
            evictLeastFrequentlyUsed();
        }
        
        keyToValue.put(key, value);
        keyToFrequency.put(key, 1);
        frequencyToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFrequency = 1;
    }
  
    private void evictLeastFrequentlyUsed() {
        LinkedHashSet<K> keySet = frequencyToKeys.get(minFrequency);
        if (keySet.isEmpty()) {
            return;
        }
        
        K keyToRemove = keySet.iterator().next();
        keySet.remove(keyToRemove);
        keyToValue.remove(keyToRemove);
        keyToFrequency.remove(keyToRemove);
    }

    public int size() {
        return keyToValue.size();
    }

    public boolean isEmpty() {
        return keyToValue.isEmpty();
    }

    public void clear() {
        keyToValue.clear();
        keyToFrequency.clear();
        frequencyToKeys.clear();
        minFrequency = 0;
    }
    
    @Override
    public String toString() {
        return keyToValue.toString();
    }

  
    public static void main(String[] args) {
        // Create an LFU cache with capacity 2
        LFUCache<Integer, String> cache = new LFUCache<>(2);
        
        // Add some entries
        cache.put(1, "One");
        cache.put(2, "Two");
        System.out.println("Cache after adding 1 and 2: " + cache);
        
        // Access an entry to increase its frequency
        System.out.println("Get key 1: " + cache.get(1));
        
        // Add a new entry which will cause an eviction
        // Key 2 should be evicted as it has lower frequency
        cache.put(3, "Three");
        System.out.println("Cache after adding 3: " + cache);
        
        // Try to access the evicted entry
        System.out.println("Get key 2 (should be null): " + cache.get(2));
    }
}
