package com.math.yang.mathyang.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by zhangtx on 2018/1/10.
 */
@Table("download_tb")
public class DownloadBean implements Serializable {
    public static final String DOWNLOAD_STATUS = "status";
    private int status;
    /**
     * 下载进度
     */
    private int progress;
    /**
     * 下载id
     */
    @Column("id")
    private String downloadId;
    private String downloadUrl;
    private String bookid;
    private int totalBytes;
    private int sofarBytes;

    public int getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }

    public int getSofarBytes() {
        return sofarBytes;
    }

    public void setSofarBytes(int sofarBytes) {
        this.sofarBytes = sofarBytes;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }
}
