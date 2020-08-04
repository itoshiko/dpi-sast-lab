package com.sast.notice.pojo;

public enum PriorityEnum {
    NORMAL(1, "一般通知"),
    IMPORTANT(2, "重要通知"),
    EMERGENCY(3, "紧急通知"),
    UPMOST(4, "最重要");

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Integer code;

    private String message;

    PriorityEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static PriorityEnum parse(int code) {
        PriorityEnum[] values = values();
        for (PriorityEnum value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new RuntimeException("Unknown code of Priority");
    }
}
