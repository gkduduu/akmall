package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by 하영 on 2016-12-05.
 * gkduduu@naver.com
 * what is? : changeparam에서 씀
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeParamResult {
    public String param;
    public Action action;
    public String act;
    public String dp;
}
