
package com.ak.android.akplaza.common;

public final class Const {
    private Const() {
    }

    /**
     * 개발용이면 false로 설정, 운영용이면 true로 설정
     */
    public static final boolean IS_PRODUCTION = true;

    public static final String SERVER_ADDR;
    public static final String URL_BASE;
    public static final String URL_LIB;
    public static final String URL_ADDRESS;
    public static final String KCP_ADDRESS;
    public static final String URL_MEMBERS;

    static {
        if (IS_PRODUCTION) {

            SERVER_ADDR = "m.akplaza.com";
            KCP_ADDRESS = "https://smpay.kcp.co.kr";
            URL_MEMBERS = "m.akmembers.com";
        } else {
            SERVER_ADDR = "91.1.105.166";
//        	SERVER_ADDR = "91.3.115.105:8080";
            KCP_ADDRESS = "https://devpggw.kcp.co.kr:8100";
            URL_MEMBERS = "91.1.105.170";
        }

        URL_BASE = "http://" + SERVER_ADDR;
        URL_LIB = URL_BASE + "/app/lib.do?";
        URL_ADDRESS = URL_BASE + "/app/address.do?";
    }
}
