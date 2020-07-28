package com.sast.material.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class SysMaterial implements Serializable {

    static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String classification;
    private int price;
    private boolean isLoanable;
    private boolean needReview;
    private Date warehousingDate;
    private int total;
    private int remaining;
    private String storageLocation;
    private ArrayList<SysTag> tags;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isLoanable() {
        return isLoanable;
    }

    public void setLoanable(boolean loanable) {
        isLoanable = loanable;
    }

    public boolean isNeedReview() {
        return needReview;
    }

    public void setNeedReview(boolean needReview) {
        this.needReview = needReview;
    }

    public Date getWarehousingDate() {
        return warehousingDate;
    }

    public void setWarehousingDate(Date warehousingDate) {
        this.warehousingDate = warehousingDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public ArrayList<SysTag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<SysTag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "SysMaterial{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classification='" + classification + '\'' +
                ", price=" + price +
                ", isLoanable=" + isLoanable +
                ", needReview=" + needReview +
                ", warehousingDate=" + warehousingDate +
                ", total=" + total +
                ", remaining=" + remaining +
                ", storageLocation='" + storageLocation + '\'' +
                ", tags=" + tags +
                '}';
    }
}
