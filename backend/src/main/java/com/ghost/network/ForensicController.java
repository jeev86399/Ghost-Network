package com.ghost.network;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/api/forensics")
@CrossOrigin(origins = "http://localhost:3000")
public class ForensicController {

    /**
     * DISK SCAN: Updated for Phase 4.0 with Threat Heuristics
     */
    @GetMapping("/scan")
    public List<ForensicResult> startScan() {
        String myPath = System.getProperty("user.home") + "/Desktop/GhostTest"; 
        File testFolder = new File(myPath);
        List<String[]> rawResults = Collections.synchronizedList(new ArrayList<>());
        List<ForensicResult> finalResults = new ArrayList<>();

        if (testFolder.exists() && testFolder.isDirectory()) {
            List<File> foundFiles = Scanner.getAllFiles(testFolder);
            ParallelEngine engine = new ParallelEngine();
            engine.startParallelScan(foundFiles, rawResults);

            for (String[] res : rawResults) {
                File diskFile = new File(testFolder, res[0]);
                String diskHash = HexUtils.generateHash(diskFile);
                
                // PHASE 4.0: Calculate AI Threat Probability
                double threatScore = HeuristicEngine.calculateThreat(
                    Double.parseDouble(res[3]), res[2], res[1]
                );

                // Updated to 6-parameter constructor
                finalResults.add(new ForensicResult(
                    res[0], 
                    res[1], 
                    res[2], 
                    Double.parseDouble(res[3]), 
                    diskHash,
                    threatScore
                ));
            }
        }
        return finalResults;
    }

    /**
     * BULK UPLOAD & RECONSTRUCTION: Phase 4.0 Specter Engine
     */
    @PostMapping("/upload-multiple")
    public List<ForensicResult> uploadMultiple(@RequestParam("files") MultipartFile[] files) {
        List<ForensicResult> list = Collections.synchronizedList(new ArrayList<>());
        ParallelEngine engine = new ParallelEngine();
        
        java.util.Arrays.stream(files).parallel().forEach(file -> {
            File temp = null;
            try {
                byte[] fileBytes = file.getBytes();
                if (fileBytes.length == 0) return;

                // 1. Digital Signature Detection
                String hexHeader = HexUtils.bytesToHex(java.util.Arrays.copyOf(fileBytes, 16));
                String detectedType = SignatureMatcher.detectType(hexHeader);

                // 2. Surgical Carving (Phase 2.0/4.0)
                byte[] carvedData = CarvingEngine.stitchFile(fileBytes, detectedType);

                String cleanName = file.getOriginalFilename();
                if (cleanName != null && cleanName.contains("/")) {
                    cleanName = cleanName.substring(cleanName.lastIndexOf("/") + 1);
                }

                temp = File.createTempFile("ghost_" + UUID.randomUUID(), "_" + cleanName);
                Files.write(temp.toPath(), carvedData);
                
                // 3. Integrity Verification (SHA-256)
                String fileHash = HexUtils.generateHash(temp);
                
                // 4. Deep Parallel Analysis
                List<String[]> res = new ArrayList<>();
                engine.startParallelScan(Collections.singletonList(temp), res);
                
                if (!res.isEmpty()) {
                    String[] r = res.get(0);
                    
                    // 5. PHASE 4.0: AI Heuristic Threat Scoring
                    double threatProb = HeuristicEngine.calculateThreat(
                        Double.parseDouble(r[3]), r[2], r[1]
                    );

                    // Final Forensic Result Construction
                    list.add(new ForensicResult(
                        cleanName, 
                        r[1], 
                        r[2], 
                        Double.parseDouble(r[3]), 
                        fileHash,
                        threatProb
                    ));
                }
                
            } catch (Exception e) {
                System.err.println("[-] Specter Engine Error: " + file.getOriginalFilename());
            } finally {
                if (temp != null && temp.exists()) temp.delete();
            }
        });
        
        return list;
    }
   @GetMapping("/preview/{fileName}")
public ResponseEntity<String> previewCarvedContent(@PathVariable String fileName) {
    try {
        // 1. Check the DEEP SCAN path (Desktop)
        String desktopPath = System.getProperty("user.home") + "/Desktop/GhostTest/" + fileName;
        File desktopFile = new File(desktopPath);
        
        if (desktopFile.exists()) {
            byte[] data = Files.readAllBytes(desktopFile.toPath());
            return ResponseEntity.ok(new String(data));
        }

        // 2. Check the UPLOAD path (Temp Folder)
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File[] matches = tempDir.listFiles((dir, name) -> name.contains(fileName));
        
        if (matches != null && matches.length > 0) {
            byte[] data = Files.readAllBytes(matches[0].toPath());
            return ResponseEntity.ok(new String(data));
        }

        return ResponseEntity.status(404).body("Evidence Link Severed: File not found in local or temp storage.");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Surgical Extraction Failed: " + e.getMessage());
    }
}
}