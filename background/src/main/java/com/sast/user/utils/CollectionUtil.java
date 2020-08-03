package com.sast.user.utils;

import java.util.Iterator;
import java.util.Map;

public class CollectionUtil {

    /**
     * @param map 取值的集合
     * @param key 所想取值的集合的key
     * @return 返回key对应的value
     */
    public static String getMapValue(Map<String,Object> map,String key){
        String result = null;
        if(map != null){
            Iterator<String> iterable = map.keySet().iterator();
            while (iterable.hasNext()){
                Object object = iterable.next();
                if(key.equals(object))
                    if(map.get(object) != null)
                        result = map.get(object).toString();
            }
        }

        return result;
    }

}
