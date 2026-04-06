# CSC 483.1 — Algorithms Analysis and Design
## Assignment: Algorithm Design, Analysis, and Optimization for Real-World Systems

**Student:** NKEEH FAVOUR GODWIN  
**Matric No:** U2022/5570030  
**Department:** Computer Science  
**University:** University of Port Harcourt  
**Session:** 2025/2026 — First Semester  
**Submission Date:** April 5, 2026  

---

## 📁 Project Structure

```
src/com/csc483/assignment/
├── search/
│   ├── Product.java           # Product entity class
│   ├── ProductSearch.java     # Sequential, Binary & Hybrid search
│   └── SearchBenchmark.java   # Performance benchmark program
├── sorting/
│   ├── SortingAlgorithms.java # Insertion, Merge & Quick Sort
│   ├── DataGenerator.java     # Test dataset generator
│   └── SortingBenchmark.java  # Sorting performance benchmark
└── test/
    ├── ProductSearchTest.java  # JUnit 5 tests for search
    └── SortingAlgorithmsTest.java # JUnit 5 tests for sorting
```

---

## ⚙️ Requirements

- Java 11 or higher
- JUnit 5 (for running tests)

---

## 🔧 Compilation

Navigate to the project root and compile all source files:

```bash
mkdir -p out
javac -d out $(find src -name "*.java" ! -path "*/test/*")
```

---

## ▶️ Running the Programs

### Question 1 — Search Benchmark
```bash
java -cp out com.csc483.assignment.search.SearchBenchmark
```

### Question 2 — Sorting Benchmark
```bash
java -cp out com.csc483.assignment.sorting.SortingBenchmark
```

---

## 🧪 Running JUnit Tests

Download JUnit 5 standalone jar, then:

```bash
java -jar junit-platform-console-standalone.jar \
     --class-path out \
     --scan-class-path
```

---

## 📊 Sample Output

### Search Benchmark
```
================================================================
   TECHMART SEARCH PERFORMANCE ANALYSIS (n = 100,000 products)
================================================================
SEQUENTIAL SEARCH:
  Best Case  (ID at position 0)      : 0.021 ms
  Average Case (middle ID)           : 48.234 ms
  Worst Case (ID not found)          : 91.456 ms

BINARY SEARCH:
  Best Case  (ID at mid)             : 0.001 ms
  Average Case (random ID)           : 0.089 ms
  Worst Case (ID not found)          : 0.094 ms

PERFORMANCE IMPROVEMENT: Binary search is ~542x faster on average
================================================================
```

### Sorting Benchmark
```
================================================================
  SORTING ALGORITHMS COMPARISON - RANDOM
================================================================
Size       Algorithm     Time (ms)   Comparisons      Swaps
100        Insertion         0.000         2,475      2,475
           Merge             0.001           332        664
           Quick             0.000           398        265
...
================================================================
```

---

## ⚠️ Known Limitations

- `addProduct()` requires a pre-allocated array with one free null slot at the end
- Sorting algorithms operate on `int[]` arrays only
- Benchmark timings vary depending on JVM version and hardware

---

## 📚 References

1. Cormen et al. — *Introduction to Algorithms*, 4th Edition
2. Sedgewick & Wayne — *Algorithms*, 4th Edition
3. Java Documentation: https://docs.oracle.com/en/java/
4. JUnit 5 User Guide: https://junit.org/junit5/docs/current/user-guide/
