
package com.ak.android.akmall.common;

public final class Const {
    private Const() {
    }

    /**
     * 개발용이면 false로 설정, 운영용이면 true로 설정
     */
    public static final boolean IS_PRODUCTION = false;//p65458 todo false -> true

    public static final String GCM_SENDER_ID;
    public static final String SERVER_ADDR;
    public static final String URL_BASE;
    public static final String URL_LIB;

    static {
        if (IS_PRODUCTION) {
            GCM_SENDER_ID = "290048048463";
        	//GCM_SENDER_ID = "828409247060";
            SERVER_ADDR = "m.akmall.com";
        } else {
            GCM_SENDER_ID = "828409247060";
            //SERVER_ADDR = "91.3.200.68";
            //민석선임
            //SERVER_ADDR = "91.3.115.209";
            //창욱전임 
            //SERVER_ADDR = "91.3.115.184:8082";
            //SERVER_ADDR = "91.3.115.179:8082";
            SERVER_ADDR = "devm.akmall.com";
            //SERVER_ADDR = "91.3.115.209:8082";//실제 동작 url p65458 todo
            
        }

        URL_BASE = "http://" + SERVER_ADDR;
        URL_LIB = URL_BASE + "/app/lib.do?";
    }


}
