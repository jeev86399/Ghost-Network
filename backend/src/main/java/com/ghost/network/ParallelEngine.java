package com.ghost.network;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ParallelEngine {
    // Uses all available CPU cores for maximum performance
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public void startParallelScan(List<File> files, List<String[]> reportData) {
        System.out.println("[*] Starting Parallel Engine with " 
                + Runtime.getRuntime().availableProcessors() + " threads...");

        for (File file : files) {
            executor.execute(() -> {
                // 1. High-speed header reading
                String hex = FastFileReader.getHeaderFast(file);

                // 2. Identify file type
                String type = SignatureMatcher.detectType(hex);

                // 3. If UNKNOWN → Apply Heuristic (NLP-based content analysis)
                if (type.equals("UNKNOWN")) {
                    type = HeuristicLab.identifyByContent(file);
                }

                // 4. Statistical Analysis (Entropy)
                double entropy = EntropyAnalyzer.calculateEntropy(file);

                // 5. Verification
                boolean mismatch = ExtensionChecker.isMismatched(file, type);
                String status = mismatch ? "SUSPICIOUS" : "OK";

                // 6. Thread-safe logging to the report list
                reportData.add(new String[]{
                        file.getName(),
                        type,
                        status,
                        String.format("%.2f", entropy)
                });

                // 7. Console Output (Enhanced Logging)
                System.out.println("[SCANNER][" + Thread.currentThread().getName() 
                        + "] Found " + type + " in " + file.getName());

                if (mismatch) {
                    System.out.println("[⚠️ THREAD: " + Thread.currentThread().getName() 
                            + "] Spoofed: " + file.getName());
                } else {
                    System.out.println("[✅ THREAD: " + Thread.currentThread().getName() 
                            + "] Verified: " + file.getName());
                }
            });
        }

        // Properly shut down the threads after tasks are done
        shutdown();
    }

    private void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}