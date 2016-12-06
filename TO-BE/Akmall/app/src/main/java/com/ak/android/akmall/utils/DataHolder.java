package com.ak.android.akmall.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 하영 on 2016-11-28.
 * gkduduu@naver.com
 * what is? : 중카테고리 json 전송시 넘나길어서 TransationTooLargeException 회피하기 위한 크라스
 */

public class DataHolder {
    private static Map<String, Object> mDataHolder = new ConcurrentHashMap<>();

    public static String putDataHolder(Object data){
        String dataHolderId = UUID.randomUUID().toString();
        mDataHolder.put(dataHolderId, data);
        return dataHolderId;
    }

    public static Object popDataHolder(String key){
        Object obj = mDataHolder.get(key);
        //pop된 데이터는 홀더를 제거
        mDataHolder.remove(key);
        return obj;
    }
}
