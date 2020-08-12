package com.sast.material.pojo;

import java.util.ArrayList;

public class SysMaterialImg {

    static final long serialVersionUID = 1L;

    private int id;
    private int mid;
    private String imgUUID;

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

    public String getImgUUID() {
        return imgUUID;
    }

    public void setImgUUID(String imgUUID) {
        this.imgUUID = imgUUID;
    }

    @Override
    public String toString() {
        return "SysMaterialImg{" +
                "id=" + id +
                ", mid=" + mid +
                ", imgUUID='" + imgUUID + '\'' +
                '}';
    }
}
