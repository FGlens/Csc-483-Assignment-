package com.csc483.assignment.search;

import java.util.*;

/**
 * Benchmarks sequential and binary search on a dataset of 100,000 products.
 * Tests best, average, and worst cases and prints a formatted results table.
 *
 * @author [Student Name]
 * @version 1.0
 */
public class SearchBenchmark {

    private static final int DATASET_SIZE = 100_000;
    private static final int MAX_ID       = 200_000;
    private static final int WARMUP_RUNS  = 3;
    private static final int TIMED_RUNS   = 5;

    public static void main(String[] args) {
        System.out.println("Generating dataset of " + DATASET_SIZE + " products...");
        Product[] products = generateDataset(DATASET_SIZE, MAX_ID);

        // Sort for binary search
        Product[] sortedProducts = Arrays.copyOf(products, products.length);
        Arrays.sort(sortedProducts);

        // Pick test IDs
        int bestCaseId     = sortedProducts[0].getProductId();               // first element
        int avgCaseId      = sortedProducts[DATASET_SIZE / 2].getProductId(); // middle element
        int worstCaseId    = MAX_ID + 1;                                      // definitely not in array

        System.out.println("\n================================================================");
        System.out.println("   TECHMART SEARCH PERFORMANCE ANALYSIS (n = 100,000 products)");
        System.out.println("================================================================\n");

        // -------- SEQUENTIAL SEARCH --------
        System.out.println("SEQUENTIAL SEARCH:");
        double seqBest    = benchmark(() -> ProductSearch.sequentialSearchById(sortedProducts, bestCaseId));
        double seqAverage = benchmark(() -> ProductSearch.sequentialSearchById(sortedProducts, avgCaseId));
        double seqWorst   = benchmark(() -> ProductSearch.sequentialSearchById(sortedProducts, worstCaseId));

        System.out.printf("  Best Case  (ID at position 0)      : %.3f ms%n", seqBest);
        System.out.printf("  Average Case (middle ID)           : %.3f ms%n", seqAverage);
        System.out.printf("  Worst Case (ID not found)          : %.3f ms%n%n", seqWorst);

        // -------- BINARY SEARCH --------
        System.out.println("BINARY SEARCH:");
        double binBest    = benchmark(() -> ProductSearch.binarySearchById(sortedProducts, bestCaseId));
        double binAverage = benchmark(() -> ProductSearch.binarySearchById(sortedProducts, avgCaseId));
        double binWorst   = benchmark(() -> ProductSearch.binarySearchById(sortedProducts, worstCaseId));

        System.out.printf("  Best Case  (ID at mid)             : %.3f ms%n", binBest);
        System.out.printf("  Average Case (random ID)           : %.3f ms%n", binAverage);
        System.out.printf("  Worst Case (ID not found)          : %.3f ms%n%n", binWorst);

        // -------- SPEEDUP --------
        double speedup = seqAverage / binAverage;
        System.out.printf("PERFORMANCE IMPROVEMENT: Binary search is ~%.0fx faster on average%n%n", speedup);

        // -------- HYBRID NAME SEARCH --------
        System.out.println("HYBRID NAME SEARCH (HashMap Index):");
        Map<String, Product> nameIndex = ProductSearch.buildNameIndex(sortedProducts);

        String testName = sortedProducts[DATASET_SIZE / 3].getProductName();

        double hybridSearch = benchmark(() -> ProductSearch.hybridSearchByName(nameIndex, testName));
        double naiveSearch  = benchmark(() -> ProductSearch.searchByName(sortedProducts, testName));

        // Benchmark add (use a copy each time to keep array valid)
        final Product[] addTarget = Arrays.copyOf(sortedProducts, sortedProducts.length + 1);
        Product newProd = new Product(MAX_ID - 1, "NewGadget Pro", "Electronics", 49999.99, 10);
        long addStart = System.nanoTime();
        ProductSearch.addProduct(addTarget, newProd);
        long addEnd = System.nanoTime();
        double addTime = (addEnd - addStart) / 1_000_000.0;

        System.out.printf("  Average search time (HashMap)      : %.3f ms%n", hybridSearch);
        System.out.printf("  Average search time (sequential)   : %.3f ms%n", naiveSearch);
        System.out.printf("  Average insert time (sorted array) : %.3f ms%n", addTime);

        System.out.println("\n================================================================");
        System.out.println("               COMPARISON COUNT TABLE");
        System.out.println("================================================================");
        System.out.printf("%-30s %15s %15s%n", "Case", "Sequential", "Binary");
        System.out.println("-".repeat(62));

        ProductSearch.sequentialSearchById(sortedProducts, bestCaseId);
        long seqBestComp = ProductSearch.comparisonCount;
        ProductSearch.binarySearchById(sortedProducts, bestCaseId);
        long binBestComp = ProductSearch.comparisonCount;

        ProductSearch.sequentialSearchById(sortedProducts, avgCaseId);
        long seqAvgComp = ProductSearch.comparisonCount;
        ProductSearch.binarySearchById(sortedProducts, avgCaseId);
        long binAvgComp = ProductSearch.comparisonCount;

        ProductSearch.sequentialSearchById(sortedProducts, worstCaseId);
        long seqWorstComp = ProductSearch.comparisonCount;
        ProductSearch.binarySearchById(sortedProducts, worstCaseId);
        long binWorstComp = ProductSearch.comparisonCount;

        System.out.printf("%-30s %15d %15d%n", "Best Case",    seqBestComp,  binBestComp);
        System.out.printf("%-30s %15d %15d%n", "Average Case", seqAvgComp,   binAvgComp);
        System.out.printf("%-30s %15d %15d%n", "Worst Case",   seqWorstComp, binWorstComp);
        System.out.println("================================================================");
    }

    /**
     * Generates an array of random Product objects with unique IDs.
     *
     * @param size  number of products to generate
     * @param maxId upper bound for product IDs (exclusive)
     * @return array of Product objects with unique IDs
     */
    public static Product[] generateDataset(int size, int maxId) {
        String[] categories = {"Laptops", "Phones", "Tablets", "Cameras", "Audio", "Gaming"};
        String[] names = {"Pro", "Ultra", "Max", "Plus", "Lite", "Edge", "Nova", "Ace"};
        Random rand = new Random(42); // fixed seed for reproducibility

        Set<Integer> usedIds = new HashSet<>();
        Product[] products = new Product[size];

        for (int i = 0; i < size; i++) {
            int id;
            do { id = rand.nextInt(maxId) + 1; } while (usedIds.contains(id));
            usedIds.add(id);

            String name  = names[rand.nextInt(names.length)] + " " + id;
            String cat   = categories[rand.nextInt(categories.length)];
            double price = 5000 + rand.nextDouble() * 500_000;
            int stock    = rand.nextInt(500);

            products[i] = new Product(id, name, cat, price, stock);
        }
        return products;
    }

    /**
     * Runs a Runnable several times and returns the average execution time in milliseconds.
     * Includes warmup runs to allow JIT compilation.
     *
     * @param task the operation to benchmark
     * @return average execution time in milliseconds over TIMED_RUNS
     */
    private static double benchmark(Runnable task) {
        // Warmup
        for (int i = 0; i < WARMUP_RUNS; i++) task.run();

        // Timed runs
        long total = 0;
        for (int i = 0; i < TIMED_RUNS; i++) {
            long start = System.nanoTime();
            task.run();
            total += System.nanoTime() - start;
        }
        return (total / (double) TIMED_RUNS) / 1_000_000.0;
    }
}
