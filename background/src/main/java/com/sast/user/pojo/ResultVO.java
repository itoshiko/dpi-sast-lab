package com.sast.user.pojo;

import com.sast.user.utils.DateUtil;
import com.sast.user.utils.ResultEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public final class ResultVO implements Serializable {

    private static final long serialVersionUID = 1725159680599612404L;


    /**
     * 返回msg，object，以及token
     * 返回的code为默认
     *
     * @param message
     * @param data
     * @param jwtToken
     * @return
     */
    public final static Map<String, Object> success(String message, Object data, String jwtToken, Boolean success) {
        Map<String, Object> map = new HashMap<>();
        map.put("jwtToken", jwtToken);
        map.put("code", ResultEnum.SUCCESS.getCode());
        map.put("message", message);
        map.put("success", success);
        map.put("data", data);
        map.put("date", DateUtil.getTime());
        return map;
    }


    /**
     * 返回默认的信息
     *
     * @return
     */
    public final static Map<String, Object> success(String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", name);
        map.put("code", ResultEnum.SUCCESS.getCode());
        map.put("message", ResultEnum.SUCCESS.getMessage());
        map.put("data", null);
        map.put("success", true);
        map.put("date", DateUtil.getTime());
        return map;
    }

    public final static Map<String, Object> failure(int code, String message, Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("message", message);
        map.put("data", data);
        map.put("success", false);
        map.put("date", DateUtil.getTime());
        return map;
    }

    public final static Map<String, Object> failure(int code, String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("message", message);
        map.put("data", null);
        map.put("success", false);
        map.put("date", DateUtil.getTime());
        return map;
    }

    public final static Map<String, Object> failure(ResultEnum respCode, Object data, Boolean success) {
        return getStringObjectMap(respCode, data, success);
    }

    public final static Map<String, Object> failure(ResultEnum respCode, Boolean success) {
        return getStringObjectMap(respCode, success);
    }

    /*
     * 成功返回特定的状态码和信息
     * */
    public final static Map<String, Object> result(ResultEnum respCode, Object data, Boolean success) {
        return getStringObjectMap(respCode, data, success);
    }

    private static Map<String, Object> getStringObjectMap(ResultEnum respCode, Object data, Boolean success) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", respCode.getCode());
        map.put("message", respCode.getMessage());
        map.put("data", data);
        map.put("success", success);
        map.put("date", DateUtil.getTime());
        return map;
    }

    /*
     * 成功返回特定的状态码和信息
     * */
    public final static Map<String, Object> result(ResultEnum respCode, Boolean success) {
        return getStringObjectMap(respCode, success);
    }

    private static Map<String, Object> getStringObjectMap(ResultEnum respCode, Boolean success) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", respCode.getCode());
        map.put("message", respCode.getMessage());
        map.put("data", null);
        map.put("success", success);
        map.put("date", DateUtil.getTime());
        return map;
    }

}

