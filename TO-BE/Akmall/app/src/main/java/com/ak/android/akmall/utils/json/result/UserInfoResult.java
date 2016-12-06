package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by 하영 on 2016-12-06.
 * gkduduu@naver.com
 * what is? : 세팅시 사용자 정보 가져오기
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResult {
    //{userInfo":{"custId":"lotus1121","custNo":72979126,"custName":"송부련"}}
    public String custId;
    public String custNo;
    public String custName;
}
