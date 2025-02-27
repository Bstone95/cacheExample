import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class Cache2 {
    private int numberOfBlocks;
    private int associativity;
    static int miss = 0;
    static int hit = 0;
    private Map<Integer, Integer>[] cache;
    private Map<Integer, Integer>[] lruOrder; // Keeps track of the LRU order within each set

    public Cache2(int numberOfBlocks, int associativity) {
        this.numberOfBlocks = numberOfBlocks;
        this.associativity = associativity;
        this.cache = new HashMap[numberOfBlocks / associativity];
        this.lruOrder = new LinkedHashMap[numberOfBlocks / associativity];

        for (int i = 0; i < cache.length; i++) {
            cache[i] = new HashMap<>();
            lruOrder[i] = new LinkedHashMap<>(associativity, 0.75f, true);
        }
    }

    public void incMiss() {
        miss++;
    }

    public void incHit() {
        hit++;
    }
    
    public void directMapped(int blockAddress) {
        int setIndex = blockAddress % (numberOfBlocks / associativity);

        if (!cache[setIndex].containsKey(blockAddress)) {
            handleCacheMiss(setIndex, blockAddress);
            incMiss();
        } else {
            System.out.print("MEM[" + blockAddress + "]" + "HIT \t");
            incHit();
        }

        printCacheContent();
    }
    
    public void twoWayAssoc(int blockAddress) {
    	int setIndex = blockAddress % (numberOfBlocks / associativity);
    

        if (!cache[setIndex].containsKey(blockAddress)) {
            handleCacheMiss(setIndex, blockAddress);
            incMiss();
        } else {
           updateLRU(setIndex, blockAddress);
            System.out.print("MEM[" + blockAddress + "]\t HIT \t");
            incHit();
        }

        printCacheContent();
    }
    
    public void fourWayAssoc(int blockAddress) {
    	int setIndex = blockAddress % (numberOfBlocks / associativity);

        if (!cache[setIndex].containsKey(blockAddress)) {
            handleCacheMiss(setIndex, blockAddress);
            incMiss();
        } else {
            updateLRU(setIndex, blockAddress);
            System.out.print("MEM[" + blockAddress + "]\t HIT \t");
            incHit();
        }

        printCacheContent();
    }
    
    public void fullyAssoc(int blockAddress) {
        if (!cache[0].containsKey(blockAddress)) {
            handleCacheMiss(0, blockAddress);
            incMiss();
        } else {
            updateLRU(0, blockAddress);
            System.out.print("MEM[" + blockAddress + "]\t HIT \t");
            incHit();
        }

        printCacheContent();
    }

    private void handleCacheMiss(int setIndex, int blockAddress) {
        System.out.print("MEM[" + blockAddress + "]\t MISS \t");

        if (cache[setIndex].size() < associativity) {
            cache[setIndex].put(blockAddress, setIndex);
        } else {
            cache[setIndex].put(blockAddress, setIndex);
            evictLRUBlock(setIndex);
        }

        updateLRU(setIndex, blockAddress);
    }

    private void evictLRUBlock(int setIndex) {
        int lruBlock = lruOrder[setIndex].entrySet().iterator().next().getKey();
       // System.out.println("Evicting block " + lruBlock);//
        cache[setIndex].remove(lruBlock);
        lruOrder[setIndex].remove(lruBlock);
    }

    private void updateLRU(int setIndex, int blockAddress) {
        lruOrder[setIndex].remove(blockAddress);
        
        lruOrder[setIndex].put(blockAddress, setIndex);
    }

    private void printCacheContent() {
        System.out.print("Cache Content: ");
        for (int i = 0; i < cache.length; i++) {
            for (int blockAddress : cache[i].keySet()) {
                System.out.print("Mem[" + blockAddress +"]" + " ");
            }
        }
        System.out.println();
        System.out.println();
    }

    private static int getAccesses() {
        return hit + miss;
    }

    private static double getMissRate() {
        return getAccesses() == 0 ? 0 : (double) miss / getAccesses();
    }

    public static void main(String[] args) {
        // Example usage
        Cache2 cache = new Cache2(4, 1); // 4 blocks, 2-way set-associative cache
        System.out.println("Fully Associative:");
        System.out.println();
        // Access block addresses
        cache.directMapped(0);
        cache.directMapped(8);
        cache.directMapped(0);
        cache.directMapped(9);
        cache.directMapped(8);

        System.out.print("Miss rate is: " + getMissRate());
    }
}