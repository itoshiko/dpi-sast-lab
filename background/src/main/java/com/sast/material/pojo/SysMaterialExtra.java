package com.sast.material.pojo;

import java.util.ArrayList;

public class SysMaterialExtra {

    int mid;
    ArrayList<SysMaterialImg> img;
    ArrayList<SysMaterialDoc> doc;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public ArrayList<SysMaterialImg> getImg() {
        return img;
    }

    public void setImg(ArrayList<SysMaterialImg> img) {
        this.img = img;
    }

    public ArrayList<SysMaterialDoc> getDoc() {
        return doc;
    }

    public void setDoc(ArrayList<SysMaterialDoc> doc) {
        this.doc = doc;
    }

    @Override
    public String toString() {
        return "SysMaterialExtra{" +
                "mid=" + mid +
                ", img=" + img +
                ", doc=" + doc +
                '}';
    }
}
