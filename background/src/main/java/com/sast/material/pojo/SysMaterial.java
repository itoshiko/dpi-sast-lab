package com.sast.material.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sast.form.service.TagListConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class SysMaterial implements Serializable {

    @ExcelIgnore
    static final long serialVersionUID = 1L;

    @ExcelProperty(value = "ID", index = 1)
    private int id;
    @ExcelProperty(value = "名称", index = 0)
    private String name;
    @ExcelProperty(value = "分类号", index = 2)
    private String classification;
    @ExcelProperty(value = "单价", index = 3)
    private int price;
    @ExcelProperty(value = "是否可外借", index = 6)
    private boolean canLoan;
    @ExcelProperty(value = "外借是否需要审核", index = 7)
    private boolean needReview;
    @ExcelProperty(value = "入库日期", index = 8)
    private Date warehousingDate;
    @ExcelProperty(value = "总量", index = 4)
    private int total;
    @ExcelProperty(value = "在库数量", index = 5)
    private int remaining;
    @ExcelProperty(value = "存放位置", index = 9)
    private String storageLocation;
    @ExcelProperty(value = "标签", index = 10, converter = TagListConverter.class)
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

    public boolean isCanLoan() {
        return canLoan;
    }

    public void setCanLoan(boolean canLoan) {
        this.canLoan = canLoan;
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
                ", isLoanable=" + canLoan +
                ", needReview=" + needReview +
                ", warehousingDate=" + warehousingDate +
                ", total=" + total +
                ", remaining=" + remaining +
                ", storageLocation='" + storageLocation + '\'' +
                ", tags=" + tags +
                '}';
    }
}
