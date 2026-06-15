package com.ghost.network;

public class ForensicResult {
    public String fileName;
    public String detectedType;
    public String status;
    public double entropy;
    public String hash;
    public double threatScore;

    public ForensicResult(String fileName, String detectedType, String status, double entropy, String hash, double threatScore){
        this.fileName = fileName;
        this.detectedType = detectedType;
        this.status = status;
        this.entropy = entropy;
        this.hash = hash;
        this.threatScore = threatScore;
    }

    // JACKSON NEEDS THESE GETTERS TO SEND DATA TO REACT
    public String getFileName() { return fileName; }
    public String getDetectedType() { return detectedType; }
    public String getStatus() { return status; }
    public double getEntropy() { return entropy; }
    
    // Setters (Optional but good practice)
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setDetectedType(String detectedType) { this.detectedType = detectedType; }
    public void setStatus(String status) { this.status = status; }
    public void setEntropy(double entropy) { this.entropy = entropy; }
}