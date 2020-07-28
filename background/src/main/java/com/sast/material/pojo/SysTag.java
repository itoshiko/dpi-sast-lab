package com.sast.material.pojo;

public class SysTag {
    private int tagId;
    private String tagName;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public SysTag() {
    }

    public SysTag(int tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "SysTag{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
