package com.mr.detector.bean;

/**
 * Created by MR on 2017/5/10.
 */

public class ReportFile {
    private String fileName;
    private String fileSize;
    private String fileTime;
    private String filePath;

    public ReportFile() {

    }

    public ReportFile(String fileName, String fileSize, String fileTime,String filePath) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileTime = fileTime;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileTime() {
        return fileTime;
    }

    public String getFilePath() {
        return filePath;
    }
}
