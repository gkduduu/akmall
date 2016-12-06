package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by 하영 on 2016-11-29.
 * gkduduu@naver.com
 * what is? : main.do의 카테고리 정보
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateData {
    public String active_yn;
    public String item_title;
    public String new_tab_yn;
    public String template_id;
    public String template_path;
    public String template_type_code;
    public String template_type_name;
    public String template_url;
}
