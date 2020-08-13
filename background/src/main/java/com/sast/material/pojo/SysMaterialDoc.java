package com.sast.material.pojo;

import java.util.ArrayList;

public class SysMaterialDoc {

    static final long serialVersionUID = 1L;

    private int id;
    private int mid;
    private String docUUID;
    private String docType;
    private String docName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getDocUUID() {
        return docUUID;
    }

    public void setDocUUID(String docUUID) {
        this.docUUID = docUUID;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    @Override
    public String toString() {
        return "SysMaterialDoc{" +
                "id=" + id +
                ", mid=" + mid +
                ", docUUID='" + docUUID + '\'' +
                ", docType='" + docType + '\'' +
                '}';
    }
}
