package com.antoine.alphatheorytakehome.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DataSets")
public class DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Date dateUploaded;

    private String fileName;

    private String uploaderIp;

    public DataSet() {}

    public DataSet(String fileName, String uploaderIp) {
        this.fileName = fileName;
        this.uploaderIp = uploaderIp;
        this.dateUploaded = new Date();
    }

    public Integer getId() {
        return id;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUploaderIp() {
        return uploaderIp;
    }
}