package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by 하영 on 2016-11-30.
 * gkduduu@naver.com
 * what is? : 상품 추가하기 위한 서버연동 후 저장
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddingResult {
    public String resultMsg;
    public String resultCode;
    public ResultData resultDatas;
}
